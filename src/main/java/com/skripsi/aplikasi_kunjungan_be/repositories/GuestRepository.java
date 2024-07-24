package com.skripsi.aplikasi_kunjungan_be.repositories;

import com.skripsi.aplikasi_kunjungan_be.entities.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest,String> {

    @Query(value = "select * from guest where identitas_number = ?1 " +
            "and to_char(visit_date_start, 'yyyy-MM-dd HH:mm') = ?2 " +
            "and to_char(visit_date_end, 'yyyy-MM-dd HH:mm') = ?3 and is_deleted = false", nativeQuery = true)
    Guest getGuestIdentitasAndVisitDate(String identitasNumber, String visitDateStart, String visitDateEnd);

    @Query(value = "select * from guest where identitas_number = ?1 " +
            "and status = ?2 " +
            "and is_deleted = false", nativeQuery = true)
    Guest getGuestByIdentitasAndStatus(String identitasNumber, String status);

    @Query(value = "select * from guest where running_number = ?1 and is_deleted = false", nativeQuery = true)
    Guest getGuestByRunningNumber(String runningNumber);

    @Modifying
    @Transactional
    @Query(value = "update guest set modified_by = ?1, modified_date =?2, status = ?3 WHERE running_number = ?4 and is_deleted = false", nativeQuery = true)
    void updateStatus(String modifiedBy, Date modifiedDate, String status, String runningNumber);

    @Query(value = "select * from guest where is_deleted = false order by running_number DESC", nativeQuery = true)
    Page<Guest> getPage(Pageable pageable);

    @Query(value = "select * from guest where lower(full_name) like ?1 and is_deleted = false order by running_number DESC", nativeQuery = true)
    Page<Guest> getPageWithFilter(String fullName, Pageable pageable);

    @Query(value = "select * from guest where to_char(created_date, 'yyyy-MM-dd') = ?1 and status =?2 order by created_date desc", nativeQuery = true)
    List<Guest> getGuestListByDateAndStatus(String date, String status);

}
