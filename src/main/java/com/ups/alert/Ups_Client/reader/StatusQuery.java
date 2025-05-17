package com.ups.alert.Ups_Client.reader;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;

@Slf4j
public class StatusQuery extends Thread {

    private final BufferedOutputStream bo;
    private final int upsno;
    private final ConnectionToPort ctp;

    private boolean b2 = true, b3, b4, b5, b6, b7, b8, b9, b10;

    public StatusQuery(ConnectionToPort ctp, BufferedOutputStream sout, int upsno) {
        this.ctp = ctp;
        this.bo = sout;
        this.upsno = upsno;
    }

    private void doQuery(char c, String label) {
        try {
            bo.write(c);
            bo.flush();
            ctp.Query = label;
        } catch (Exception e) {
            log.error("[UPS:{}] Failed to send query '{}': {}", upsno, label, e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            log.info("[UPS:{}] Sending Smart Mode query", upsno);
            doQuery((char) 89, "SmartMode");
            Thread.sleep(8000);

            while (true) {
                Thread.sleep(2000);

                if (ctp.i == 2 && b2) {
                    log.info("[UPS:{}] Query: BatteryLevel", upsno);
                    doQuery('f', "BatteryLevel");
                    Thread.sleep(7000);
                    b2 = false; b3 = true;
                } else if (ctp.i == 3 && b3) {
                    log.info("[UPS:{}] Query: PowerLoad", upsno);
                    doQuery('P', "PowerLoad");
                    Thread.sleep(7000);
                    b3 = false; b4 = true;
                } else if (ctp.i == 4 && b4) {
                    log.info("[UPS:{}] Query: InputLineVoltage", upsno);
                    doQuery('L', "InputLineVoltage");
                    Thread.sleep(7000);
                    b4 = false; b5 = true;
                } else if (ctp.i == 5 && b5) {
                    log.info("[UPS:{}] Query: LineFrequency", upsno);
                    doQuery('F', "LineFrequency");
                    Thread.sleep(7000);
                    b5 = false; b6 = true;
                } else if (ctp.i == 6 && b6) {
                    log.info("[UPS:{}] Query: OutputVoltage", upsno);
                    doQuery('O', "OutputVoltage");
                    Thread.sleep(7000);
                    b6 = false; b7 = true;
                } else if (ctp.i == 7 && b7) {
                    log.info("[UPS:{}] Query: EstimatedRuntime", upsno);
                    doQuery('j', "EstimatedRuntime");
                    Thread.sleep(7000);
                    b7 = false; b8 = true;
                } else if (ctp.i == 8 && b8) {
                    log.info("[UPS:{}] Query: MaxLineVoltage", upsno);
                    doQuery('M', "MaxLineVoltage");
                    Thread.sleep(7000);
                    b8 = false; b9 = true;
                } else if (ctp.i == 9 && b9) {
                    log.info("[UPS:{}] Query: MinLineVoltage", upsno);
                    doQuery('N', "MinLineVoltage");
                    Thread.sleep(7000);
                    b9 = false; b10 = true;
                } else if (ctp.i == 10 && b10) {
                    log.info("[UPS:{}] Query: BatteryVoltage", upsno);
                    doQuery('B', "BatteryVoltage");
                    Thread.sleep(7000);
                    b10 = false; b2 = true;
                }
            }

        } catch (InterruptedException e) {
            log.error("[UPS:{}] StatusQuery interrupted", upsno);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("[UPS:{}] StatusQuery error: {}", upsno, e.getMessage(), e);
        }
    }
}
