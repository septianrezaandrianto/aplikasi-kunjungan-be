package com.skripsi.aplikasi_kunjungan_be.services;


import com.skripsi.aplikasi_kunjungan_be.constants.Constant;
import com.skripsi.aplikasi_kunjungan_be.dtos.AdminRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.entities.Admin;
import com.skripsi.aplikasi_kunjungan_be.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Response<?> createAdmin(AdminRequest adminRequest) {
        Admin existAdmin = adminRepository.getAdminByUsername(adminRequest.getUsername());
        if (Objects.nonNull(existAdmin)) {

        }

        Admin admin = Admin.builder()
                .username(adminRequest.getUsername())
                .password(adminRequest.getPassword())
                .phoneNumber(adminRequest.getPhoneNumber())
                .address(adminRequest.getAddress())
                .createdBy(adminRequest.getUsername())
                .createdDate(new Date())
                .isDeleted(false)
                .role(adminRequest.getRole())
                .fullName(adminRequest.getFullName())
                .build();
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(adminRepository.save(admin))
                .build();
    }

    @Override
    public Response<?> getAdminList() {
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(adminRepository.getAdminList())
                .build();
    }
}
