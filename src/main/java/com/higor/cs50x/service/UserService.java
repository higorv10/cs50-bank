package com.higor.cs50x.service;

import com.higor.cs50x.dto.request.LoginRequest;
import com.higor.cs50x.dto.request.RegisterUserRequest;
import com.higor.cs50x.dto.response.ProfileResponse;
import com.higor.cs50x.exceptions.InvalidCredentialsException;
import com.higor.cs50x.exceptions.UserAlreadyExistsException;
import com.higor.cs50x.model.entity.User;
import com.higor.cs50x.model.adapter.UserPrincipal;
import com.higor.cs50x.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository repository;
    private final AuthenticationManager manager;
    private final JWTService jwtService;
    private final PasswordEncoder encoder;

    public User register(RegisterUserRequest request)
    {
        if(repository.existsByEmailOrCellphoneOrCpf(
                request.email(),
                request.cellphone(),
                request.cpf()))
        {
         throw new UserAlreadyExistsException("Email, cellphone or CPF already registered");
        }
        User user = new User();
        user.setName(request.name());
        user.setCpf(request.cpf());
        user.setCellphone(request.cellphone());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));

        repository.save(user);
        return user;
    }

    public String verify(LoginRequest request)
    {
        try {
             manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()));
             return jwtService.generateToken(request.email());
        }
        catch (BadCredentialsException | UsernameNotFoundException e)
        {
            throw new InvalidCredentialsException("Please provide a valid password or email address");
        }
    }

    public ProfileResponse getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        User user = repository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Authentication Error"));

        return new ProfileResponse(user);
    }
}

