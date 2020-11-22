package com.deik.webdev.customerapp.service;

import com.deik.webdev.customerapp.dao.CountryDao;
import com.deik.webdev.customerapp.exception.UnknownCountryException;
import com.deik.webdev.customerapp.model.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryDao countryDao;

    @Override
    public Collection<Country> getAllCountry() {
        return countryDao.readAll();
    }

    @Override
    public void recordCountry(Country country) {
        countryDao.createCountry(country);
    }

    @Override
    public void deleteCountry(Country country) throws UnknownCountryException {
        countryDao.deleteCountry(country);
    }

}