package com.wazzup.repository;

import com.wazzup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE upper(u.email) = upper(:email) OR u.phoneNumber = upper(:phoneNumber) ")
    Optional<User> findByEmailOrPhoneNumber(@Param("email") String email, @Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM User u WHERE upper(u.email) = upper(:email)")
    Optional<User> findByEmail(@Param("email") String email);

}
