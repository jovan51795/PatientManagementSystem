package com.pms.repo;

import com.pms.models.Patient;
import com.pms.spel.IDataSets;
import com.pms.spel.IMonthsSpel;
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

    @Query("SELECT p.status as status, COUNT(p) as count FROM Patient p WHERE p.active = true GROUP By p.status ORDER BY count DESC")
    List<IPatientReportSpel> getPatientReport();

    @Query(value = "SELECT DISTINCT p.created_date as months FROM Patient p WHERE p.active = true AND p.created_date LIKE %:year%")
    List<IMonthsSpel> getChartReport(String year);

    @Query(value ="SELECT COUNT(p) as count FROM Patient p WHERE p.active = true GROUP By p.created_date ORDER BY p.created_date ASC")
    List<IDataSets> getChartReportDataSets();

    @Query(value ="SELECT COUNT(p) as count FROM Patient p WHERE p.active = true AND p.gender = :gender GROUP By p.created_date ORDER BY p.created_date ASC")
    List<IDataSets> getChartReportDataSetsByGender(String gender);


}
