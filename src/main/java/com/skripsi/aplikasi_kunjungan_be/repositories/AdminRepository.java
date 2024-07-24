package com.skripsi.aplikasi_kunjungan_be.repositories;

import com.skripsi.aplikasi_kunjungan_be.entities.Admin;
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
public interface AdminRepository extends JpaRepository<Admin, String> {

    @Query(value = "select * from admin where username = ?1 and is_deleted = false", nativeQuery = true)
    Admin getAdminByUsername(String username);

    @Query(value= "select * from admin where is_deleted = false order by full_name ASC", nativeQuery = true)
    List<Admin> getAdminList();

    @Query(value = "select * from admin where id = ?1 and is_deleted = false", nativeQuery = true)
    Admin getAdminById(String adminId);

    @Query(value = "select * from admin where is_deleted = false order by created_date DESC", nativeQuery = true)
    Page<Admin> getPage(Pageable pageable);

    @Query(value = "select * from admin where lower(full_name) like ?1 and is_deleted = false order by created_date DESC", nativeQuery = true)
    Page<Admin> getPageWithFilter(String fullName, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update admin set is_deleted = ?1, modified_by = ?2, modified_date = ?3 where id = ?4", nativeQuery = true)
    void deleteSoft(boolean isDeleted, String username, Date date, String id);

    @Modifying
    @Transactional
    @Query(value = "update admin set role = ?1, phone_number = ?2, address = ?3, modified_by = ?4, modified_date = ?5 where id = ?6", nativeQuery = true)
    void updateAdmin(String role, String phoneNumber, String address, String username, Date date, String id);
}
