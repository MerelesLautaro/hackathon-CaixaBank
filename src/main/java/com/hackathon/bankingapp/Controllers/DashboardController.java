package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.DTO.AccountDTO;
import com.hackathon.bankingapp.DTO.UserDTO;
import com.hackathon.bankingapp.Services.IUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("isAuthenticated()")
public class DashboardController {

    private final IUserDetailsService userDetailsService;

    public DashboardController(IUserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getAuthenticatedUserInfo() {
        UserDTO authenticatedUser = userDetailsService.getAuthenticatedUser();
        return ResponseEntity.ok(authenticatedUser);
    }

    @GetMapping("/account")
    public ResponseEntity<AccountDTO> getAuthenticatedAccountInfo(){
        return ResponseEntity.ok(userDetailsService.getAuthenticationAccount());
    }

}
