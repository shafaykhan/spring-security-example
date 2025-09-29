package com.shafay.SpringSecurity.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shafay.SpringSecurity.security.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  public AuthenticationFilter(@Lazy AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String username = request.getHeader("X-Auth-Username");
    final String password = request.getHeader("X-Auth-Password");
    final String token = request.getHeader("X-Auth-Token");

    try {
      if (username != null && password != null) {
        processUsernamePasswordAuthentication(response, username, password);
        return;

      } else if (token != null) {
        String tokenWithoutBearer = token.substring(7);
        processTokenAuthentication(tokenWithoutBearer);

      }

      filterChain.doFilter(request, response);

    } catch (AuthenticationException exception) {
      SecurityContextHolder.clearContext();

      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write(new ObjectMapper()
          .writeValueAsString(new ExceptionResponse(HttpStatus.UNAUTHORIZED, exception.getMessage())));

    } catch (Exception exception) {
      SecurityContextHolder.clearContext();

      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write(new ObjectMapper()
          .writeValueAsString(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage())));

    }
  }

  private void processUsernamePasswordAuthentication(HttpServletResponse response, String username, String password) throws IOException {
    Authentication authentication = tryToAuthenticate(new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(authentication));
  }

  private void processTokenAuthentication(String token) {
    Authentication authentication = tryToAuthenticate(new PreAuthenticatedAuthenticationToken(token, null));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private Authentication tryToAuthenticate(Authentication authenticationRequest) {
    Authentication responseAuthentication = authenticationManager.authenticate(authenticationRequest);

    if (responseAuthentication == null || !responseAuthentication.isAuthenticated())
      throw new InternalAuthenticationServiceException("Unable to authenticate User with provided credentials");

    return responseAuthentication;
  }
}