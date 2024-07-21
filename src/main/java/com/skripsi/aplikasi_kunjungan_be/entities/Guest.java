package com.skripsi.aplikasi_kunjungan_be.entities;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.skripsi.aplikasi_kunjungan_be.services.ByteArrayBase64Deserializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Date;

@Table(name="guest")
@Entity(name="Guest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id", length = 100)
    private String id;
    @Column(length = 35)
    private String visitorIdNumber;
    @Column(length = 35)
    private String identitasNumber;
    @Column(length = 25)
    private String phoneNumber;
    @Column(length = 35)
    private String fullName;
    @Column(length = 50)
    private String officeName;
    @Column(length = 100)
    private String email;
    private Date visitDateStart;
    private Date visitDateEnd;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
    private Admin admin;
    @Column(length = 25)
    private String status;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
    private Boolean isDeleted;
    @Column(length = 25)
    private String runningNumber;
    @JsonDeserialize(using = ByteArrayBase64Deserializer.class)
    private byte[] image;

}
