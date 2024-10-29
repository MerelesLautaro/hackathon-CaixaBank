package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.Entities.Permission;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Repositories.IPermissionRepository;
import com.hackathon.bankingapp.Utils.NullAwareBeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService{

    private final IPermissionRepository permissionRepository;

    public PermissionService(IPermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findPermission(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public Optional<Permission> editPermission(Long id, Permission permission) {
        Permission permissionEdit = permissionRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Permission Not Found"));

        NullAwareBeanUtils.copyNonNullProperties(permission,permissionEdit);

        return Optional.of(this.permissionRepository.save(permissionEdit));
    }
}
