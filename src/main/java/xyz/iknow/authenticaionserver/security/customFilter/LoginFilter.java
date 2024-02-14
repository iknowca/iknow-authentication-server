package xyz.iknow.authenticaionserver.security.customFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    public LoginFilter(String loginUrl) {
        super(loginUrl);
    }
    private Map<String, Object> parseJsonMap(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        GsonJsonParser gsonJsonParser = new GsonJsonParser();
        return gsonJsonParser.parseMap(body);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(!HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        Map<String, Object> parseJsonMap = parseJsonMap(request);
        String username = (String) parseJsonMap.get("email");
        String password = (String) parseJsonMap.get("password");

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = super.getAuthenticationManager().authenticate(authRequest);
        return authentication;
    }
}