package com.ups.alert.Ups_Client.reader;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;

@Slf4j
@AllArgsConstructor
class ConnectionToPort {
    public CommPortIdentifier cpid;
    public CommPort cp;
    public SerialPort mySerialPort;
    public BufferedOutputStream sout;
    public BufferedReader br;
    public String Query = "";
    public int i;

    public ConnectionToPort() {

    }

    public void setAttributes(String name, String port, String model, String batteries, String commport, int number, String serialno) {
        try {
            log.info("[{}] Initializing port: {} for model: {}", name, port, model);
            Properties p = System.getProperties();
            p.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyS0:/dev/ttyS1:/dev/ttyS2:/dev/ttyS3:/dev/ttyS4:/dev/ttyS5:/dev/ttyS6:/dev/ttyS7:/dev/ttyUSB0");
              log.info("port :{}",port);
            this.cpid = CommPortIdentifier.getPortIdentifier(port);
            this.cp = this.cpid.open("UPSInverter", 500);
            this.mySerialPort = (SerialPort) this.cp;
            this.mySerialPort.setSerialPortParams(2400, 8, 1, 0);
            this.mySerialPort.setRTS(true);

            this.br = new BufferedReader(new InputStreamReader(this.mySerialPort.getInputStream()));
            this.sout = new BufferedOutputStream(new PrintStream(this.mySerialPort.getOutputStream()));

            StatusQuery sq = new StatusQuery(this, this.sout, number);
            sq.start();
            Thread.sleep(100L);

            ReadStatus rs = new ReadStatus(this, name, this.br, model, batteries, commport, number, serialno);
            rs.start();

        } catch (Exception e) {
            log.error("Exception in ConnectionToPort: ", e);
        }
    }
}
