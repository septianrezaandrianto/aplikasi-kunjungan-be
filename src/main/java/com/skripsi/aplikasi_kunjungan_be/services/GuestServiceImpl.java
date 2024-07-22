package com.skripsi.aplikasi_kunjungan_be.services;

import com.google.gson.Gson;
import com.skripsi.aplikasi_kunjungan_be.constants.Constant;
import com.skripsi.aplikasi_kunjungan_be.dtos.GuestRequest;
import com.skripsi.aplikasi_kunjungan_be.dtos.Response;
import com.skripsi.aplikasi_kunjungan_be.dtos.WaGatewayRequest;
import com.skripsi.aplikasi_kunjungan_be.entities.Admin;
import com.skripsi.aplikasi_kunjungan_be.entities.Guest;
import com.skripsi.aplikasi_kunjungan_be.entities.QueueNumber;
import com.skripsi.aplikasi_kunjungan_be.handler.DataExistException;
import com.skripsi.aplikasi_kunjungan_be.handler.NotFoundException;
import com.skripsi.aplikasi_kunjungan_be.repositories.AdminRepository;
import com.skripsi.aplikasi_kunjungan_be.repositories.GuestRepository;
import com.skripsi.aplikasi_kunjungan_be.repositories.QueueNumberRepository;
import com.skripsi.aplikasi_kunjungan_be.rest.WaGatewayRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private QueueNumberRepository queueNumberRepository;
    @Autowired
    private WaGatewayRest waGatewayRest;

    @Value("${base.url}")
    private String baseUrl;

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

        String approveLink = baseUrl + "/guest/doAction/" + guest.getRunningNumber() + "/APPROVE";
        String rejectLink = baseUrl + "/guest/doAction/" + guest.getRunningNumber() + "/REJECT";
        String waMessage = "Ada Pesan Masuk untuk Anda dari : " +
                "\n\nNama Lengkap: " + guest.getFullName() +
                "\nNo HP : " + guest.getPhoneNumber() +
                "\nNo Antrian : " + guest.getRunningNumber() +
                "\nKantor : " + guest.getOfficeName() +
                "\nWaktu Kunjungan : " + guestRequest.getVisitDateStart() +  " - " + guestRequest.getVisitDateEnd() +
                "\nKeperluan Kunjungan : " + guestRequest.getNote() +
                ",\n\nKlik untuk Approve : " + approveLink +
                "\n\nKlik untuk Reject : " + rejectLink;
        WaGatewayRequest waGatewayRequest = WaGatewayRequest.builder()
                .countryCode("62")
                .target(admin.getPhoneNumber())
                .message(waMessage)
                .build();

        log.info("waGatewayRequest : " + new Gson().toJson(waGatewayRequest));
        String responseRest = waGatewayRest.sendMessage(waGatewayRequest).block();
        log.info("responseRest : " + responseRest);
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

        String message = Constant.Response.SUCCESS_MESSAGE;
        if (!Constant.Status.WAITING_APPROVAL.equals(guest.getStatus())) {
            message = "Status anda sudah pernah di " + guest.getStatus();
        }
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(message)
                .build();
    }

    @Override
    public Response<?> getPage(int pageNumber, int pageSize, String filter) {
        String filterMapping = mappingFilter(filter);
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        Page<Guest> guestPaging = null;
        if(Objects.isNull(filter) || "".equals(filter.trim())) {
            guestPaging = guestRepository.getPage(paging);
        } else {
            guestPaging = guestRepository.getPageWithFilter(filterMapping.toLowerCase(), paging);
        }

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(guestPaging.getContent())
                .totalPage(guestPaging.getTotalPages())
                .totalData(guestPaging.getTotalElements())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
    }

    private String mappingFilter(String filter) {
        if(Objects.isNull(filter) || "".equals(filter.trim())) {
            return "%%";
        } else {
            return "%".concat(filter.toLowerCase().trim()).concat("%");
        }
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
