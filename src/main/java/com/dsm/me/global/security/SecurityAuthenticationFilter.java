package com.dsm.me.global.security;

import com.dsm.me.global.error.exceptions.token.TokenException;
import com.dsm.me.global.security.token.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Value("${auth.jwt.header}")
    private String header;
    @Value("${auth.jwt.type}")
    private String type;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null){
            try{
                jwtUtil.validateToken(token);
            }  catch (TokenException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(header);
        if(token!=null&&token.startsWith(type)){
            return token.substring(7);
        }
        return null;
    }
}
