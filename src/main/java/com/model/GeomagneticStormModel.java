package com.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class GeomagneticStormModel {
    @SerializedName("time_tag")
    private String timeTag;

    @SerializedName("kp_index")
    private int kpIndex;
}
