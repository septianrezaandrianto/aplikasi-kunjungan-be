package com.skripsi.aplikasi_kunjungan_be.services;

import com.skripsi.aplikasi_kunjungan_be.constants.Constant;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.entities.QueueNumber;
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
        String runningNumber = null;
        Date nowDate = new Date();
        String dbDateFormater = Constant.DateFormatter.DB_DATE_FORMATTER.format(nowDate);
        QueueNumber queueNumber = queueNumberRepository.getRunningNumber(dbDateFormater);
        String prefix = Constant.PREFFIX_RUNNING_NUMBER.replace("{NowDate}",
                Constant.DateFormatter.RUNNING_NUMBER_DATE_FORMATTER.format(nowDate));
        if (Objects.isNull(queueNumber)) {
            String paddedNumber = String.format("%07d", 1);
            runningNumber = prefix.concat(paddedNumber);
            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                    .data(runningNumber)
                    .build();
        }

        int createNewNumber = Integer.parseInt(queueNumber.getRunningNumber().split("-")[1]);
        String paddedNumber = String.format("%07d", createNewNumber + 1);
        runningNumber = prefix.concat(paddedNumber);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(runningNumber)
                .build();
    }
}
