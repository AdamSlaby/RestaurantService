package pl.restaurant.orderservice.business.configuration;

import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springsecurity.facade.SimpleHttpFacade;
import org.keycloak.common.util.Base64;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
public class BasicAuthRemoveInterceptor extends OncePerRequestFilter {

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
