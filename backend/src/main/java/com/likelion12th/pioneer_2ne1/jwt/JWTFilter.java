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
        String requestUri = request.getRequestURI();

        // 특정 URI는 필터링하지 않고 그대로 진행
        if (requestUri.matches("^\\/api\\/members\\/login(?:\\/.*)?$") || requestUri.matches("^\\/api\\/members\\/join(?:\\/.*)?$") ||requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        // Authorization 헤더가 없으면 쿠키에서 JWT를 찾기
        if (authorization == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        authorization = "Bearer " + cookie.getValue();
                        break;
                    }
                }
            }
        }

        // Authorization 헤더 또는 쿠키에서 Bearer 토큰이 없는 경우
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("JWTFilter: token null or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 토큰에서 JWT 추출
        String token = authorization.split(" ")[1];
        try {
            // 토큰 유효성 검사
            if (jwtUtil.isExpired(token)) {
                System.out.println("Token expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 유효한 토큰인 경우 사용자 정보를 추출
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            Member userEntity = new Member();
            userEntity.setEmail(username);
            userEntity.setPassword("temppassword"); // 비밀번호는 실제로는 설정하지 않아야 함
            userEntity.setRole(role);

            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("JWTFilter: Token is valid. Username: " + username);
        } catch (Exception e) {
            System.out.println("JWTFilter: Error processing token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(e.getMessage());
            System.out.println("JWTFilter: Error processing token");
            return;
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}

//
//public class JWTFilter extends OncePerRequestFilter {
//
//    private final JWTUtil jwtUtil;
//
//
//    public JWTFilter(JWTUtil jwtUtil) {
//
//        this.jwtUtil = jwtUtil;
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//
//        String requestUri = request.getRequestURI();
//
//        if (requestUri.matches("^\\/members\\/login(?:\\/.*)?$")) {
//            System.out.println("requestUri.matches(\"^\\\\/members\\\\/login(?:\\\\/.*)?$\")");
//            filterChain.doFilter(request, response);
//            return;
//        }
//        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//
//        String authorization= request.getHeader("Authorization");
//
//
////        try{
////            if (authorization == null ) {
////                Cookie[] cookies = request.getCookies();
////                for (Cookie cookie : cookies) {
////
////                    System.out.println(cookie.getName());
////                    if (cookie.getName().equals("Authorization")) {
////
////                        authorization = cookie.getValue();
////                    }
////                }
////            }
////        } catch (Exception e) {
////            System.out.println("JWTFilter cookies null error");
////            System.out.println("JWTFilter cookies error: " + e.getMessage());
////        }
//        if (authorization == null ) {
//            Cookie[] cookies = request.getCookies();
//            for (Cookie cookie : cookies) {
//
//                System.out.println(cookie.getName());
//                if (cookie.getName().equals("Authorization")) {
//
//                    authorization = cookie.getValue();
//                    System.out.println(authorization);
//                }
//            }
//        }
//
//
//
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//
//            System.out.println("JWTFilter: token null");
//            filterChain.doFilter(request, response);
//
//            return;
//        }
//
//        System.out.println("authorization now");
//        String token = authorization.split(" ")[1];
//        //토큰 소멸 시간 검증
//        try {
//
//
//            if (jwtUtil.isExpired(token)) {
//
//                System.out.println("token expired");
//
//                //            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "다시 로그인해주세요");
//
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                filterChain.doFilter(request, response);
//
//                return;
//            } else {
//                String username = jwtUtil.getUsername(token);
//                System.out.println("JWT Token is valid. Username: " + username);
//            }
//        } catch (Exception e) {
//
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            System.out.println(e.getMessage());
//            return;
//        }
//
//
//        String username = jwtUtil.getUsername(token);
//        String role = jwtUtil.getRole(token);
//
//        Member userEntity = new Member();
//        userEntity.setEmail(username);
//        userEntity.setPassword("temppassword");
//        userEntity.setRole(role);
//
//        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
//        System.out.println("JWTFilter/ userEntity: " + userEntity);
//
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//    }
//}
