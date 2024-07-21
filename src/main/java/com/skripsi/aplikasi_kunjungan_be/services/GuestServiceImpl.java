package com.skripsi.aplikasi_kunjungan_be.services;

import com.skripsi.aplikasi_kunjungan_be.dtos.GuestRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public Response<?> createGuest(GuestRequest guestRequest) {
        return null;
    }
}
