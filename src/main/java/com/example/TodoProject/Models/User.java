package com.example.TodoProject.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@Data
@Entity
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserTable")
public class User {
    @Id
    @GeneratedValue
    Long id;
    String email;
    String password;
}
