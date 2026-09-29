package com.example.TodoProject.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
public class User {
    @Id
    Long id;
    String email;
    String password;
}
