package com.skripsi.aplikasi_kunjungan_be.services;


import com.skripsi.aplikasi_kunjungan_be.constants.Constant;
import com.skripsi.aplikasi_kunjungan_be.dtos.*;
import com.skripsi.aplikasi_kunjungan_be.entities.Admin;
import com.skripsi.aplikasi_kunjungan_be.handler.DataExistException;
import com.skripsi.aplikasi_kunjungan_be.handler.NotFoundException;
import com.skripsi.aplikasi_kunjungan_be.repositories.AdminRepository;
import com.skripsi.aplikasi_kunjungan_be.securities.jwts.JwtUtils;
import com.skripsi.aplikasi_kunjungan_be.securities.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public Response<?> getPage(int pageNumber, int pageSize, String filter) {
        String filterMapping = mappingFilter(filter);
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        Page<Admin> adminPaging = null;
        if(Objects.isNull(filter) || "".equals(filter.trim())) {
            adminPaging = adminRepository.getPage(paging);
        } else {
            adminPaging = adminRepository.getPageWithFilter(filterMapping.toLowerCase(), paging);
        }

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(adminPaging.getContent())
                .totalPage(adminPaging.getTotalPages())
                .totalData(adminPaging.getTotalElements())
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


    @Override
    @Transactional
    public Response<?> createAdmin(AdminRequest adminRequest) {
        Admin existAdmin = adminRepository.getAdminByUsername(adminRequest.getUsername());
        if (Objects.nonNull(existAdmin)) {
            throw new DataExistException("Username " + Constant.Response.EXSIST_MESSAGE.replace("{value}",
                    adminRequest.getUsername()));
        }

        Admin admin = Admin.builder()
                .username(adminRequest.getUsername())
                .password(encoder.encode(adminRequest.getPassword()))
                .phoneNumber(adminRequest.getPhoneNumber())
                .address(adminRequest.getAddress())
                .createdBy(adminRequest.getUsername())
                .createdDate(new Date())
                .isDeleted(false)
                .role(adminRequest.getRole())
                .fullName(adminRequest.getFullName())
                .build();
        adminRepository.save(admin);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .build();
    }

    @Override
    public Response<?> getAdminList() {
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(adminRepository.getAdminList())
                .build();
    }

    @Override
    public Response<?> login(LoginRequest loginRequest) throws Exception {
        Admin admin = adminRepository.getAdminByUsername(loginRequest.getUsername());
        if (Objects.isNull(admin)) {
            throw new NotFoundException(Constant.Response.INVALID_LOGIN_MESSAGE);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        OAuth2Response oAuth2Response = OAuth2Response.builder()
                .access_token(jwt)
                .expires_in(jwtExpirationMs)
                .token_type("Bearer")
                .build();

        LoginResponse response = mappingLoginResponse(admin, oAuth2Response);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(response)
                .build();
    }

    @Override
    public Response<?> getById(String id) {
        Admin admin = adminRepository.getAdminById(id);
        if(Objects.isNull(admin)) {
            throw new NotFoundException(Constant.Response.DATA_NOT_FOUND_MESSAGE);
        }

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .data(admin)
                .build();
    }

    @Override
    @Transactional
    public Response<?> deleteAdmin(String id) {
        Admin admin = adminRepository.getAdminById(id);
        if(Objects.isNull(admin)) {
            throw new NotFoundException(Constant.Response.DATA_NOT_FOUND_MESSAGE);
        }
        adminRepository.deleteSoft(true, "SYSTEM", new Date(), id);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .build();
    }

    @Override
    @Transactional
    public Response<?> updateAdmin(String id, AdminEditRequest adminEditRequest) {
        Admin admin = adminRepository.getAdminById(id);
        if(Objects.isNull(admin)) {
            throw new NotFoundException(Constant.Response.DATA_NOT_FOUND_MESSAGE);
        }
        adminRepository.updateAdmin(adminEditRequest.getRole(), adminEditRequest.getPhoneNumber(), adminEditRequest.getAddress(),
                "SYSTEM", new Date(), id);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage(Constant.Response.SUCCESS_MESSAGE)
                .build();

    }

    private LoginResponse mappingLoginResponse(Admin admin, OAuth2Response oAuth2Response) {
        return LoginResponse.builder()
                .userId(admin.getId())
                .username(admin.getUsername())
                .accessToken(oAuth2Response.getAccess_token())
                .tokenType(oAuth2Response.getToken_type())
                .expiresIn(oAuth2Response.getExpires_in())
                .role(admin.getRole())
                .build();
    }
}
