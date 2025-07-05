package com.cf.service;

import com.cf.model.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    List<Device> getAllDevices();

    Optional<Device> getDevice(String id);

    Device createDevice(Device device);

    Optional<Device> updateDevice(String id, Device device);

    boolean deleteDevice(String id);
}
