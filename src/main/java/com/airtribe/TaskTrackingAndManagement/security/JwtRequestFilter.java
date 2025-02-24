package com.airtribe.TaskTrackingAndManagement.security;

import com.airtribe.TaskTrackingAndManagement.util.jwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private jwtUtil JwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain Chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            username = JwtUtil.extractUsername(jwt);
        }
        if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            //check if the token is blacklisted
            if(tokenBlacklist.isBlacklisted(jwt)){
                throw new RuntimeException("Token is invalidated");
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(JwtUtil.validateToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
       }
        Chain.doFilter(request, response);
    }
}
