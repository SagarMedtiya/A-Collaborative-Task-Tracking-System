package com.airtribe.TaskTrackingAndManagement.security;

import com.airtribe.TaskTrackingAndManagement.util.jwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final com.airtribe.TaskTrackingAndManagement.util.jwtUtil jwtUtil;

    public TokenBlacklist(jwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //Add a token to the blacklist
    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }
    //check if a token is blacklisted
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    //remove the expired token from the blacklist
    @Scheduled(fixedRate = 60000)
    public void removeExpiredTokens() {
        blacklistedTokens.removeIf(token ->{
            try{
                Claims claims = jwtUtil.extractAllClaims(token);
                return claims.getExpiration().before(new Date());
            }
            catch(Exception e){
                return true;
            }
        });
    }
}
