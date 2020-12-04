package com.deik.webdev.customerapp.dao;

import com.deik.webdev.customerapp.entity.CityEntity;
import com.deik.webdev.customerapp.entity.CountryEntity;
import com.deik.webdev.customerapp.exception.UnknownCityException;
import com.deik.webdev.customerapp.model.City;
import com.deik.webdev.customerapp.repository.CityRepository;
import com.deik.webdev.customerapp.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityDaoImpl implements CityDao {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Override
    public void createCity(City city) {
        CityEntity cityEntity;

        cityEntity = CityEntity.builder()
                .city(city.getCity())
                .country(queryCountry(city.getCountry()))
                .lastUpdate(new Timestamp((new Date()).getTime()))
                .build();
        log.info("CityEntity: {}", cityEntity);
        try {
            cityRepository.save(cityEntity);
        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    protected CountryEntity queryCountry(String country) {

        Optional<CountryEntity> countryEntity = countryRepository.findByCountry(country);
        if (!countryEntity.isPresent()) {
            countryEntity = Optional.ofNullable(CountryEntity.builder()
                    .country(country)
                    .lastUpdate(new Timestamp((new Date()).getTime()))
                    .build());
            countryRepository.save(countryEntity.get());
            log.info("Recorded new Country: {}", country);
        }
        log.trace("Country Entity: {}", countryEntity);
        return countryEntity.get();
    }

    @Override
    public Collection<City> readAll() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(),false)
                .map(entity -> new City(
                        entity.getCity(),
                        entity.getCountry().getCountry()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCity(City city) throws UnknownCityException {
        Optional<CityEntity> cityEntity = StreamSupport.stream(cityRepository.findAll().spliterator(),false).filter(
                entity ->{
                    return city.getCity().equals(entity.getCity()) &&
                            city.getCountry().equals(entity.getCountry().getCountry());
                }
        ).findAny();
        if (!cityEntity.isPresent()) {
            throw new UnknownCityException(String.format("City Not Found %s", city), city);
        }
        cityRepository.delete(cityEntity.get());
    }

    @Override
    public void updateCity(City city, City newCity) throws UnknownCityException {
        Optional<CityEntity> cityEntity = cityRepository.findByCity(city.getCity());
        if (!cityEntity.isPresent()) {
            throw new UnknownCityException(String.format("City Not Found %s", city), city);
        }
        log.info("Original: " + cityEntity.toString());
        cityEntity.get().setCity(newCity.getCity());
        cityEntity.get().setLastUpdate(new Timestamp((new Date()).getTime()));
        log.info("Updated: " + cityEntity.toString());
        try {
            cityRepository.save(cityEntity.get());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
