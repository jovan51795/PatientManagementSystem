package com.pms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.response.ResponseObject;
import com.pms.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pms.constants.Constants.ERROR_STATUS;
import static com.pms.constants.Constants.UNAUTHORIZED;

@RestController
@RequestMapping("/api/v1/private")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final JwtService jwtService;
    private final ObjectMapper mapper;
    @GetMapping("/details")
    public ResponseObject getUserDetails(HttpServletRequest request) throws JsonProcessingException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtService.getUserCredentials(token);
        }

        return new ResponseObject(ERROR_STATUS, UNAUTHORIZED, null);
    }
}
