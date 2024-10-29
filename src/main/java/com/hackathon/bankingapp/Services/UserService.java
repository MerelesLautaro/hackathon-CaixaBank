package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.UserDTO;
import com.hackathon.bankingapp.DTO.UserRegisterRequestDTO;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Role;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Repositories.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final RoleService roleService;
    private final IAccountService accountService;

    public UserService(IUserRepository userRepository, RoleService roleService, IAccountService accountService){
        this.userRepository = userRepository;
        this.roleService  = roleService;
        this.accountService = accountService;
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserRegisterRequestDTO userRegisterRequestDTO) {

        if (userRepository.existsByEmail(userRegisterRequestDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        if (userRepository.existsByPhoneNumber(userRegisterRequestDTO.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is already in use");
        }

        User user = new User();
        user.setName(userRegisterRequestDTO.getName());
        user.setEmail(userRegisterRequestDTO.getEmail());
        user.setAddress(userRegisterRequestDTO.getAddress());
        user.setPhoneNumber(userRegisterRequestDTO.getPhoneNumber());
        user.setPassword(this.encriptPassword(userRegisterRequestDTO.getPassword()));

        // Asignar roles por defecto
        Set<Role> roleList = roleService.findRoleByName("USER");
        if (roleList.isEmpty()) {
            throw new EntityNotFoundException("Role by name not found");
        }
        user.setRoleList(roleList);

        User userSaved = userRepository.save(user);

        // Crear una cuenta para el usuario si no tiene una
        if (userSaved.getAccount() == null) {
            Account account = new Account();
            account.setAccountNumber(userSaved.getAccountNumber());
            accountService.saveAccount(account);
            userSaved.setAccount(account);
            userRepository.save(userSaved);
        }

        return UserDTO.fromUser(userSaved);
    }


    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
