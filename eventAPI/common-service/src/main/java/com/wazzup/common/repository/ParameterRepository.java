package com.wazzup.common.repository;

import com.wazzup.common.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    @Query("SELECT p FROM Parameter p WHERE  p.code = :parameterCode")
    Optional<Parameter> findByCode(@Param("parameterCode") String code);
}
