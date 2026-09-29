package com.example.TodoProject.controller;
import com.example.TodoProject.service.TodoService;
import org.springframework.data.domain.Page;  // Fixes the 'Page' error
import com.example.TodoProject.Models.Todo;
//import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

//import static jdk.internal.jrtfs.JrtFileAttributeView.AttrID.size;

@Slf4j
@RestController
//@RequestMapping("/api/v1")
public class TodoController {

    @Autowired
    private TodoService todoService;

//    @GetMapping("/auto")
//    String getii(){
//        demoService.getAllWorksDone();
//        return "It worked ";
//    }
    @PostMapping("/create")
    ResponseEntity<Todo> createUser(@RequestBody Todo todo){
        return new ResponseEntity<>(todoService.createTodo(todo), HttpStatus.CREATED);
    }

    @GetMapping("/getTodo/{id}")
    ResponseEntity<Todo> getTodoById(@PathVariable long id){
        try {
            Todo newTodo = todoService.getTodobyId(id);
            return new ResponseEntity<>(newTodo, HttpStatus.OK);
        }catch(RuntimeException e){
            log.info("Error");
            log.warn("Just a warning");
            log.error("",e);
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getTodos")
    ResponseEntity<List<Todo>> getTodos(){
            return new ResponseEntity<>(todoService.getTodos(),HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<Todo> updateTodo(@RequestBody Todo todo){
        return new ResponseEntity<>(todoService.updateTodo(todo), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    void deleteTodobyId (@PathVariable  long id){
        todoService.deleteTodoById(id);
    }

    @GetMapping("/pages")
    ResponseEntity<Page<Todo>> getTodoPaged(@RequestParam int  page, @RequestParam int  size){
        return new ResponseEntity<>(todoService.getTodoPaged(page,size),HttpStatus.OK);
    }
//    @GetMapping("/getAtd")
//    String getAtd(){
//        return "Attendance";
//    }
//
//    @GetMapping("/absent")
//    String absent(){
//        return "Absentice";
//    }
//
//    @GetMapping("/{num}")
//    String num(@PathVariable long num){
//        return "The num is " + num;
//    }
//
//    @PostMapping
//    String getName(@RequestBody String body){
//        return body;
//    }
//
//    @GetMapping
//    String data(@RequestParam String name){
//        return name;
//    }
//

//
//    @DeleteMapping("/{id}")
//    String DeleteId(@PathVariable("id") long num){
//        return "Delete id = " + num;
//    }

}
