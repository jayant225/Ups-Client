package com.ups.alert.Ups_Client.model.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpsDetail {
    private Long id;
    private String name;
    private String model;
    private String connectionType;
    private String floor;
    private String location;
    private String currentPowerStatus;
    private String currentBatteryLevel;
    private String currentVoltage;
    private LocalDateTime lastSeen;
    private String port;
    private String ip;
    private Integer batteries;
}

