package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.DTO.*;
import com.hackathon.bankingapp.Services.IUserDetailsService;
import com.hackathon.bankingapp.Services.IUserService;
import com.hackathon.bankingapp.Services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/users")
@PreAuthorize("permitAll()")
public class UserController {

    private final IUserService userService;
    private final IUserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    public UserController(IUserService userService,IUserDetailsService userDetailsService, TokenBlacklistService tokenBlacklistService){
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid AuthLoginRequestDTO userRequest) {
        return ResponseEntity.ok(userDetailsService.loginUser(userRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        tokenBlacklistService.addTokenToBlacklist(token);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserRegisterRequestDTO userRegisterRequestDTO){
        return ResponseEntity.ok(userService.saveUser(userRegisterRequestDTO));
    }
}
