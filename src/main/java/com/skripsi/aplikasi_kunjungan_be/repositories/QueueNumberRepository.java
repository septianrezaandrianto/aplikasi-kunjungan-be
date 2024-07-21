package com.skripsi.aplikasi_kunjungan_be.repositories;

import com.skripsi.aplikasi_kunjungan_be.entities.QueueNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueNumberRepository extends JpaRepository<QueueNumber, String> {

    @Query(value = "select * from queue_number where to_char(created_date, 'yyyy-MM-dd') = ?1 order by created_date DESC limit 1", nativeQuery = true)
    QueueNumber getRunningNumber(String currentDate);
}
