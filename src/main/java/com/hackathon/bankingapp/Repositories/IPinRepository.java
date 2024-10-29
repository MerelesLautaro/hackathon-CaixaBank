package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPinRepository extends JpaRepository<Pin,Long> {
}
