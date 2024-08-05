package com.skripsi.aplikasi_kunjungan_be.services;

import com.skripsi.aplikasi_kunjungan_be.dtos.AdminEditRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.AdminRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.LoginRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;

public interface AdminService {

    Response<?> createAdmin(AdminRequest adminRequest);
    Response<?> getAdminList();
    Response<?> login(LoginRequest loginRequest) throws Exception;
    Response<?> getPage(int pageNumber, int pageSize, String filter);
    Response<?> getById(String id);
    Response<?> deleteAdmin(String id);
    Response<?> updateAdmin(String id, AdminEditRequest adminEditRequest);
}
