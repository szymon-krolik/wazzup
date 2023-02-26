package com.wazzup.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wazzup.dto.CreateUserDTO;
import com.wazzup.repository.UserRepository;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.wazzup.utils.TestUtils.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    private final String POST_CREATE_USER_ACCOUNT = "/api/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

   @Autowired
   private ResourceLoader resourceLoader;
    @Test
    public void shouldReturnSuccess() throws Exception {
        //given
        String requestFileJsonPath = "classpath:request/user/create-new-user.json";
        //when
         mockMvc.perform(post(POST_CREATE_USER_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readJsonFromFile(requestFileJsonPath, resourceLoader))
                         .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    public void shouldReturnConflict() throws Exception {
        //given
        String requestFileJsonPath = "classpath:request/user/create-new-user.json";
        //when
        mockMvc.perform(post(POST_CREATE_USER_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJsonFromFile(requestFileJsonPath, resourceLoader))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResponse();
    }

    private CreateUserDTO buildCreateUserDto() {
        return CreateUserDTO.builder()
                .email("sz.krolik@interia.pl")
                .phoneNumber("+48123123123")
                .name("Szymon")
                .password("test1")
                .matchingPassword("test1")
                .birthDateS("26/06/1999")
                .city("Rybnik")
                .build();

    }
}
