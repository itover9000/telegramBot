package com.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class GeomagneticStormModel {
    private static final String TIME_TAG = "time_tag";
    private static final String KP_INDEX = "kp_index";

    @SerializedName("time_tag")
    private String timeTag;

    @SerializedName("kp_index")
    private int kpIndex;
}
