package com.skripsi.aplikasi_kunjungan_be.controllers;

import com.skripsi.aplikasi_kunjungan_be.dtos.GuestRequest;
import com.skripsi.aplikasi_kunjungan_be.services.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequestMapping("/guest")
@RestController
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/createGuest")
    public ResponseEntity<?> createGuest(@Valid @RequestBody GuestRequest guestRequest) throws ParseException {
        return ResponseEntity.ok(guestService.createGuest(guestRequest));
    }
}
