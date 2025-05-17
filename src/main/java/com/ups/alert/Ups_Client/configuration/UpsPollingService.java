package com.ups.alert.Ups_Client.configuration;



import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Configuration
public class UpsPollingService {

    // need to fetch from master service
    private final String LOCATION = "Indore";

    @Scheduled(fixedDelay = 30000)
    public void poll() {




//
//        List<UpsDetail> upsList = new ArrayList<>();
//
//        for (UpsDetail ups : upsList) {
//
//            String raw = null;
//            if ("Serial".equalsIgnoreCase(ups.getConnectionType())) {
//
//
////                raw = serialReader.read(ups);
//
//
//            } else if ("FTP".equalsIgnoreCase(ups.getConnectionType())) {
//
//
////                raw = ftpReader.read(ups);
//
//
//            }
//
//
//        }
   }

}