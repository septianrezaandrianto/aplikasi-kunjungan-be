package com.skripsi.aplikasi_kunjungan_be.services;

import com.skripsi.aplikasi_kunjungan_be.constants.Constant;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.repositories.QueueNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class QueueNumberServiceImpl implements QueueNumberService {

    @Autowired
    private QueueNumberRepository queueNumberRepository;

    @Override
    public Response<?> getRunningNumber() {
        String dbDateFormater = Constant.DateFormatter.DB_DATE_FORMATTER.format(new Date());
        String runningNumber = queueNumberRepository.getRunningNumber(dbDateFormater);
        if (Objects.isNull(runningNumber)) {
            String paddedNumber = String.format("%010d", 1);
            runningNumber = Constant.PREFFIX_RUNNING_NUMBER.concat(paddedNumber);
        }
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(runningNumber)
                .build();
    }
}
