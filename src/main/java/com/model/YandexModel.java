package com.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class YandexModel {
    private int code;
    private String lang;
    private List<String> text;
}
