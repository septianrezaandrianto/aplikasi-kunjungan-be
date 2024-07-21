package com.skripsi.aplikasi_kunjungan_be.services;

import com.skripsi.aplikasi_kunjungan_be.constants.Constant;
import com.skripsi.aplikasi_kunjungan_be.dtos.GuestRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.entities.Admin;
import com.skripsi.aplikasi_kunjungan_be.entities.Guest;
import com.skripsi.aplikasi_kunjungan_be.entities.QueueNumber;
import com.skripsi.aplikasi_kunjungan_be.handler.DataExistException;
import com.skripsi.aplikasi_kunjungan_be.handler.NotFoundException;
import com.skripsi.aplikasi_kunjungan_be.repositories.AdminRepository;
import com.skripsi.aplikasi_kunjungan_be.repositories.GuestRepository;
import com.skripsi.aplikasi_kunjungan_be.repositories.QueueNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private QueueNumberRepository queueNumberRepository;

    @Override
    @Transactional
    public Response<?> createGuest(GuestRequest guestRequest) throws ParseException {
        Date nowDate = new Date();
        Guest existGuest = guestRepository.getGuestByIdentitasAndStatus(guestRequest.getIdentitasNumber(),
                Constant.Status.WAITING_APPROVAL);

        if (Objects.nonNull(existGuest)) {
            throw new DataExistException("Nomor Identitas " + Constant.Response.EXSIST_MESSAGE.replace("{value}",
                    existGuest.getIdentitasNumber()) + " dan sedang menunggu persetujuan");
        }
        Admin admin = adminRepository.getAdminById(guestRequest.getAdminId());
        if (Objects.isNull(admin)) {
            throw new NotFoundException(Constant.Response.NOT_FOUND_MESSAGE.replace("{value1}", "Admin")
                    .replace("{value2}", guestRequest.getAdminId()));
        }

        QueueNumber queueNumber = QueueNumber.builder()
                .runningNumber(guestRequest.getRunningNumber())
                .createdDate(nowDate)
                .build();
        queueNumberRepository.save(queueNumber);

        byte[] image = null;
        if (Objects.nonNull(guestRequest.getImage()) && !guestRequest.getImage().equals("")) {
            image = decodeBase64String(guestRequest.getImage());
        }

        Guest guest = Guest.builder()
                .visitorIdNumber(guestRequest.getVisitorIdNumber())
                .identitasNumber(guestRequest.getIdentitasNumber())
                .phoneNumber(guestRequest.getPhoneNumber())
                .fullName(guestRequest.getFullName())
                .officeName(guestRequest.getOfficeName())
                .email(guestRequest.getEmail())
                .visitDateStart(Constant.DateFormatter.VISIT_DATE_FORMATTER.parse(guestRequest.getVisitDateStart()))
                .visitDateEnd(Constant.DateFormatter.VISIT_DATE_FORMATTER.parse(guestRequest.getVisitDateEnd()))
                .note(guestRequest.getNote())
                .admin(admin)
                .status(Constant.Status.WAITING_APPROVAL)
                .runningNumber(guestRequest.getRunningNumber())
                .image(image)
                .isDeleted(false)
                .createdBy(guestRequest.getFullName())
                .createdDate(nowDate)
                .build();
        guestRepository.save(guest);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .build();
    }

    @Override
    public Response<?> doAction(String runningNumber, String action) {
        Guest guest = guestRepository.getGuestByRunningNumber(runningNumber);
        if (Objects.isNull(guest)) {
            throw new NotFoundException(Constant.Response.NOT_FOUND_MESSAGE.replace("{value1}", "Admin")
                    .replace("{value2}", runningNumber));
        }
        String status = null;
        if (Objects.nonNull(action) && Constant.Status.APPROVE.equals(action)) {
            status = Constant.Status.APPROVE;
        } else if (Objects.nonNull(action) && Constant.Status.REJECT.equals(action)) {
            status = Constant.Status.REJECT;
        }

        if (Objects.nonNull(status) && Constant.Status.WAITING_APPROVAL.equals(guest.getStatus())) {
            guestRepository.updateStatus("SYSTEM", new Date(), status, guest.getRunningNumber());
        }
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .build();
    }

    private byte[] decodeBase64String(String base64String) {
        try {
            if (base64String == null || !base64String.contains(",")) {
                throw new IllegalArgumentException("Invalid base64 string format");
            }
            String base64Data = base64String.split(",")[1];
            return Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Failed to decode base64 string", e);
        }
    }
}
