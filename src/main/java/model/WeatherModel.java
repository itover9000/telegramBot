package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherModel {

    private String name;
    private Double temp;
    private Double humidity;
    private String icon;
    private String main;

}
