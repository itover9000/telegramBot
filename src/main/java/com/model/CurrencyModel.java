package com.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CurrencyModel {
    @SerializedName("Cur_ID")
    private int curId;
    @SerializedName("Cur_Abbreviation")
    private String curAbbreviation;
    @SerializedName("Cur_OfficialRate")
    private double curOfficialRate;
}
