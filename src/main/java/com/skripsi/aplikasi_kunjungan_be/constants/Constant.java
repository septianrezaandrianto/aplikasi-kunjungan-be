package com.skripsi.aplikasi_kunjungan_be.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

public interface Constant {

    static String PREFFIX_RUNNING_NUMBER = "PRT-";

    String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/cpl/doForgotPassword",
            "/user/register",
            "/user/login",
            "/user/doActiveUser",
            "/report/**",
            "/reportPenagihan/**",
            "/uploadData/downloadTemplate"
    };

    class DateFormatter {
        public static SimpleDateFormat DISPLAY_DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
        public static SimpleDateFormat FULL_DISPLAY_DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        public static SimpleDateFormat DB_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
        public static SimpleDateFormat YEAR_FORMATTER = new SimpleDateFormat("yyyy");
        public static SimpleDateFormat EXCEL_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        public static SimpleDateFormat FULL_DATE_FORMATTER = new SimpleDateFormat("dd MMMM yyyy",  new Locale("id", "ID"));
        public static SimpleDateFormat EXCEL_UPLOAD_DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
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

    class Extension {
        public static String PNG = "png";
        public static String JPG = "jpg";
        public static String JPEG = "jpeg";

        public static String SLASH = "/";
        public static String DOT = ".";
        public static String UNDERSCORE = "_";
        public static String XLS = "xls";
        public static String XLSX = "xlsx";
        public static String PDF = "pdf";
        public static String EMPTY_STRING = "";
    }

    class Unicode {
        public static String SOLIDUS = "%2F";
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