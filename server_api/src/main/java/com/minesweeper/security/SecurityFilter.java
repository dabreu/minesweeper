package com.minesweeper.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.minesweeper.model.User;
import com.minesweeper.repository.UserRepository;

@Component
public class SecurityFilter implements Filter {

    @Autowired
    UserRepository repository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (requiresAuthentication(req)) {
            if (!authenticate(req, res)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean requiresAuthentication(HttpServletRequest req) {
        String uri = req.getRequestURI();
        return (!uri.contains("/login") && !uri.contains("/register"));
    }

    private boolean authenticate(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String token = req.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return false;
        }
        Optional<User> user = repository.findUsernameByActiveToken(token);
        if (!user.isPresent()) {
            return false;
        }
        SecurityContext.setPrincipal(user.get().getUsername());
        return true;
    }

}
