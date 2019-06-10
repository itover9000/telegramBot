package com.model;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class GeomagneticStormModel {
    private String time_tag;
    private int kp_index;
}
