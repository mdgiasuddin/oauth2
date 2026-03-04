package com.example.oauth2.controller;

import com.example.oauth2.model.entity.Country;
import com.example.oauth2.model.entity.Person;
import com.example.oauth2.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping
    public String test() {
        return "Test endpoint";
    }

    @GetMapping("/person/{id}")
    public void getPerson(@PathVariable Long id) {
        Person person = testService.getPerson(id);
        Country country = person.getCountry();
        log.info(country.getName());
    }
}
