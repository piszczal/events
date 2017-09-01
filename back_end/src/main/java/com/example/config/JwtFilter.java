package com.example.config;

/**
 * Created by Szymon on 24.04.2017.
 */

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import com.example.service.EventService;
import com.example.service.EventServiceImpl;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {

    private EventService eventService = new EventServiceImpl();

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String authHeader = request.getHeader("authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        } else {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Missing or invalid Authorization header");
            }

            final String token = authHeader.substring(7);

            try {
                Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode("-e+n%bm21=4)-k^ynhde5-m2!vl%h%35e9@8d--o*0doh+9jy3")))
                        .parseClaimsJws(token);
            } catch (final SignatureException e) {
                try {
                    eventService.verifyUserToken(token);
                } catch (HttpStatusCodeException ex) {
                    throw new ServletException("Invalid token or no authorization");
                }
                throw new ServletException("Invalid token or no authorization");
            }

            chain.doFilter(req, res);
        }
    }
}