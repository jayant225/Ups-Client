package com.ups.alert.Ups_Client.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class FtpConnection implements Runnable {

    private String server = "172.20.3.50";
    private Integer port = 21;
    private String user = "apc";
    private String pass = "apc";

    public static FTPClient ftpClient;

    public FtpConnection() {}

    public FtpConnection(String ip, int port) {
        this.server = ip;
        this.port = port;
    }

    @Override
    public void run() {
        checkconn();
    }

    public void checkconn() {
        try {
            log.info("Trying to connect to FTP {}:{}", server, port);
            ftpClient = new FTPClient();
            ftpClient.connect(server, port);
            ftpClient.enterLocalPassiveMode(); // important for firewalls
            ftpClient.login(user, pass);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            if (ftpClient.isConnected()) {
                log.info("FTP connection established.");
                FTPConnect();
            }
        } catch (Exception e) {
            log.error("Error in CheckConn for FTP {}:{}", server, port, e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                log.warn("Error closing FTP connection", e);
            }
        }
    }

    public static void FTPConnect() {
        log.info("Inside FTPConnect - downloading file...");

        String remoteFilePath = "/logs/data.txt";
        String localFilePath = "/opt/ftpUser.txt";

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath))) {
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            if (success) {
                log.info("File downloaded successfully to {}", localFilePath);
            } else {
                log.warn("Failed to download file from FTP server.");
            }
        } catch (IOException ex) {
            log.error("Error during FTP file retrieval: ", ex);
        }
    }
}
