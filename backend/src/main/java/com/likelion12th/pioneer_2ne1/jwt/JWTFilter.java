package com.likelion12th.pioneer_2ne1.jwt;

import com.likelion12th.pioneer_2ne1.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization= request.getHeader("Authorization");


        try{
            if (authorization == null ) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {

                    System.out.println(cookie.getName());
                    if (cookie.getName().equals("Authorization")) {

                        authorization = cookie.getValue();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("JWTFilter cookies null error");
            System.out.println(e.getMessage());
        }





        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("JWTFilter: token null");
            filterChain.doFilter(request, response);

            return;
        }

        System.out.println("authorization now");
        String token = authorization.split(" ")[1];
        //토큰 소멸 시간 검증
        try {


            if (jwtUtil.isExpired(token)) {

                System.out.println("token expired");

                //            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "다시 로그인해주세요");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                filterChain.doFilter(request, response);

                return;
            } else {
                String username = jwtUtil.getUsername(token);
                System.out.println("JWT Token is valid. Username: " + username);
            }
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        Member userEntity = new Member();
        userEntity.setEmail(username);
        userEntity.setPassword("temppassword");
        userEntity.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        System.out.println("JWTFilter/ userEntity: " + userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
