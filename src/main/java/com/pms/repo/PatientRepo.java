package com.pms.repo;

import com.pms.models.Patient;
import com.pms.spel.IPatientReportSpel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PatientRepo extends JpaRepository<Patient, String> {

    @Query(value = "select * from patient where active = true", nativeQuery = true)
    Optional<List<Patient>> findActivePatient();

    @Query("SELECT p.status as status, COUNT(p) as count FROM Patient p GROUP By p.status ORDER BY count DESC")
    List<IPatientReportSpel> getPatientReport();
}
