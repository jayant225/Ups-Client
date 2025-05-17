package com.ups.alert.Ups_Client.model.model;

import lombok.Data;

import java.util.List;

@Data
public class UpsDetailResponse {
    private String status;
    private List<UpsDetail> data;
}
