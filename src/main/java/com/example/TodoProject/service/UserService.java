package com.example.TodoProject.service;

import com.example.TodoProject.Models.User;
import com.example.TodoProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    private UserRepository UserRepository;

    public User createUser(User User){
        return UserRepository.save(User);
    }

    public User getUserbyId(Long id){
        return UserRepository.findById(id).orElseThrow(()->new RuntimeException("User not found")) ;
    }

    public List<User> getUsers(){
        return UserRepository.findAll();
    }
    //    Update User
    public User updateUser(User User){
        return UserRepository.save(User);
    }
    //  DeleteUserById
    public void deleteUserById(long id){
        UserRepository.delete(getUserbyId(id));
    }
    //    DeleteUser
    public void deleteUser(User User){
        UserRepository.delete(User);
    }
}
