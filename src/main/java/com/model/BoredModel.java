package com.model;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class BoredModel {
    private String activity;
    private int participants;
}
