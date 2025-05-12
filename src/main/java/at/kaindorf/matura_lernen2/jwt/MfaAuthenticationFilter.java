package at.kaindorf.matura_lernen2.jwt;

import at.kaindorf.matura_lernen2.pojos.TokenType;
import at.kaindorf.matura_lernen2.services.JWTService;
import at.kaindorf.matura_lernen2.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MfaAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String mfaToken = request.getHeader("mfaToken");

        if (mfaToken == null  ) {
            if (request.getRequestURI().equals("/api/auth/otp-signin")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"This can only be accessed with a valid mfaToken");
                return;
            }
            filterChain.doFilter(request,response);
            return;
        }

        if (!request.getRequestURI().equals("/api/auth/otp-signin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Only Access with a valid JWT token");
            return;
        }

        String username = jwtService.extractUsername(mfaToken);
        if (username.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"No Username in the MFA token");
            return;
        }

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
        if (jwtService.isTokenValid(mfaToken,userDetails, TokenType.MFA)) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid MFA Token");
            return;
        }
        filterChain.doFilter(request,response);

    }
}
