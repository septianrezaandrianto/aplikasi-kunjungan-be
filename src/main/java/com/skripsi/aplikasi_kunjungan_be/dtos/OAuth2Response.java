package com.skripsi.aplikasi_kunjungan_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2Response {

    private String access_token;
    private String token_type;
    private int expires_in;

}