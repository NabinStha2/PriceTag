package com.example.pricetag.controllers;

import com.example.pricetag.config.JwtAuthenticationToken;
import com.example.pricetag.responses.AuthResponseDto;
import com.example.pricetag.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/jwt")
public class JwtController {

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret:mySecretKey}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateJwt(@RequestParam String email, @RequestParam List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        String token = Jwts.builder()
                .setSubject(email)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(getSigningKey())
                .compact();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateJwt(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("subject", claims.getSubject());
            response.put("roles", claims.get("roles"));
            response.put("expiration", claims.getExpiration());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<Map<String, String>> protectedEndpoint(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a protected endpoint");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities().toString());
        
        return ResponseEntity.ok(response);
    }
}
