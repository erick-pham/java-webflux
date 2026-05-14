package com.example.erick.filter;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.erick.shared.context.UserContext;
import com.example.erick.shared.context.UserRequestDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ContextCleanupFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            UserContext.set(new UserRequestDTO("123", "erick")); // Giả lập set user context

            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
