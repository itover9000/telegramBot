package com.model;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class GeomagneticStormModel {
//    private DateFormat time_tag;
    private String time_tag;
    private int kp_index;
}
