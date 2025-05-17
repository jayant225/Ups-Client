package com.ups.alert.Ups_Client.reader;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;

@Slf4j
public class ReadStatus extends Thread {

    private final BufferedReader br;
    private final ConnectionToPort ctp;
    private final String name, model, batteries, commport, serialno;
    private final int upsno;

    public ReadStatus(ConnectionToPort ctp, String name, BufferedReader br,
                      String model, String batteries, String commport,
                      int upsno, String serialno) {
        this.ctp = ctp;
        this.br = br;
        this.name = name;
        this.model = model;
        this.batteries = batteries;
        this.commport = commport;
        this.upsno = upsno;
        this.serialno = serialno;
    }

    @Override
    public void run() {
        String status;

        while (true) {
            try {
                if (br.ready()) {
                    status = br.readLine();
                    if (status == null || status.trim().isEmpty()) continue;

                    log.info("[{}] Received: {}", name, status);

                    switch (ctp.Query) {
                        case "BatteryLevel":
                            processMetric("Battery Level %", status);
                            ctp.i = 3;
                            break;
                        case "PowerLoad":
                            processMetric("Power Load %", status);
                            ctp.i = 4;
                            break;
                        case "InputLineVoltage":
                            processMetric("Input Line Voltage", status);
                            ctp.i = 5;
                            break;
                        case "LineFrequency":
                            processMetric("Line Frequency", status);
                            ctp.i = 6;
                            break;
                        case "OutputVoltage":
                            processMetric("Output Voltage", status);
                            ctp.i = 7;
                            break;
                        case "EstimatedRuntime":
                            processMetric("Estimated Runtime", status.replace(":", ""));
                            ctp.i = 8;
                            break;
                        case "MaxLineVoltage":
                            processMetric("Max Line Voltage", status);
                            ctp.i = 9;
                            break;
                        case "MinLineVoltage":
                            processMetric("Min Line Voltage", status);
                            ctp.i = 10;
                            break;
                        case "BatteryVoltage":
                            processMetric("Battery Voltage", status);
                            ctp.i = 2;
                            break;
                        default:
                            log.debug("[{}] Ignoring unrecognized or startup message: {}", name, status);
                    }
                } else {
                    Thread.sleep(500);
                }

            } catch (Exception e) {
                log.error("[{}] Error in ReadStatus: {}", name, e.getMessage(), e);
            }
        }
    }




    private void processMetric(String label, String value) {
        log.info("[{}] {} = {}", name, label, value);

        // todo sms alert


        // Future: Insert to DB, send to master API, or trigger alert


    }
}
