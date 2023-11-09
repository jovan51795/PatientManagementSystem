package com.pms.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "doctor_id")
    @GenericGenerator(
            name = "doctor_id",
            strategy = "com.pms.models.CustomIdGenerator"
    )
    @Column(name = "doctor_id")
    private String id;

    private String first_name;
    private String last_name;
    private String fullname;
    private String specialization;
    private Boolean active = true;
}
