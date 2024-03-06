package xyz.iknow.authenticaionserver.security.customFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetailsService;
import xyz.iknow.authenticaionserver.security.jwt.exception.TokenException;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.jwt.JwtUtility;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (CorsUtils.isPreFlightRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (jwtUtility.isTokenCheckFilterExcludeUri(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");

        Map<String, Object> jwtValueMap;

        try {
            if (token == null) {
                throw new TokenException(TokenException.TOKEN_ERROR.NOT_FOUND_TOKEN);
            }
            if (!token.startsWith("Bearer ")) {
                throw new TokenException(TokenException.TOKEN_ERROR.INVALID_TOKEN);
            }
            jwtValueMap = jwtService.parseToken(token.substring(7));
        } catch (TokenException e) {
            e.sendResponseError(response);
            return;
        }

        Long accountId = Long.parseLong(jwtValueMap.get("accountId").toString());
        UserDetails userDetails = CustomUserDetails.builder()
                .username(accountId.toString())
                .password(token.substring(7))
                .build();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
