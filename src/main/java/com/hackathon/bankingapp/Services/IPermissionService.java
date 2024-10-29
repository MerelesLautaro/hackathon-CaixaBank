package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.Entities.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    public Permission savePermission(Permission permission);
    public List<Permission> getPermissions();
    public Optional<Permission> findPermission(Long id);
    public void deletePermission(Long id);
    public Optional<Permission> editPermission(Long id, Permission permission);
}
