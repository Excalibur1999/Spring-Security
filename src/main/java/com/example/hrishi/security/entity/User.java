package com.example.hrishi.security.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String userName;
    private String password;
    private boolean active;
    private String roles;
}
