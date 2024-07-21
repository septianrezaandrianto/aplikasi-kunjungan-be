package com.skripsi.aplikasi_kunjungan_be.repositories;

import com.skripsi.aplikasi_kunjungan_be.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest,String> {

    @Query(value = "select * from guest where identitas_number = ?1 " +
            "and to_char(visit_date_start, 'yyyy-MM-dd HH:mm') = ?2 " +
            "and to_char(visit_date_end, 'yyyy-MM-dd HH:mm') = ?3 and is_deleted = false", nativeQuery = true)
    Guest getGuestIdentitasAndVisitDate(String identitasNumber, String visitDateStart, String visitDateEnd);
}
