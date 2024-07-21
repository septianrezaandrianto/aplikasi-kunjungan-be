package com.skripsi.aplikasi_kunjungan_be.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRequest {

    private String visitorIdNumber;
    private String identitasNumber;
    private String phoneNumber;
    private String fullName;
    private String officeName;
    private String email;
    private String visitDateStart;
    private String visitDateEnd;
    private String note;
    private String adminId;
    private String runningNumber;
    private byte[] image;
}
