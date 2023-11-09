package com.pms.dto;

public record DoctorDto(
        String id,
        String first_name,
        String last_name,
        String fullname,
        String specialization
) {
}
