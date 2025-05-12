package at.kaindorf.matura_lernen2.jwt;

import at.kaindorf.matura_lernen2.pojos.TokenType;
import at.kaindorf.matura_lernen2.services.JWTService;
import at.kaindorf.matura_lernen2.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        jwtToken = jwtToken.substring(7);

        String username = jwtService.extractUsername(jwtToken);

        if (username == null || username.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Your jwt token was invalid");
            return;
        }
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

        if (userDetails == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"The username in your jwt token is not inside our db");
            return;
        }

        if (jwtService.isTokenValid(jwtToken,userDetails,TokenType.JWT)) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Your jwt token was invalid");
            return;
        }

        filterChain.doFilter(request,response);
    }
}
