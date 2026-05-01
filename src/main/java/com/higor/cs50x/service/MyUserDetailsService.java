package com.higor.cs50x.service;

import com.higor.cs50x.model.entity.User;
import com.higor.cs50x.model.adapter.UserPrincipal;
import com.higor.cs50x.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService
{
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email)
    {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Authentication failed."));
        return new UserPrincipal(user);
    }
}
