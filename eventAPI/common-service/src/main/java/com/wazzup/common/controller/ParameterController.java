package com.wazzup.common.controller;

import com.wazzup.common.dto.ParameterDTO;
import com.wazzup.common.service.ParameterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameter")
@AllArgsConstructor
public class ParameterController {

    private final ParameterService PARAMETER_SERVICE;

    @GetMapping("/by-code")
    @ResponseBody
    public ParameterDTO getParameterByCode(@RequestParam("code") String code) {
        return PARAMETER_SERVICE.getParameterByCode(code);
    }
}
