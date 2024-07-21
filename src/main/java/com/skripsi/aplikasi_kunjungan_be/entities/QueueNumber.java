package com.skripsi.aplikasi_kunjungan_be.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

@Table(name="queue_number")
@Entity(name="QueueNumber")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueNumber implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id", length = 100)
    private String id;
    @Column(length = 25)
    private String runningNumber;
    private Date createdDate;
}
