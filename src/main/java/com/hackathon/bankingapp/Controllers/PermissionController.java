package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.Entities.Permission;
import com.hackathon.bankingapp.Services.IPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permission")
@PreAuthorize("permitAll()")
public class PermissionController {

    private final IPermissionService permissionService;

    public PermissionController(IPermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PostMapping("/save")
    public ResponseEntity<Permission> savePermission(@RequestBody Permission permission){
        return ResponseEntity.ok(permissionService.savePermission(permission));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Permission>> getPermissions(){
        return ResponseEntity.ok(permissionService.getPermissions());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Permission> findPermission(@PathVariable Long id){
        Optional<Permission> permission = permissionService.findPermission(id);
        return permission.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePermission(@PathVariable Long id){
        permissionService.deletePermission(id);
        return ResponseEntity.ok("Permission deleted");
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Permission> editPermission(@PathVariable Long id, @RequestBody Permission permission){
        Optional<Permission> permissionEdit = permissionService.editPermission(id,permission);
        return permissionEdit.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
