package com.Ashish.ProjectManagementSystem.UserRepository;

import com.Ashish.ProjectManagementSystem.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public  User findByEmail(String email);
}
