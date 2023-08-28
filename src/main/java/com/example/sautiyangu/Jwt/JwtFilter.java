package com.example.sautiyangu.Jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUserDetailsServvice customerUserDetailsServvice;

    Claims claims = null;
    private String userName = null;
    private ServletRequest httpServletRequest;
    private ServletResponse httpServletResponce;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponce, FilterChain filterChain) throws ServletException, IOException {

        if (httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")){
            filterChain.doFilter(httpServletRequest,httpServletResponce);
        }else{
            String authorizationHeder = httpServletRequest.getHeader("Authorization");
            String token = null;

            if (authorizationHeder != null && authorizationHeder.startsWith("Bearer ")){
                token = authorizationHeder.substring(7);
                userName = jwtUtil.extractUsername(token);
                claims = jwtUtil.extractionAllClaims(token);
            }
            if (userName != null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = customerUserDetailsServvice.loadUserByUsername(userName);

                if (jwtUtil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                            usernamePasswordAuthenticationToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                            );
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(httpServletRequest,httpServletResponce);
        }
    }
    public boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser(){
        return userName;
    }
}
