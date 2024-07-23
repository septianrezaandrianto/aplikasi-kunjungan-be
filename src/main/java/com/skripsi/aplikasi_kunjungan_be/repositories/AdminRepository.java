package com.skripsi.aplikasi_kunjungan_be.repositories;

import com.skripsi.aplikasi_kunjungan_be.entities.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
