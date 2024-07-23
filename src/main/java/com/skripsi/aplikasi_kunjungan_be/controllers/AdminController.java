package com.skripsi.aplikasi_kunjungan_be.controllers;

import com.skripsi.aplikasi_kunjungan_be.dtos.AdminRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.LoginRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/createAdmin")
    ResponseEntity<?> createAdmin(@Valid @RequestBody AdminRequest adminRequest) {
        return ResponseEntity.ok(adminService.createAdmin(adminRequest));
    }

    @GetMapping("/getAdminList")
    ResponseEntity<?> getAdminList() {
        return ResponseEntity.ok(adminService.getAdminList());
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Response<?>> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseEntity.ok(adminService.login(loginRequest));
    }

    @GetMapping(value = "/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "pageNumber")int pageNumber,
                                     @RequestParam(value = "pageSize")int pageSize,
                                     @RequestParam(value = "filter", defaultValue = "")String filter) {
        return ResponseEntity.ok(adminService.getPage(pageNumber, pageSize, filter));
    }
}
