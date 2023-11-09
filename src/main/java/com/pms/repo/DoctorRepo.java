package com.pms.repo;

import com.pms.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, String> {
    @Query(value = "SELECT * from doctor where active=true", nativeQuery = true)
    Optional<List<Doctor>> findAllActive();
}
