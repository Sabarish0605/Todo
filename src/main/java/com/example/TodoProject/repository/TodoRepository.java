package com.example.TodoProject.repository;

import com.example.TodoProject.Models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// CRUD Operations - Create,Read,Update,Delete
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserEmail(String userEmail);
}
