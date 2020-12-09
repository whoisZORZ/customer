package com.deik.webdev.customerapp.dao;

import com.deik.webdev.customerapp.entity.*;
import com.deik.webdev.customerapp.exception.*;
import com.deik.webdev.customerapp.model.Staff;
import com.deik.webdev.customerapp.repository.*;
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
public class StaffDaoImpl implements StaffDao {

    private final StaffRepository staffRepository;
    private final AddressRepository addressRepository;
    private final StoreRepository storeRepository;

    @Override
    public void createStaff(Staff staff) throws UnknownStoreException, UnknownAddressException, OutOfBoundsException {
        StaffEntity staffEntity;
        activeValue(staff.getActive());
        correctValue(staff.getId());
        correctValue(staff.getStoreId());
        correctValue(staff.getAddressId());

        staffEntity = StaffEntity.builder()
                .id(staff.getId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .address(queryAddress(staff.getAddressId()))
                .email(staff.getEmail())
                .store(queryStore(staff.getStoreId()))
                .active(staff.getActive())
                .username(staff.getUsername())
                .password(staff.getPassword())
                .lastUpdate(new Timestamp((new Date()).getTime()))
                .build();
        log.info("StaffEntity: {}", staffEntity);
        try {
            staffRepository.save(staffEntity);
            log.info("Recorded new Staff: {}", staff);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    protected AddressEntity queryAddress(int addressId) throws UnknownAddressException {
        Optional<AddressEntity> addressEntity = addressRepository.findById(addressId);
        if (!addressEntity.isPresent()) {
            throw new UnknownAddressException(String.valueOf(addressId));
        }
        else {
            log.trace("AddressEntity: {}", addressEntity);
            return addressEntity.get();
        }
    }

    protected StoreEntity queryStore(int storeId) throws UnknownStoreException {
        Optional<StoreEntity> storeEntity = storeRepository.findById(storeId);
        if (!storeEntity.isPresent()) {
            throw new UnknownStoreException(String.valueOf(storeId));
        }
        else {
            log.trace("StoreEntity: {}", storeEntity);
            return storeEntity.get();
        }
    }

    private void activeValue(int active) throws OutOfBoundsException {
        if (active != 0 && active != 1) {
            throw new OutOfBoundsException("Value of 'active' must be 0 or 1!");
        }
    }

    private void correctValue(int value) throws OutOfBoundsException {
        if (value < 0) {
            throw new OutOfBoundsException("Value can't be smaller than 0!");
        }
    }

    @Override
    public Collection<Staff> readAll() {
        log.info("Read all staff");
        return StreamSupport.stream(staffRepository.findAll().spliterator(),false)
                .map(entity -> new Staff(
                        entity.getId(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getAddress().getId(),
                        entity.getEmail(),
                        entity.getStore().getId(),
                        entity.getActive(),
                        entity.getUsername(),
                        entity.getPassword()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Staff> readStaffByUsername(String username) throws UnknownStaffException, EmptyException {
        if (username == null) {
            throw new EmptyException("Add a username!");
        }
        Collection<StaffEntity> staffEntity = staffRepository.findByUsername(username);
        if (staffEntity.isEmpty()) {
            throw new UnknownStaffException("No Staff Found");
        }
        else {
            log.info("Read all staff (by username)");
            return StreamSupport.stream(staffRepository.findByUsername(username).spliterator(),false)
                    .map(entity -> new Staff(
                            entity.getId(),
                            entity.getFirstName(),
                            entity.getLastName(),
                            entity.getAddress().getId(),
                            entity.getEmail(),
                            entity.getStore().getId(),
                            entity.getActive(),
                            entity.getUsername(),
                            entity.getPassword()
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<Staff> readStaffByEmail(String email) throws UnknownStaffException, EmptyException {
        if (email == null) {
            throw new EmptyException("Add an email!");
        }
        Collection<StaffEntity> staffEntity = staffRepository.findByEmail(email);
        if (staffEntity.isEmpty()) {
            throw new UnknownStaffException("No Staff Found");
        }
        else {
            log.info("Read all staff (by email)");
            return StreamSupport.stream(staffRepository.findByEmail(email).spliterator(),false)
                    .map(entity -> new Staff(
                            entity.getId(),
                            entity.getFirstName(),
                            entity.getLastName(),
                            entity.getAddress().getId(),
                            entity.getEmail(),
                            entity.getStore().getId(),
                            entity.getActive(),
                            entity.getUsername(),
                            entity.getPassword()
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<Staff> readStaffByStoreId(Integer storeId) throws UnknownStaffException, EmptyException {
        if (storeId == null) {
            throw new EmptyException("Add a store ID!");
        }
        Collection<StaffEntity> staffEntity = staffRepository.findByStoreId(storeId);
        if (staffEntity.isEmpty()) {
            throw new UnknownStaffException("No Staff Found");
        }
        else {
            log.info("Read all staff (by store ID)");
            return StreamSupport.stream(staffRepository.findByStoreId(storeId).spliterator(),false)
                    .map(entity -> new Staff(
                            entity.getId(),
                            entity.getFirstName(),
                            entity.getLastName(),
                            entity.getAddress().getId(),
                            entity.getEmail(),
                            entity.getStore().getId(),
                            entity.getActive(),
                            entity.getUsername(),
                            entity.getPassword()
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<Staff> readActiveStaff(Integer active) throws UnknownStaffException, OutOfBoundsException, EmptyException {
        if (active == null) {
            throw new EmptyException("Add 0 (inactive) or 1 (active)!");
        }
        activeValue(active);
        Collection<StaffEntity> staffEntity = staffRepository.findByActive(active);
        if (staffEntity.isEmpty()) {
            throw new UnknownStaffException("No Staff Found");
        }
        else {
            log.info("Read all staff (by store ID)");
            return StreamSupport.stream(staffRepository.findByActive(active).spliterator(),false)
                    .map(entity -> new Staff(
                            entity.getId(),
                            entity.getFirstName(),
                            entity.getLastName(),
                            entity.getAddress().getId(),
                            entity.getEmail(),
                            entity.getStore().getId(),
                            entity.getActive(),
                            entity.getUsername(),
                            entity.getPassword()
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Staff readStaffById(Integer id) throws UnknownStaffException, EmptyException, OutOfBoundsException {
        if (id == null) {
            throw new EmptyException("Add a store ID!");
        }
        correctValue(id);
        Optional<StaffEntity> staffEntity = staffRepository.findById(id);
        if (!staffEntity.isPresent()) {
            throw new UnknownStaffException("No Staff Found");
        }
        else {
            log.info("Read staff (by ID)");
            return new Staff(
                    staffEntity.get().getId(),
                    staffEntity.get().getFirstName(),
                    staffEntity.get().getLastName(),
                    staffEntity.get().getAddress().getId(),
                    staffEntity.get().getEmail(),
                    staffEntity.get().getStore().getId(),
                    staffEntity.get().getActive(),
                    staffEntity.get().getUsername(),
                    staffEntity.get().getPassword()
            );
        }
    }

    @Override
    public void deleteStaff(Staff staff) throws UnknownStaffException {
        Optional<StaffEntity> staffEntity = StreamSupport.stream(staffRepository.findAll().spliterator(),false).filter(
                entity ->{
                    return staff.getId() == entity.getId() &&
                            staff.getFirstName().equals(entity.getFirstName()) &&
                            staff.getLastName().equals(entity.getLastName()) &&
                            staff.getAddressId() == entity.getAddress().getId() &&
                            staff.getEmail().equals(entity.getEmail()) &&
                            staff.getStoreId() == entity.getStore().getId() &&
                            staff.getActive() == entity.getActive() &&
                            staff.getUsername().equals(entity.getUsername()) &&
                            staff.getPassword().equals(entity.getPassword());
                }
        ).findAny();
        if (!staffEntity.isPresent()) {
            throw new UnknownStaffException(String.format("Staff Not Found %s", staff));
        }
        staffRepository.delete(staffEntity.get());
        log.info("Deleted staff: " + staffEntity.toString());
    }

    @Override
    public void updateStaff(Staff staff, Staff newStaff) throws UnknownStoreException, UnknownAddressException, UnknownStaffException, OutOfBoundsException {
        Optional<StaffEntity> staffEntity = staffRepository.findById(staff.getId());
        if (!staffEntity.isPresent()) {
            throw new UnknownStaffException(String.format("Staff Not Found %s", staff), staff);
        }
        activeValue(newStaff.getActive());
        correctValue(newStaff.getId());
        correctValue(newStaff.getStoreId());
        correctValue(newStaff.getAddressId());
        log.info("Original: " + staffEntity.toString());
        staffEntity.get().setId(newStaff.getId());
        staffEntity.get().setFirstName(newStaff.getFirstName());
        staffEntity.get().setLastName(newStaff.getLastName());
        staffEntity.get().setAddress(queryAddress(newStaff.getAddressId()));
        staffEntity.get().setEmail(newStaff.getEmail());
        staffEntity.get().setStore(queryStore(newStaff.getStoreId()));
        staffEntity.get().setActive(newStaff.getActive());
        staffEntity.get().setUsername(newStaff.getUsername());
        staffEntity.get().setPassword(newStaff.getPassword());
        staffEntity.get().setLastUpdate(new Timestamp((new Date()).getTime()));
        log.info("Updated: " + staffEntity.toString());
        try {
            staffRepository.save(staffEntity.get());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
