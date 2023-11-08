package com.pms.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final RestTemplate restTemplate;

    public CustomOAuth2UserService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // TODO Auto-generated method stub
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );
        Map<String, Object> userDetails = response.getBody();

        // Create a new map of user attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", userDetails.get("email"));
        attributes.put("name", userDetails.get("name"));

        System.out.println(userDetails.get("email") + "///////////////");
        System.out.println(userDetails.get("name") + "///////////////");
        System.out.println(userDetails.get("picture") + "///////////////");

        // Create a new OAuth2User with the user's attributes
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }


}

