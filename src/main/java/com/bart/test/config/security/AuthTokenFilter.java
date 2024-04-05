package com.bart.test.config.security;

import com.bart.test.service.UserService;
import com.bart.test.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Data
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String AUTH_TYPE  = "Bearer";
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private  JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(httpServletRequest);
            log.info("jwt string: " + jwt);
            if (jwt != null && jwtUtils.validateJwtToken(jwt,httpServletRequest)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.info("username: " + username);
                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String parseJwt(HttpServletRequest httpServletRequest) {
        String headerAuth = httpServletRequest.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AUTH_TYPE)) {
            return headerAuth.substring(AUTH_TYPE.length()+1);
        }
        return null;
    }
}
