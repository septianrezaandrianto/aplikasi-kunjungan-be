package com.skripsi.aplikasi_kunjungan_be.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GeneralErrorException extends RuntimeException {

    public GeneralErrorException(String message) {
        super(message);
    }

    public GeneralErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
