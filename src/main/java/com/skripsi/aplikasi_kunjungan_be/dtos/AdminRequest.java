package com.skripsi.aplikasi_kunjungan_be.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRequest {

    @NotBlank(message = "Username tidak boleh kosong")
    @NotNull(message = "Username tidak boleh kosong")
    @Size(max = 35, message = "Panjang username maksimal 35 karakter")
    private String username;
    @NotBlank(message = "Password tidak boleh kosong")
    @NotNull(message = "Password tidak boleh kosong")
    private String password;

    @NotBlank(message = "Nomor Hp tidak boleh kosong")
    @NotNull(message = "Nomor Hp tidak boleh kosong")
    @Pattern(regexp = "\\d+", message = "Format nomor Hp tidak valid")
    @Size(max = 13, message = "Panjang nomor Hp maksimal 13 karakter")
    private String phoneNumber;
    private String address;
    @NotBlank(message = "Role tidak boleh kosong")
    @NotNull(message = "Role tidak boleh kosong")
    private String role;
    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    @NotNull(message = "Nama lengkap tidak boleh kosong")
    @Size(max = 50, message = "Panjang nama lengkap maksimal 50 karakter")
    private String fullName;

}
