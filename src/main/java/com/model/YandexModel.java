package com.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class YandexModel {
    @SerializedName("code")
    private int code;
    @SerializedName("text")
    private List<String> text;
}
