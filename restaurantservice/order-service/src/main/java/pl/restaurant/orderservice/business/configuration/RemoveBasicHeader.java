package pl.restaurant.orderservice.business.configuration;

import org.springframework.http.HttpHeaders;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class RemoveBasicHeader extends HttpServletRequestWrapper {
    public RemoveBasicHeader(HttpServletRequest request) {
        super(request);
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return true;
    }

    @Override
    public String getAuthType() {
        return "Basic";
    }

    @Override
    public String getHeader(String name) {
        if (!HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
            return super.getHeader(name);
        }
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (!HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
            return super.getHeaders(name);
        }
        return Collections.enumeration(Collections.emptyList());
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(Collections.list(super.getHeaderNames())
                .stream()
                .filter(s -> !HttpHeaders.AUTHORIZATION.equalsIgnoreCase(s))
                .collect(Collectors.toList()));
    }

    @Override
    public int getIntHeader(String name) {
        if (!HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
            return super.getIntHeader(name);
        }
        return -1;
    }
}
