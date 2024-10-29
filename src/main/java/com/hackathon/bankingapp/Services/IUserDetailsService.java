package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.AccountDTO;
import com.hackathon.bankingapp.DTO.AuthLoginRequestDTO;
import com.hackathon.bankingapp.DTO.UserDTO;
import com.hackathon.bankingapp.Exceptions.UserNotFoundException;
import com.hackathon.bankingapp.Repositories.IUserRepository;
import com.hackathon.bankingapp.Utils.JWTUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class IUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    private IUserDetailsService(IUserRepository userRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        com.hackathon.bankingapp.Entities.User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        //creamos una lista para los permisos
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //traer roles y convertirlos en 'SimpleGrantedAuthority'
        user.getRoleList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));
        // concatenamos 'ROLE_' para diferenciarlo de los permisos, Spring Security reconoce esto como un ROL


        //traer permisos y convertirlos en 'SimpleGrantedAuthority'
        user.getRoleList().stream()
                .flatMap(role -> role.getPermissionSet().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermission())));

        return new User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNotExpired(),
                user.isAccountNotExpired(),
                user.isCredentialNotExpired(),
                authorityList
        );
    }

    public Map<String, String> loginUser(AuthLoginRequestDTO userRequest) {
        //recuperar usuario y contraseña
        String email = userRequest.email();
        String password = userRequest.password();

        Authentication authentication = this.authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createToken(authentication);
        return Collections.singletonMap("token",token);
    }

    private Authentication authenticate(String email, String password) {

        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails == null) {
            throw new UserNotFoundException("User not found for the given identifier: " + email);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public UserDTO getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        String email = authentication.getName();

        com.hackathon.bankingapp.Entities.User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getAccountNumber(),
                user.getPassword()
        );
    }

    public AccountDTO getAuthenticationAccount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        String email = authentication.getName();

        com.hackathon.bankingapp.Entities.User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return AccountDTO.fromAccount(user.getAccount());
    }

    public boolean hasRoleAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}
