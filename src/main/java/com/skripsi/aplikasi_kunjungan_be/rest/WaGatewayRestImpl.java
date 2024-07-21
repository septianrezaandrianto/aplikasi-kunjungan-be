package com.skripsi.aplikasi_kunjungan_be.rest;

import com.skripsi.aplikasi_kunjungan_be.dtos.WaGatewayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WaGatewayRestImpl implements WaGatewayRest {

    private final WebClient webClient;
    @Autowired
    public WaGatewayRestImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Value("${wa.token}")
    private String waToken;


    @Override
    public Mono<String> sendMessage(WaGatewayRequest waGatewayRequest) {
        return webClient.post()
                .uri("https://api.fonnte.com/send")
                .header("Authorization", waToken)
                .header("Content-Type", "application/json")
                .bodyValue(waGatewayRequest)
                .retrieve()
                .bodyToMono(String.class);
    }
}
