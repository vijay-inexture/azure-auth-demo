package com.inexture.azureauthgradle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inexture.azureauthgradle.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	boolean existsByRoleName(String role);

}
