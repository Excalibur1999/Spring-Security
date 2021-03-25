package com.example.hrishi.security.controllers;

import com.example.hrishi.security.entity.User;
import com.example.hrishi.security.model.JwtRequest;
import com.example.hrishi.security.model.JwtResponse;
import com.example.hrishi.security.repositories.UserRepository;
import com.example.hrishi.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")
public class JwtController {

     @Autowired
     private AuthenticationManager authenticationManager;
     @Autowired
     private UserDetailsService groupUserDetailService;

     @Autowired
     private JwtUtil jwtUtil;

     @Autowired
     BCryptPasswordEncoder encoder;
     @Autowired
     UserRepository userRepository;

     @PostMapping("/token")
     public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
          try {

               Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

          }catch (UsernameNotFoundException e){
//               e.printStackTrace();
               throw new Exception("Bad Credentials");
          }
          UserDetails userDetails = groupUserDetailService.loadUserByUsername(jwtRequest.getUsername());
         String token= jwtUtil.generateToken(userDetails);

         return ResponseEntity.ok(new JwtResponse(token));

     }
     @RequestMapping("/welcome")
     public String welcome(){
          return "Welcome to this private site";
     }
}
