package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission,Long> {
}
