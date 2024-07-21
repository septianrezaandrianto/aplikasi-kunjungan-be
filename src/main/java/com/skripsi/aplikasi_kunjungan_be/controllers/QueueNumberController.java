package com.skripsi.aplikasi_kunjungan_be.controllers;

import com.skripsi.aplikasi_kunjungan_be.services.QueueNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queueNumber")
public class QueueNumberController {

    @Autowired
    private QueueNumberService queueNumberService;

    @GetMapping("/getRunningNumber")
    public ResponseEntity<?> getRunningNumber() {
        return ResponseEntity.ok(queueNumberService.getRunningNumber());
    }
}
