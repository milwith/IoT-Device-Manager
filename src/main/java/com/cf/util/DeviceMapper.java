package com.cf.util;

import com.cf.dto.DeviceDto;
import com.cf.model.Device;

public class DeviceMapper {

    public static DeviceDto toDto(Device device) {
        DeviceDto dto = new DeviceDto();
        dto.id = device.id;
        dto.name = device.name;
        dto.type = device.type;
        dto.status = device.status;
        dto.lastCommunication = device.lastCommunication;
        return dto;
    }

    public static Device toEntity(DeviceDto dto) {
        Device device = new Device();
        device.id = dto.id;
        device.name = dto.name;
        device.type = dto.type;
        device.status = dto.status;
        device.lastCommunication = dto.lastCommunication;
        return device;
    }
}
