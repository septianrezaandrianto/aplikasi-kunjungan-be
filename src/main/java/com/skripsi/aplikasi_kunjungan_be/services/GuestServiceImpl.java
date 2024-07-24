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
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private HttpServletResponse httpServletResponse;

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
    @Transactional
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

            String waMessage = null;
            if (Constant.Status.APPROVE.equals(action)) {
                waMessage = "Selemat!!!\n\n" +
                        "Permintaan anda dengan nomor antrian : " + runningNumber + " telah di APPROVE";
            } else {
                waMessage = "Maaf!!!\n\n" +
                        "Permintaan anda dengan nomor antrian : " + runningNumber + " di REJECT";
            }

            WaGatewayRequest waGatewayRequest = WaGatewayRequest.builder()
                    .countryCode("62")
                    .target(guest.getPhoneNumber())
                    .message(waMessage)
                    .build();

            log.info("waGatewayRequest : " + new Gson().toJson(waGatewayRequest));
            String responseRest = waGatewayRest.sendMessage(waGatewayRequest).block();
            log.info("responseRest : " + responseRest);

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


    public void generateXlsxReport(String date, String status) throws IOException {
        List<Guest> guestList = guestRepository.getGuestListByDateAndStatus(date, status);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report");

            // Create style for borders
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);

            // Add two header rows: Date and Status
            Row dateRow = sheet.createRow(0);
            dateRow.createCell(0).setCellValue("Tanggal: " + date);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // Merge all columns
            dateRow.getCell(0).setCellStyle(borderStyle); // Apply border style

            Row statusRow = sheet.createRow(1);
            statusRow.createCell(0).setCellValue("Status: " + status);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4)); // Merge all columns
            statusRow.getCell(0).setCellStyle(borderStyle); // Apply border style

            // Create a Row for header
            Row headerRow = sheet.createRow(2);
            headerRow.createCell(0).setCellValue("No");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Kantor");
            headerRow.createCell(3).setCellValue("Status");
            headerRow.createCell(4).setCellValue("Photo"); // Added column for photo
            for (int i = 0; i <= 4; i++) {
                headerRow.getCell(i).setCellStyle(borderStyle); // Apply border style
            }

            // Write guestList data to Excel
            int rowNum = 3;
            for (Guest guest : guestList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 3);
                row.createCell(1).setCellValue(guest.getFullName());
                row.createCell(2).setCellValue(guest.getOfficeName());
                row.createCell(3).setCellValue(guest.getStatus());

                // Handle image if present
                byte[] imageData = guest.getImage();
                if (imageData != null) {
                    try {
                        // Add image to the workbook
                        int pictureIdx = workbook.addPicture(imageData, Workbook.PICTURE_TYPE_JPEG);
                        CreationHelper helper = workbook.getCreationHelper();
                        Drawing<?> drawing = sheet.createDrawingPatriarch();
                        ClientAnchor anchor = helper.createClientAnchor();

                        // Set anchor position and size
                        anchor.setCol1(4); // Column for photo (adjust as needed)
                        anchor.setRow1(row.getRowNum());
                        Picture pict = drawing.createPicture(anchor, pictureIdx);
                        // Auto-size picture to fit cell
                        pict.resize();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i <= 4; i++) {
                    row.getCell(i).setCellStyle(borderStyle); // Apply border style
                }
            }

            // Add total row at the end
            Row totalRow = sheet.createRow(rowNum);
            Cell totalCell = totalRow.createCell(0);
            totalCell.setCellValue("Total:");
            totalCell.setCellStyle(borderStyle); // Apply border style

            sheet.addMergedRegion(new CellRangeAddress(
                    totalRow.getRowNum(), // from row
                    totalRow.getRowNum(), // to row
                    1, // from column
                    4 // to column
            ));

            totalRow.createCell(1).setCellValue(guestList.size());
            totalRow.getCell(1).setCellStyle(borderStyle);// Apply border style

            // Auto-size columns (optional)
            for (int i = 0; i <= 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // Set content type and header for the response
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"Report-Tamu-" + date + ".xlsx\"");

            // Write the workbook content to the servlet response's OutputStream
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            workbook.write(outputStream);

            // Flush the output stream
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
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
