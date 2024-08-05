package com.skripsi.aplikasi_kunjungan_be.controllers;

import com.skripsi.aplikasi_kunjungan_be.constants.Constant;
import com.skripsi.aplikasi_kunjungan_be.dtos.GuestRequest;
import com.skripsi.aplikasi_kunjungan_be.services.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RequestMapping("/guest")
@RestController
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/createGuest")
    public ResponseEntity<?> createGuest(@Valid @RequestBody GuestRequest guestRequest) throws ParseException, InterruptedException {
        return ResponseEntity.ok(guestService.createGuest(guestRequest));
    }

    @GetMapping("/doAction/{runningNumber}/{action}")
    public ResponseEntity<?> doAction(@PathVariable("runningNumber") String runningNumber,
                                      @PathVariable("action") String action) {
        return ResponseEntity.ok(guestService.doAction(runningNumber, action));
    }

    @GetMapping(value = "/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "pageNumber")int pageNumber,
                                                       @RequestParam(value = "pageSize")int pageSize,
                                                       @RequestParam(value = "filter", defaultValue = "")String filter) {
        return ResponseEntity.ok(guestService.getPage(pageNumber, pageSize, filter));
    }

    @GetMapping(value = "/generateXlsxReport/{date}/{status}")
    public ResponseEntity<?> generateXlsxReport(@PathVariable("date")String date, @PathVariable("status")String status) throws IOException {
        guestService.generateXlsxReport(date,status);
        return ResponseEntity.ok(Constant.Response.SUCCESS_MESSAGE);
    }

}
