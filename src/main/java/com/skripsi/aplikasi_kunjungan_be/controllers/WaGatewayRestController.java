package com.skripsi.aplikasi_kunjungan_be.controllers;

import com.skripsi.aplikasi_kunjungan_be.dtos.WaGatewayRequest;
import com.skripsi.aplikasi_kunjungan_be.rest.WaGatewayRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/waGateway")
public class WaGatewayRestController {
    @Autowired
    private WaGatewayRest waGatewayRest;

    @PostMapping("/sendMessage")
    public Mono<String> sendMessage(@RequestBody WaGatewayRequest waGatewayRequest) {
        return waGatewayRest.sendMessage(waGatewayRequest);
    }
}
