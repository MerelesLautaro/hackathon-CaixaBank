package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.Entities.Permission;
import com.hackathon.bankingapp.Exceptions.EntityNotFoundException;
import com.hackathon.bankingapp.Entities.Role;
import com.hackathon.bankingapp.Repositories.IRoleRepository;
import com.hackathon.bankingapp.Utils.NullAwareBeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService implements IRoleService{

    private final IRoleRepository roleRepository;
    private final IPermissionService permissionService;

    public RoleService(IRoleRepository roleRepository, IPermissionService permissionService){
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    @Override
    public Role saveRole(Role role) {
        Set<Permission> permissionsList = new HashSet<>();

        for(Permission permission: role.getPermissionSet() ){
            Permission readPermission = permissionService.findPermission(permission.getId()).orElse(null);
            if(readPermission!=null){
                permissionsList.add(readPermission);
            }
        }

        if(!permissionsList.isEmpty())
        {
            role.setPermissionSet(permissionsList);
            return roleRepository.save(role);
        }

        return role;
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findRole(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> editRole(Long id, Role role) {
        Role roleEdit = this.findRole(id).orElseThrow(() -> new EntityNotFoundException("Entity not found")) ;;

        NullAwareBeanUtils.copyNonNullProperties(role,roleEdit);

        return Optional.ofNullable(this.saveRole(roleEdit));
    }

    @Override
    public Set<Role> findRoleByName(String nameRole) {
        List<Role> allRoles = this.getRoles();
        Set<Role> containRole = new HashSet<>();

        for(Role role: allRoles){
            if(Objects.equals(role.getRole(), nameRole)){
                containRole.add(role);
            }
        }

        return containRole;
    }
}
