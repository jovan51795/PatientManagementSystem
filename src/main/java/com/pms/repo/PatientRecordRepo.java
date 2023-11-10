package com.pms.repo;

import com.pms.models.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRecordRepo extends JpaRepository<PatientRecord, Long> {
}
