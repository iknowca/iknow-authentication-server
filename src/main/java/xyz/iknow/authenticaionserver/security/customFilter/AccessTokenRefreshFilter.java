package xyz.iknow.authenticaionserver.security.customFilter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.service.AccountService;
import xyz.iknow.authenticaionserver.security.jwt.exception.TokenException;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;
import xyz.iknow.authenticaionserver.utility.redis.token.token.RefreshToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenRefreshFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenService tokenService;
    @Setter
    private String url;

    public AccessTokenRefreshFilter(String url, JwtService jwtService, TokenService tokenService, AccountService accountService) {
        super();
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!url.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        for (int i = 0; i < request.getCookies().length; i++) {
            if (request.getCookies()[i].getName().equals("refreshToken")) {
                token = request.getCookies()[i].getValue();
                break;
            }
        }

        try {
            if (token == null) {
                throw new TokenException(TokenException.TOKEN_ERROR.NOT_FOUND_TOKEN);
            }
            Map jwtValueMap = jwtService.parseToken(token);
            Long accountId = Long.parseLong(jwtValueMap.get("accountId").toString());
            Optional<RefreshToken> maybeToken = tokenService.findRefreshTokenById(accountId);
            if (maybeToken.isEmpty()) {
                throw new TokenException(TokenException.TOKEN_ERROR.NOT_FOUND_TOKEN);
            }
            RefreshToken refreshToken = maybeToken.get();
            if (!refreshToken.getJwt().equals(token)) {
                throw new TokenException(TokenException.TOKEN_ERROR.INVALID_TOKEN);
            }

            String accessToken = jwtService.generateAccessToken(Account.builder()
                    .id(accountId)
                    .build());

            response.setStatus(200);
            response.setContentType("application/json");

            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String jsonStr = gson.toJson(Map.of("accessToken", "Bearer " + accessToken,
                    "status", "success"
            ));

            out.write(jsonStr);
            out.flush();
            out.close();
        } catch (TokenException e) {
            e.sendResponseError(response);
        }
    }
}
