package com.skripsi.aplikasi_kunjungan_be.dtos;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRequest {

    @NotBlank(message = "Id visitor tidak boleh kosong")
    @NotNull(message = "Id visitor tidak boleh kosong")
    @Size(max = 35, message = "Panjang Id visitor maximal 35 karakter")
    private String visitorIdNumber;

    @NotBlank(message = "Nomor identitas tidak boleh kosong")
    @NotNull(message = "Nomor identitas tidak boleh kosong")
    @Pattern(regexp = "\\d+", message = "Format nomor identitas tidak valid")
    @Size(min = 16, max = 16, message = "Panjang nomor identitas harus 16 karakter")
    private String identitasNumber;

    @NotBlank(message = "Nomor Hp tidak boleh kosong")
    @NotNull(message = "Nomor Hp tidak boleh kosong")
    @Pattern(regexp = "\\d+", message = "Format nomor Hp tidak valid")
    @Size(max = 13, message = "Panjang nomor Hp maximal 13 karakter")
    private String phoneNumber;
    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    @NotNull(message = "Nama lengkap tidak boleh kosong")
    @Size(max = 50, message = "Panjang nama lengkap maksimal 50 karakter")
    private String fullName;
    @NotBlank(message = "Nama kantor tidak boleh kosong")
    @NotNull(message = "Nama kantor tidak boleh kosong")
    private String officeName;
    @Email(message = "Format email tidak valid")
    private String email;
    @NotBlank(message = "Tanggal mulai kunjungan tidak boleh kosong")
    @NotNull(message = "Tanggal mulai kunjungan tidak boleh kosong")
    private String visitDateStart;
    @NotBlank(message = "Tanggal selesai kunjungan tidak boleh kosong")
    @NotNull(message = "Tanggal selesai kunjungan tidak boleh kosong")
    private String visitDateEnd;
    private String note;
    @NotBlank(message = "Id admin tidak boleh kosong")
    @NotNull(message = "Id admin tidak boleh kosong")
    private String adminId;
    @NotBlank(message = "Nomor antrian tidak boleh kosong")
    @NotNull(message = "Nomor antrian  tidak boleh kosong")
    private String runningNumber;
    private String image;
}
