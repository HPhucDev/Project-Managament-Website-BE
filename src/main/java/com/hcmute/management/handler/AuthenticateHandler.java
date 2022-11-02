package com.hcmute.management.handler;

import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class AuthenticateHandler {
    @Autowired
    JwtUtils jwtUtils;
    private final UserService userService;
    public UserEntity authenticateUser(HttpServletRequest req) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken) == true)
                throw new BadCredentialsException("access token is  expired");
                UserEntity user = userService.findById(jwtUtils.getUserNameFromJwtToken(accessToken));
                if (user == null) {
                    throw new BadCredentialsException("User not found");
                }
                return user;

        }else throw new BadCredentialsException("Access token is missing");
    }

}
