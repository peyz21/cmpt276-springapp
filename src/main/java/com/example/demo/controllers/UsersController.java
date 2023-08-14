package com.example.demo.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

@Controller
public class UsersController {
    
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/users/view")
    public String getAllUsers(Model model){
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "users/showAll";
    }

    @GetMapping("/users/view/{uid}")
    public String getUser(Model model, @PathVariable Integer uid
    ){
        Optional<User> user = userRepo.findById(uid);
        user.ifPresent(value -> model.addAttribute("user", value));
        return "users/showUser";
    }

    @DeleteMapping("/users/delete/{uid}")
    public String deleteUser(@PathVariable Integer uid
    ){
        userRepo.deleteById(uid);
        return "redirect:/users/view";  // Redirect to the list of users after deleting
    }

    @DeleteMapping("/users/deleteAll")
    public String deleteAllUsers(){
        userRepo.deleteAll();
        return "redirect:/users/view";  // Redirect to the list of users after deleting all
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user){
        userRepo.save(user);
        return "redirect:/users/view";  // Redirect to the list of users after adding
    }

    @PutMapping("/users/edit/{uid}")
    public String editUser(@PathVariable Integer uid, @ModelAttribute User user){
        Optional<User> existingUser = userRepo.findById(uid);
        if(existingUser.isPresent()){
            User dbUser = existingUser.get();
            dbUser.setName(user.getName());
            dbUser.setPassword(user.getPassword());
            dbUser.setSize(user.getSize());
            userRepo.save(dbUser);
        }
        return "redirect:/users/view/{uid}";  // Redirect to the edited user's details page
    }
}
