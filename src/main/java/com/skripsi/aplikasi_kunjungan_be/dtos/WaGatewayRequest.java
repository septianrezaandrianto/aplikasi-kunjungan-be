package com.skripsi.aplikasi_kunjungan_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaGatewayRequest {

    private String target;
    private String message;
    private String countryCode;
    
}
