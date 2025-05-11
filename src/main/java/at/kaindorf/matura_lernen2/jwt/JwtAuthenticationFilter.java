package at.kaindorf.matura_lernen2.jwt;

import at.kaindorf.matura_lernen2.services.JWTService;
import at.kaindorf.matura_lernen2.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            log.info("Auth or unauthorized");
            filterChain.doFilter(request,response);
            return;
        }
        jwtToken = jwtToken.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        if (!username.isBlank()) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
            if (jwtService.isTokenValid(jwtToken,userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
            else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid Jwt Token");
                return;
            }
        }

        filterChain.doFilter(request,response);
    }
}
