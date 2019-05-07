package de.brockhausag.jedichat.auth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

public class JwtFilter extends GenericFilterBean {

    private JwtProviderService jwtProviderService;

    @Autowired
    public JwtFilter(JwtProviderService jwtProviderService) {
        this.jwtProviderService = jwtProviderService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional<Authentication> authentication = jwtProviderService.getAuthentication((HttpServletRequest) request);
        authentication.ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
         chain.doFilter(request, response);
    }
}
