package com.cf.repository;

import com.cf.model.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceRepository {

    //Uses ConcurrentHashMap to allow safe concurrent access across multiple HTTP requests.
    private static final Map<String, Device> devices = new ConcurrentHashMap<>();

    public List<Device> findAll() {
        return new ArrayList<>(devices.values());
    }

    public Optional<Device> findById(String id) {
        return Optional.ofNullable(devices.get(id));
    }

    public Device save(Device device) {
        devices.put(device.id, device);
        return device;
    }

    public boolean delete(String id) {
        return devices.remove(id) != null;
    }

    public boolean exists(String id) {
        return devices.containsKey(id);
    }
}
