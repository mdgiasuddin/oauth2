package com.example.oauth2.service;

import com.example.oauth2.model.entity.Country;
import com.example.oauth2.model.entity.Person;
import com.example.oauth2.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    private final PersonRepository personRepository;
    private final Random random = new SecureRandom();

    @Transactional
    public Person getPerson(long id) {
        Person person = personRepository.findById(id)
                .orElseThrow();

        Country country = person.getCountry();
        log.info(country.getName());
        return person;
    }
}
