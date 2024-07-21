package com.skripsi.aplikasi_kunjungan_be.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

public interface Constant {

    static String PREFFIX_RUNNING_NUMBER = "PM{NowDate}-";

    String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/guest/createGuest",
            "/guest/doAction",
            "/guest/doAction/**",
            "/queueNumber/getRunningNumber",
            "/admin/createAdmin",
            "/admin/login",
            "/admin/getAdminList",
            "/waGateway/sendMessage"
    };

    class DateFormatter {
        public static SimpleDateFormat RUNNING_NUMBER_DATE_FORMATTER = new SimpleDateFormat("ddMMyyyy");
        public static SimpleDateFormat DB_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
        public static SimpleDateFormat VISIT_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    class Role {
        public static String ADMIN = "ADMIN";
        public static String RECEPTIONIST = "RECEPTIONIST";
    }

    class Status {
        public static String APPROVE = "APPROVE";
        public static String REJECT = "REJECT";
        public static String WAITING_APPROVAL = "WAITING FOR APPROVAL";
    }

    class Credential {
        public static String AES_CIPHER = "AES";
        public static String KEY = "KmzWAy87Aa123456";
    }


    class Response {
        public static String SUCCESS_MESSAGE = "success";
        public static String NOT_FOUND_MESSAGE = "{value1} dengan ID : {value2} tidak ditemukan";
        public static String NOT_FOUND_USERNAME_MESSAGE = "{value1} dengan username : {value2} tidak ditemukan";
        public static String INVALID_LOGIN_MESSAGE = "Username / Password salah";
        public static String EXSIST_MESSAGE = "{value} sudah terdaftar";
        public static String EXSIST_IMAGE_MESSAGE = "Foto {value} sudah ada";
        public static String ACTION_NOT_VALID = "Action tidak valid";
        public static String CHANGE_PASSWORD_REQUEST_MESSAGE = "Akun anda masih menunggu persetujuan ubah password sebelumnya";
        public static String INACTIVE_USER_MESSAGE = "User anda belum aktif";
        public static String DATA_NOT_FOUND_MESSAGE = "Data tidak ditemukan";
        public static String UPLOAD_EXCEL_FILE_MESSAGE = "File yang diupload harus file excel";
        public static String UPLOAD_EXCEL_FILE_TEMPLATE_MESSAGE = "Template tidak valid";
    }

}