package com.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CurrencyModel {
    private static final String CUR_ID = "Cur_ID";
    private static final String CUR_ABBREVIATION = "Cur_Abbreviation";
    private static final String CUR_OFFICIAL_RATE = "Cur_OfficialRate";

    @SerializedName(CUR_ID)
    private int curId;

    @SerializedName(CUR_ABBREVIATION)
    private String curAbbreviation;

    @SerializedName(CUR_OFFICIAL_RATE)
    private double curOfficialRate;
}
