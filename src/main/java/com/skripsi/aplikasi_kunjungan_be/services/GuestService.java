package com.skripsi.aplikasi_kunjungan_be.services;

import com.skripsi.aplikasi_kunjungan_be.dtos.GuestRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;

import java.text.ParseException;

public interface GuestService {

    Response<?> createGuest(GuestRequest guestRequest) throws ParseException;
    Response<?> doAction(String runningNumber, String action);
}
