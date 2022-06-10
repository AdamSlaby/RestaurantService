package pl.restaurant.orderservice.business.configuration;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class BasicAuthRemoveFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isBatchRequest(request)) {
            filterChain.doFilter(new RemoveBasicHeader(request), response);
        } else
            filterChain.doFilter(request, response);
    }

    private boolean isBatchRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/pay/payu/notify");
    }
}
