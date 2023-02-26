package com.wazzup.common.service;

import com.wazzup.common.dto.ParameterDTO;
import com.wazzup.common.exception.ParameterNotFoundException;
import com.wazzup.common.repository.ParameterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ParameterService {
    private final ParameterRepository PARAMETER_REPOSITORY;
    //todo mapper parametrow na dto zrobic
    public ParameterDTO getParameterByCode(String code) {
        return ParameterDTO.of(PARAMETER_REPOSITORY.findByCode(code).orElseThrow(() -> new ParameterNotFoundException()));
    }
}
