package com.example.gym.filters;

import com.example.gym.utils.TransactionContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TransactionFilter implements  jakarta.servlet.Filter {

    private static final Logger logger = LoggerFactory.getLogger(TransactionFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String transactionId = java.util.UUID.randomUUID().toString();
        servletRequest.setAttribute("transactionId", transactionId);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
