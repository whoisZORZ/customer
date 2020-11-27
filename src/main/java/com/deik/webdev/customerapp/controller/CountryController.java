package com.deik.webdev.customerapp.controller;

import com.deik.webdev.customerapp.dto.CountryDto;
import com.deik.webdev.customerapp.model.Country;
import com.deik.webdev.customerapp.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CountryController {

    private final CountryService service;

    @GetMapping("/country")
    public Collection<CountryDto> listCountries() {
        return service.getAllCountry()
                .stream()
                .map(model -> CountryDto.builder()
                        .name(model.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping("/country")
    public void record(@RequestBody CountryDto requestDto) {
        try {
            service.recordCountry(new Country(
                    requestDto.getName()
            ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}