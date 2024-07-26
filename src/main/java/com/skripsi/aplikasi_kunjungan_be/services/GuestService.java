package com.skripsi.aplikasi_kunjungan_be.services;

import com.skripsi.aplikasi_kunjungan_be.dtos.GuestRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;

import java.io.IOException;
import java.text.ParseException;

public interface GuestService {

    Response<?> createGuest(GuestRequest guestRequest) throws ParseException;
    Response<?> doAction(String runningNumber, String action);
    Response<?> getPage(int pageNumber, int pageSize, String filter);
    void generateXlsxReport(String date, String status) throws IOException;
    Response<?> countTotalGuest(String date, String status);
}
