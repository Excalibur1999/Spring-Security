package com.example.hrishi.security.controllers;

import com.example.hrishi.security.common.UserConstants;
import com.example.hrishi.security.entity.User;
import com.example.hrishi.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {



    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/join")
    public String joinGroup(@RequestBody User user){
       String encryptedPassword= passwordEncoder.encode(user.getPassword());

       user.setRoles(UserConstants.ROLE_USER);
       user.setPassword(encryptedPassword);
        userRepository.save(user);
        return "Hi "+user.getUsername()+" Welcome to the group";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    @GetMapping("/access/{userId}/{userRole}")
    public String giveAccessToUser(@PathVariable int userId,@PathVariable String userRole, Principal principal){
       User user= userRepository.findById(userId).get();
    
        List<String> rolesByLoggedInUser = getRolesByLoggedInUser(principal);
        String newRole="";
        if(rolesByLoggedInUser.contains(userRole)){
            newRole=user.getRoles()+","+userRole;
            user.setRoles(newRole);
            
        }
        userRepository.save(user);
        return "HI "+user.getUsername()+" new role assign to you by "+principal.getName();

    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> loadAllUser(){
        return userRepository.findAll();
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String test(){
        return "This is test method";
    }

    private List<String> getRolesByLoggedInUser(Principal principal){
        String roles=getLoggedInUser(principal).getRoles();
        List<String> assignRoles= Arrays.stream(roles.split(",")).collect(Collectors.toList());
        if(assignRoles.contains("ROLE_ADMIN")){
            return Arrays.stream(UserConstants.ADMIN_ACCESS).collect(Collectors.toList());
        }
        if(assignRoles.contains("ROLE_MODERATOR"))
             return Arrays.stream(UserConstants.MODERATOR_ACCESS).collect(Collectors.toList());
        return Collections.emptyList();
    }

    private User getLoggedInUser(Principal principal){
        return userRepository.findByUserName(principal.getName()).get();
    }
}
