package com.skripsi.aplikasi_kunjungan_be.rest;

import com.skripsi.aplikasi_kunjungan_be.dtos.WaGatewayRequest;
import reactor.core.publisher.Mono;

public interface WaGatewayRest {

    Mono<String> sendMessage(WaGatewayRequest waGatewayRequest);
}
