package com.example.pricetag.config;

import com.example.pricetag.services.AuthService;
import com.example.pricetag.services.JwtService;
import com.example.pricetag.utils.ColorLogger;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;
        ColorLogger.logInfo("authHeader :: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            try {
                userName = jwtService.extractUserName(token);
            } catch (ExpiredJwtException e) {
                ColorLogger.logError(e.toString());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Token Expired");
                return;
            }
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            ColorLogger.logInfo("I am inside JwtFilter :: " + userName);
            UserDetails userDetails = authService.loadUserByUsername(userName);
            if (jwtService.validateToken(token, userDetails)) {
                ColorLogger.logInfo("I am inside JwtFilter validateToken :: " + userDetails);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }

}