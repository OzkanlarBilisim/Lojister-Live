package com.lojister.core.security;

import com.lojister.business.concretes.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    protected void doWork(HttpServletRequest req) {

        String header = req.getHeader(HEADER_STRING);

        String phone = null;
        String authToken = null;


            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                authToken = header.replace(TOKEN_PREFIX, "");
                try {
                    phone = jwtTokenUtil.getUsernameFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    log.error("an error occured during getting username from token", e);
                } catch (ExpiredJwtException e) {
                    // throw new com.lojister.exception.ExpiredJwtException("deneme hata");
                    log.warn("the token is expired and not valid anymore", e);
                } catch (SignatureException e) {
                    log.error("Authentication Failed. Username or Password not valid.");
                }
            } else {
                log.warn("couldn't find bearer string, will ignore the header");
            }
            if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {


                UserDetails userDetails = userDetailsService.loadUserByUsername(phone);


                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    log.info("authenticated user " + userDetails.getUsername() + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }



    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        if (!req.getRequestURI().equals("/api/V2/account/login")) {
            try {
                doWork(req);
            } catch (UsernameNotFoundException | SignatureException | MalformedJwtException | ExpiredJwtException e) {
                // res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Yetkisiz Giriş");
            }
        }

        chain.doFilter(req, res);
    }

}
