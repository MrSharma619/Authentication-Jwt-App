package com.example.authjwtdemo.middleware;

import com.example.authjwtdemo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//middle ware for api requests
//this will intercept every api call(OncePerRequestFilter)
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService service;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain //everything from url
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        //System.out.println("hi");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            //System.out.println("hi1");

            filterChain.doFilter(request, response); //continue to application
            return; //ends app
        }

        String token = authHeader.substring(7);

        //System.out.println("hi2 " + token);

        String email = service.extractUsername(token);

        //System.out.println("hi3 " + email);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(email != null && authentication == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            //System.out.println("hi4 " + userDetails.toString());

            if(service.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() //need this otherwise other api's dont return anything
                        );

                //System.out.println("hi5" + authToken);

                //convert request to spring understandable class
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                //System.out.println("hi5" + authToken);

            }

        }

        //System.out.println("hi6");
        filterChain.doFilter(request, response); //continue to application

    }
}
