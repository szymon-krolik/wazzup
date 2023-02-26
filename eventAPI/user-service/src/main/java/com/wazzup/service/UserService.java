package com.wazzup.service;




import com.wazzup.client.CommonClient;
import com.wazzup.dto.LoginUserDTO;
import com.wazzup.dto.UserInformationDTO;
import com.wazzup.dto.notification.CreateAccountDTO;
import com.wazzup.entity.ConfirmationToken;
import com.wazzup.entity.RefreshToken;
import com.wazzup.entity.UserRole;
import com.wazzup.enums.ParameterCodeE;
import com.wazzup.exception.*;
import com.wazzup.repository.UserRepository;
import com.wazzup.dto.CreateUserDTO;
import com.wazzup.entity.User;
import com.wazzup.enums.AccountType;
import com.wazzup.mapper.UserMapper;
import com.wazzup.repository.UserRoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import io.netty.util.internal.StringUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository USER_REPOSITORY;
    private final UserMapper USER_MAPPER;
    private final CommonClient COMMON_CLIENT;
    private final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER;
    private final UserRoleRepository USER_ROLE_REPOSITORY;
    private Map<String, String> validationError = new HashMap<>();
    private final ConfirmationTokenService CONFIRMATION_TOKEN_SERVICE;
    private final KafkaTemplate<String, CreateAccountDTO> KAFKA_TEMPLATE;

    private final RefreshTokenService REFRESH_TOKEN_SERVICE;
    //todo Dodac szyfrowanie danych z frontu i potem odszyfrowac, mapowanie daty urodzenia naprawić
    public void createUser(CreateUserDTO dto, HttpServletRequest request, HttpServletResponse response) {
        CookieService.killAllCookies(request, response);
        if (checkIfUserExistByEmailOrPhoneNumber(dto.getEmail(), dto.getPhoneNumber()))
            throw new UserAlreadyExistException();

        if (!validationError.isEmpty())
            validationError.clear();
        validationError = Validator.createUserValidator(dto);
        if (!validationError.isEmpty())
            throw new RuntimeException();

        dto.setPassword(encodePassword(dto.getPassword()));
        UserRole userRole = USER_ROLE_REPOSITORY.findByName("UNAUTHENTICATED_USER").orElseThrow(() -> new RoleNotFoundException());

        dto.setUserRole(Collections.singletonList(userRole));

        try {
            User prepareUser = USER_MAPPER.toEntity(dto);
            prepareUser.setAccountType(AccountType.UNAUTH_USR);
            prepareUser.setBirthdate(LocalDate.now());
            User savedUser = USER_REPOSITORY.save(prepareUser);
            final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(savedUser);
            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
            /**
             * Create token for unauth user
             */

            String token = JwtTokenService.generateJwtoken(savedUser.getEmail(), null);
            Cookie cookieToken = CookieService.createTokenCookie(token);
            CookieService.addCookieToResponse(response, cookieToken);
            KAFKA_TEMPLATE.send("notificationTopic", new CreateAccountDTO(savedUser.getEmail(), ServiceFunctions.buildCreateAccountMailMessage(savedUser.getName(), CONFIRMATION_TOKEN.getConfirmationToken())));
        } catch (Exception ex) {
            log.error("Err. create account: {}", ex.getStackTrace());
            throw ex;
        }
    }

    private boolean checkIfUserExistByEmailOrPhoneNumber(String email, String phoneNumber) {
        return USER_REPOSITORY.findByEmailOrPhoneNumber(email, phoneNumber).isPresent();
    }

    private String encodePassword(String password) {
        return BCRYPT_PASSWORD_ENCODER.encode(password);
    }

    public void confirmUser(String token, HttpServletResponse response, HttpServletRequest request) {
        ConfirmationToken confirmationToken = CONFIRMATION_TOKEN_SERVICE.getByToken(token);
        int expirationTime = getParameterValueByCode(ParameterCodeE.CZAS_WAZN_LINKU.getDescription());

        if (!activeLink(confirmationToken.getCreatedAt(), expirationTime)) {
            throw new LinkExpiredException();
        }

        User user = confirmationToken.getUser();
        user.setAccountType(AccountType.USR);
        user.setEnabled(true);

        UserRole userRole = USER_ROLE_REPOSITORY.findByName("USER").orElseThrow(() -> new RoleNotFoundException());

        /**
         * Usuwanie starej roli z listy oraz dodanie roli potwierdzonego użytkownika
         */
        List<UserRole> currentUserRoles = user.getRoles();
        currentUserRoles.removeIf(x -> x.getRoleName().equals("UNAUTHENTICATED_USER"));

        currentUserRoles.add(userRole);
        user.setRoles(currentUserRoles);

        try {
            log.info("USER_FROM_TOKEN: {}", user);
            USER_REPOSITORY.save(user);
            CONFIRMATION_TOKEN_SERVICE.deleteConfirmationToken(confirmationToken.getId());
            /**
             * Tworzenie nowego JWToken ponieważ zmieniła się rola użytkownika, która jest wykorzystywana do autoryzacji przy zapytaniach API
             */
            Claims claims = new DefaultClaims();
            List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            claims.put("authorities", authorities);
            CookieService.killAllCookies(request, response);
        } catch (Exception ex) {
            log.error("Błąd podczas aktywowania użytkownika o id: %s", user.getId());
            throw ex;
        }

    }

    public UserInformationDTO loginUser(LoginUserDTO loginUserDTO, HttpServletResponse response) {
        if ((ServiceFunctions.isNull(loginUserDTO.getEmail()) || ServiceFunctions.isNull(loginUserDTO.getPhoneNumber())) && ServiceFunctions.isNull(loginUserDTO.getPassword()))
            throw new IncorrectCredentialsException();

        Optional<User> user = getUserByEmailOrPhoneNumber(loginUserDTO.getEmail(), loginUserDTO.getPhoneNumber());
        if (user.isEmpty())
            throw new UserNotFoundException();

        checkIfAccountIsLocked(user.get());
        checkIfAccountIsActive(user.get());
        if (!BCRYPT_PASSWORD_ENCODER.matches(loginUserDTO.getPassword(), user.get().getPassword()))
            throw new IncorrectCredentialsException();

        RefreshToken refreshToken = REFRESH_TOKEN_SERVICE.createRefreshToken(user.get().getId()).getRefreshToken();
        String[] refreshTokenArray = new String[]{refreshToken.getToken()};
        UserInformationDTO userInformationDTO = USER_MAPPER.toUserInformation(user.get());
        userInformationDTO.setRefreshToken((refreshToken != null && refreshTokenArray.length > 0) ? refreshTokenArray[0] : "");

        /**
         * Create JWToken based on authorities and user email
         */
        userInformationDTO.setAuthorities(user.get().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        String token = JwtTokenService.generateJwtoken(userInformationDTO.getEmail(), JwtTokenService.createClaims(userInformationDTO.getAuthorities()));
        Cookie cookieToken = CookieService.createTokenCookie(token);
        response.addCookie(cookieToken);
        return userInformationDTO;
    }

    private int getParameterValueByCode(String parameterCode) {
        return Integer.valueOf(COMMON_CLIENT.getParameterByCode(parameterCode).getValue());
    }

    public boolean activeLink(LocalDateTime tokenDate, int expirationTime) {
        return tokenDate.isAfter(LocalDateTime.now().minusMinutes(expirationTime));
    }

    public Optional<User> getUserByEmailOrPhoneNumber(String email, String phoneNumber) {
        return USER_REPOSITORY.findByEmailOrPhoneNumber(email, phoneNumber);
    }

    private void checkIfAccountIsLocked(User user) {
        if (user.isLocked() || !user.isAccountNonExpired())
            throw new UserAccountLockedException();
    }

    private void checkIfAccountIsActive(User user) {
        if (!user.isEnabled())
            throw new UserAccountIsNotActiveException();
    }

    private User getUserByEmail(String email) {
        return USER_REPOSITORY.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
    }

    public UserInformationDTO getUserProfile() {
        User user = getUserByEmail(getUserEmailFromContext());
        return USER_MAPPER.toUserInformation(user);
    }

    private String getUserEmailFromContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtil.isNullOrEmpty(email))
            throw new SessionNotFoundException();
        return email;
    }

    @Transactional
    public void resendActivationLink(HttpServletRequest httpServletRequest) {
        Cookie tokenCookie = CookieService.getCookieFromRequest(httpServletRequest, "TOKEN");
        if (tokenCookie == null)
            throw new CookieNotFoundException();
        String email = JwtTokenService.getSubjectFromToken(tokenCookie.getValue());

        /**
         * Delete old confirmation token and create new one
         */
        try {
            User user = getUserByEmail(email);
            CONFIRMATION_TOKEN_SERVICE.deleteByUser(user);

            final ConfirmationToken CONFIRMATION_TOKEN = new ConfirmationToken(user);
            CONFIRMATION_TOKEN_SERVICE.saveConfirmationToken(CONFIRMATION_TOKEN);
            KAFKA_TEMPLATE.send("notificationTopic", new CreateAccountDTO(user.getEmail(), ServiceFunctions.buildCreateAccountMailMessage(user.getName(), CONFIRMATION_TOKEN.getConfirmationToken())));
        } catch (Exception ex) {
            log.info("Err. db operation: {}", ex.getStackTrace());
        }
    }
}
