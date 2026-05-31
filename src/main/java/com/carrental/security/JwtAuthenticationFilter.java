package com.carrental.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String path = request.getServletPath();

        return path.equals("/")
                || path.equals("/index.html")

                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")

                || path.equals("/favicon.ico")

                || path.startsWith("/api/auth/")

                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = getToken(request);

        if (
                token != null &&
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication() == null
        ) {

            try {

                String email =
                        jwtService.extractUsername(token);

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(email);

                if (
                        jwtService.isTokenValid(
                                token,
                                userDetails
                        )
                ) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }

            } catch (Exception e) {

                System.out.println(
                        "JWT Error: " + e.getMessage()
                );
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(
            HttpServletRequest request
    ) {

        String bearerToken =
                request.getHeader("Authorization");

        if (
                StringUtils.hasText(bearerToken)
                        &&
                        bearerToken.startsWith("Bearer ")
        ) {

            return bearerToken.substring(7);
        }

        return null;
    }
}