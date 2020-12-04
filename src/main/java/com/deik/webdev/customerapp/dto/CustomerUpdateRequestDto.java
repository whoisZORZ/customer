package com.deik.webdev.customerapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequestDto extends CustomerDto {

    private String newStore;
    private String newStaff;
    private String newStaffAddress;
    private String newStaffCity;
    private String newStaffCountry;
    private String newFirstName;
    private String newLastName;
    private String newEmail;
    private String newAddress;
    private String newCity;
    private String newCountry;

}