package com.pms.controllers;

import com.pms.dto.DoctorDto;
import com.pms.response.ResponseObject;
import com.pms.services.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/doctor")
public class DoctorController {
    private final IDoctorService doctorService;
    @PostMapping()
    public ResponseObject save(@RequestBody DoctorDto doctorDto) {
        return  doctorService.save(doctorDto);
    }

    @GetMapping()
    public ResponseObject getAllDoctors() {
        return doctorService.getAllDoctor();
    }

    @DeleteMapping("/{id}")
    public ResponseObject delete(@PathVariable("id") String id) {
        return doctorService.delete(id);
    }
    @PatchMapping
    public ResponseObject update(@RequestBody DoctorDto doctorDto) {
        return doctorService.update(doctorDto);
    }
}
