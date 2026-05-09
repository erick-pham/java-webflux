package com.example.erick.filter;

import jakarta.servlet.*;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            MDC.put("traceId", UUID.randomUUID().toString());
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            // Những lỗi này đúng kiểu nên throw ra luôn1
            throw e;
        } catch (Exception e) {
            // Những lỗi "lạ" (Exception) thì bọc lại thành ServletException
            throw new ServletException("Internal Filter Error", e);
        } finally {
            MDC.clear();
        }
    }
}
