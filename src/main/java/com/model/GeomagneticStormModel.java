package com.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeomagneticStormModel {
    @SerializedName("time_tag")
    private String timeTag;

    @SerializedName("kp_index")
    private int kpIndex;
}
