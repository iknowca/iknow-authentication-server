package xyz.iknow.authenticaionserver.security.customFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.iknow.authenticaionserver.domain.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetailsService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Map<String, Object> jwtValueMap = jwtService.parseToken(token);
            String email = (String) jwtValueMap.get("email");
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }
}
