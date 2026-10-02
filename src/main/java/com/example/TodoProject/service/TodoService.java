package com.example.TodoProject.service;

import com.example.TodoProject.Models.Todo;
import com.example.TodoProject.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Todo createTodo(Todo todo, String userEmail){
        todo.setUserEmail(userEmail);
        return todoRepository.save(todo);
    }

    public Todo getTodobyId(Long id){
        return todoRepository.findById(id).orElseThrow(()->new RuntimeException("Todo not found")) ;
    }

    public List<Todo> getTodos(){
        return todoRepository.findAll();
    }

    public List<Todo> getTodosByUser(String userEmail){
        return todoRepository.findByUserEmail(userEmail);
    }
//    Update todo
    public Todo updateTodo(Todo todo){
        return todoRepository.save(todo);
    }
//  DeleteTodoById
    public void deleteTodoById(long id){
        todoRepository.delete(getTodobyId(id));
    }
//    DeleteTodo
    public void deleteTodo(Todo todo){
        todoRepository.delete(todo);
    }
//    Pagination
    public Page<Todo> getTodoPaged(int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        return todoRepository.findAll(pageable);
    }
}
