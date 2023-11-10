package com.pms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Byte[] file;
    private String prescriptions;
    @CreationTimestamp
    private ZonedDateTime date;

    @Column(length = 10000000)
    private String notes;

    @Column(length = 10000000)
    private String diagnose;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor physician;

    @ManyToOne
    @JoinColumn(name = "patient")
    @JsonIgnore
    private Patient patient;
}
