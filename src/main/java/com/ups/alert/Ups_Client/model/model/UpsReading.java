package com.ups.alert.Ups_Client.model.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsReading {

    private String date;
    private String time;
    private double vmin;
    private double vmax;
    private double vout;
    private double iout;
    private double percentWout;
    private double percentOut;
    private double frqOut;
    private double percentCap;
    private double vbat;
    private double tups;
}
