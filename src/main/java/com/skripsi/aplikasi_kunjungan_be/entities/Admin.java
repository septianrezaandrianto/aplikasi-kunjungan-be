package com.skripsi.aplikasi_kunjungan_be.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

@Table(name="admin")
@Entity(name="Admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id", length = 100)
    private String id;
    @Column(length = 35)
    private String username;
    private String password;
    @Column(length = 25)
    private String phoneNumber;
    private String address;
    @Column(length = 35)
    private String role;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
    private Boolean isDeleted;
    @Column(length = 50)
    private String fullName;


}
