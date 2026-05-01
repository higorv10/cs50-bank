package com.higor.cs50x.controller;


import com.higor.cs50x.config.SecurityConfig;
import com.higor.cs50x.dto.request.LoginRequest;
import com.higor.cs50x.dto.request.RegisterUserRequest;
import com.higor.cs50x.dto.response.LoginResponse;
import com.higor.cs50x.dto.response.ProfileResponse;
import com.higor.cs50x.dto.response.RegisterUserResponse;
import com.higor.cs50x.model.entity.User;
import com.higor.cs50x.model.adapter.UserPrincipal;
import com.higor.cs50x.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "user", description = "Controller for user registration and authentication")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@RequiredArgsConstructor
public class UserController
{
    private final UserService service;

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Method responsible for recording user data in the database")
    @ApiResponse(responseCode = "201", description = "Successfully registered user")
    @ApiResponse(responseCode = "400", description = "The fields were not filled in correctly")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request)
    {
        User user = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(user));
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Method responsible for user authentication")
    @ApiResponse(responseCode = "200", description = "User login successful")
    @ApiResponse(responseCode = "400", description = "The fields were not filled in correctly")
    @ApiResponse(responseCode = "401", description = "Invalid Credentials")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest request)
    {
        String token = service.verify(request);
        LoginResponse response = new LoginResponse(1800L, request.email(), token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/profile")
    @Operation(summary = "User Profile", description = "Method responsible for displaying user profile")
    @ApiResponse(responseCode = "200", description = "User profile displayed successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "500", description = "Invalid JWT token authentication")
    public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal)
    {
       ProfileResponse response = service.getMyProfile(userPrincipal);
       return ResponseEntity.ok(response);
    }
}
