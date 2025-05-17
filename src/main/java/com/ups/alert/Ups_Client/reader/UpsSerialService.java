package com.ups.alert.Ups_Client.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
class UpsSerialService {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void startUpsSerialConnection(String name, String port, String model,
                                         String batteries, String commport,
                                         int number, String serialno) {
        executor.submit(new UpsSerialRunner(name, port, model, batteries, commport, number, serialno));
    }
}