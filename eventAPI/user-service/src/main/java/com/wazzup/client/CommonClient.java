package com.wazzup.client;

import com.wazzup.dto.common.ParameterDTO;
import com.wazzup.enums.CommonUrlE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
public class CommonClient {
    private final WebClient COMMON_WEBCLIENT;

    public CommonClient(@Value("${common.url}") String commonUrl) {
        this.COMMON_WEBCLIENT = WebClient.builder()
                .baseUrl(commonUrl)
                .build();
    }
    //todo ogarnac wyjatki jaki rzuca ta usluga
    public ParameterDTO getParameterByCode(String code) {
        return COMMON_WEBCLIENT.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CommonUrlE.GET_PARAMETER_BY_VALUE.getUrl())
                        .queryParam("code", code)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ParameterDTO.class)
                .block();
    }
}
