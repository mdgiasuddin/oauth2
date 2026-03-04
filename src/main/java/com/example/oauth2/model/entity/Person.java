package com.example.oauth2.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = LAZY)
    private Country country;
}
