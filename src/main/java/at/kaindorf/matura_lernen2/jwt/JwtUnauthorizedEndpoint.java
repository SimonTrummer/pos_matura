package at.kaindorf.matura_lernen2.jwt;

import at.kaindorf.matura_lernen2.exceptions.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUnauthorizedEndpoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        StringBuffer path = request.getRequestURL();
        String error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        String message = authException.getMessage();
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (authException instanceof UserAlreadyExistsException) {
            error = HttpStatus.CONFLICT.getReasonPhrase();
            status = HttpServletResponse.SC_CONFLICT;
        }
        if (authException instanceof BadCredentialsException || authException instanceof UsernameNotFoundException || authException instanceof InsufficientAuthenticationException) {
            error = HttpStatus.FORBIDDEN.getReasonPhrase();
            status = HttpServletResponse.SC_FORBIDDEN;
        }

        if (authException instanceof AuthenticationServiceException) {
            error = HttpStatus.FORBIDDEN.getReasonPhrase();
            status = HttpServletResponse.SC_FORBIDDEN;
        }
        Map<String,Object> responseBody = new HashMap<>();

        responseBody.put("path", path);
        responseBody.put("error", error);
        responseBody.put("message", message);
        responseBody.put("status", status);

        ObjectMapper om = new ObjectMapper();
        response.setStatus(status);
        om.writeValue(response.getWriter(),responseBody);
    }
}
