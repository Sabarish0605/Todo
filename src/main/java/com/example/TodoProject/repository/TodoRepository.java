package com.example.TodoProject.repository;

import com.example.TodoProject.Models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD Operations - Create,Read,Update,Delete
public interface TodoRepository extends JpaRepository<Todo, Long> {

}
