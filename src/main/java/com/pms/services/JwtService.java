package com.pms.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.dto.UserDto;
import com.pms.models.User;
import com.pms.repo.UserRepository;
import com.pms.response.ResponseObject;
import com.pms.spel.IUserSpel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.pms.constants.Constants.*;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    private static final String SECRET_KEY = "566B59703373357638792F423F4528482B4D6251655468576D5A713474377739";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public ResponseObject getUserCredentials(String token) throws JsonProcessingException {
       try {
           String username =  extractUsername(token);
           Optional<IUserSpel> user =  userRepository.findUserByEmail(username);
           if(user.isEmpty()) {
               return new ResponseObject(ERROR_STATUS, USER_NOT_FOUND_MSG, null);
           }
//           String userString = mapper.writeValueAsString(user.get());
//           UserDto userDto = mapper.readValue(userString, UserDto.class);

           return new ResponseObject(SUCCESS_STATUS, null, user.get());
       }catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException(e.getMessage());
       }
    }
}
