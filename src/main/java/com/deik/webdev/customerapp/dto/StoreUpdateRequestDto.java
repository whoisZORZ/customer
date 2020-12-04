package com.deik.webdev.customerapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateRequestDto extends StoreDto {

    private String newId;
    private String newStaff;
    private String newStaffAddress;
    private String newStaffCity;
    private String newStaffCountry;
    private String newAddress;
    private String newCity;
    private String newCountry;

}
