package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.Entities.Role;
import com.hackathon.bankingapp.Services.IRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role")
@PreAuthorize("permitAll()")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping("/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        return ResponseEntity.ok(roleService.saveRole(role));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Role> findRole(@PathVariable Long id){
        Optional<Role> role = roleService.findRole(id);
        return role.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role deleted");
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Role> editRole(@PathVariable Long id, @RequestBody Role role){
        Optional<Role> roleEdit = roleService.editRole(id,role);
        return roleEdit.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
