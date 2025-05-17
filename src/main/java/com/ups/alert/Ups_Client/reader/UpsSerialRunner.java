package com.ups.alert.Ups_Client.reader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpsSerialRunner implements Runnable {
    private final String name, port, model, batteries, commport, serialno;
    private final int number;

    public UpsSerialRunner(String name, String port, String model, String batteries, String commport, int number, String serialno) {
        this.name = name;
        this.port = port;
        this.model = model;
        this.batteries = batteries;
        this.commport = commport;
        this.number = number;
        this.serialno = serialno;
    }

    @Override
    public void run() {
        try {
            log.info("[{}] Starting UPS connection on port {}", name, port);
            ConnectionToPort connection = new ConnectionToPort();
            connection.setAttributes(name, port, model, batteries, commport, number, serialno);
        } catch (Exception e) {
            log.error("[{}] Error while initializing UPS on port {}: {}", name, port, e.getMessage());
        }
    }
}