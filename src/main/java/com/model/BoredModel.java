package com.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class BoredModel {
    @SerializedName("activity")
    private String activity;
    @SerializedName("participants")
    private int participants;
}
