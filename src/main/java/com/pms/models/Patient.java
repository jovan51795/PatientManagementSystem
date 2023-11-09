package com.pms.models;

import com.pms.models.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "patient_id")
    @GenericGenerator(
            name = "patient_id",
            strategy = "com.pms.models.CustomIdGenerator"
    )
    @Column(name = "patient_id")
    private String id;

    private String first_name;
    private String middle_name;
    private String last_name;
    private String birthday;
    private String place_of_birth;
    private String gender;
    private String contact;
    private String emergency_contact;

    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_records")
    private List<PatientRecord> patientRecords;

}
