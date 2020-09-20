package com.example.demo.audit;

//public class AuditorAwareImpl implements AuditorAware<String> {
//
//    @Override
//    public Optional<String> getCurrentAuditor() {
//        return Optional.of("TestUser");
//        // Use below commented code when will use Spring Security.
//    }
//}


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("current authentication:" + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return Optional.of(((UserDetails) authentication.getPrincipal()).getUsername());
    }
}