package com.ups.alert.Ups_Client.configuration;

import com.ups.alert.Ups_Client.model.model.UpsDetail;
import com.ups.alert.Ups_Client.model.model.UpsDetailResponse;
import com.ups.alert.Ups_Client.reader.FtpConnection;
import com.ups.alert.Ups_Client.reader.UpsSerialRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class StartupApiCaller implements ApplicationRunner {

    @Value("${UPS_NAME:novalue}")
    private String upsName;

    @Value("${UPS_LOCATION:loc}")
    private String upsLocation;

    private final RestTemplate restTemplate;

    // Single-thread pool — change to Executors.newFixedThreadPool(n) for concurrency
    private final ExecutorService executorIp = Executors.newCachedThreadPool();

    private final ExecutorService executorSerial = Executors.newCachedThreadPool();

    public StartupApiCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean success = false;

        while (!success) {
            try {
                String url = String.format(
                        "http://10.81.0.209:8080/ups/v1/details?name=%s&floor=3&location=%s",
                        upsName, upsLocation
                );

                ResponseEntity<UpsDetailResponse> response =
                        restTemplate.getForEntity(url, UpsDetailResponse.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    List<UpsDetail> upsList = response.getBody().getData();
                    log.info("UPS List fetched on startup: {}", upsList);
                    success = true;

                    // Start one thread per UPS
                    for (UpsDetail upsDetail : upsList) {
                        try {

                       if(upsDetail.getConnectionType().equalsIgnoreCase("ip")) {
                           String ip = upsDetail.getIp();
                           int port = Integer.parseInt(upsDetail.getPort());

                           executorIp.submit(new FtpConnection(ip, port));
                           log.info("Started FTP connection thread for {}:{}", ip, port);

                       }
                       else if(upsDetail.getConnectionType().equalsIgnoreCase("serial")){

                           executorSerial.submit(new UpsSerialRunner("INV_2_9thfloor", "/dev/ttyS1", "SUA3000UXIQ",
                                   "04", "Comm4", 1, "BQ1218000795"));

                       }
                       else {
                           log.info("connection type is not correct");
                       }

                        } catch (NumberFormatException e) {
                            log.warn("Invalid port for UPS {}: {}", upsDetail.getName(), upsDetail.getPort());
                        }
                    }

                } else {
                    log.warn("API call failed with status {}. Retrying in 5 seconds...", response.getStatusCode());

                }

            } catch (Exception e) {
                log.error("API call failed: {}", e.getMessage());
                executorSerial.submit(new UpsSerialRunner("INV_2_9thfloor", "/dev/ttyUSB0", "SUA3000UXIQ",
                        "04", "Comm4", 2, "BQ1218000795"));
                success=true;
            }

            if (!success) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
