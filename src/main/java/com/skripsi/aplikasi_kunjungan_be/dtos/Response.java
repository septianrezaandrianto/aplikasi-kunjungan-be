package com.skripsi.aplikasi_kunjungan_be.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private int statusCode;
    private String statusMessage;
    private T data;
    private List<String> errorList;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPage;
    private Long totalData;

}
