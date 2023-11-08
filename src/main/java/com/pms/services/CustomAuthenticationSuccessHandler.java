package com.pms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

//	@Override
//	public void onAuthenticationSuccess1(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		ObjectMapper mapper = new ObjectMapper();
//		System.out.println(request.getHeader("Authorization"));
//
//	}

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.isAuthenticated()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("name", authentication.getName());
            userDetails.put("authorities", authentication.getAuthorities());
            userDetails.put("email", ( (OAuth2AuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("email"));
            userDetails.put("picture", ( (OAuth2AuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("picture"));
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(userDetails);
            response.setHeader("name", authentication.getName());
            response.setHeader("authorities", authentication.getAuthorities().toString());
            response.setHeader("email", ((OAuth2AuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("email"));
            response.setHeader("picture", ((OAuth2AuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("picture"));
            response.setHeader("Jovanie", "cabatuan");
            response.setHeader("Jovanie", "cabatuan");
            new ObjectMapper().writeValue(response.getOutputStream(), userDetails);
            out.print(json);
            out.flush();
            out.close();
        }
    }


}
