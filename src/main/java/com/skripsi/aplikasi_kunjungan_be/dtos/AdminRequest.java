package com.skripsi.aplikasi_kunjungan_be.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRequest {

    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private String role;
    private String fullName;

}
