package com.cf.service.serviceImpl;

import com.cf.model.Device;
import com.cf.repository.DeviceRepository;
import com.cf.service.DeviceService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository repo;

    public DeviceServiceImpl() {
        this.repo = new DeviceRepository();
    }

    @Override
    public Device createDevice(Device device) {
        //Save the device by adding random id
        device.id = UUID.randomUUID().toString();
        device.lastCommunication = new Timestamp(System.currentTimeMillis());
        return repo.save(device);
    }
    @Override
    public List<Device> getAllDevices() {
        // Retrieve all devices
        return repo.findAll();
    }

    @Override
    public Optional<Device> getDevice(String id) {
        // Retrieve a device by its ID
        return repo.findById(id);
    }


    @Override
    public Optional<Device> updateDevice(String id, Device device) {
        // Update an existing device's details and timestamp
        return repo.findById(id).map(existing -> {
            device.id = id;
            device.lastCommunication = new Timestamp(System.currentTimeMillis());
            return repo.save(device);
        });
    }

    @Override
    public boolean deleteDevice(String id) {
        // Delete a device by its ID
        return repo.delete(id);
    }
}
