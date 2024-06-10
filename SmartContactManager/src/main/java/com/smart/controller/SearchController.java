package com.smart.controller;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @GetMapping("/search/{name}")
    public ResponseEntity<?> search(@PathVariable("name") String name, Principal principal){
        User user=userRepository.getUserByName(principal.getName());
        List<Contact> contact=contactRepository.findByNameContainingAndUser(name,user);
        return ResponseEntity.ok(contact);
    }

}
