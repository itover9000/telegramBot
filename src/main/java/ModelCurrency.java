import lombok.Data;

import java.util.Date;

@Data
public class ModelCurrency {
    private String date;
    private String abbreviation;
    private Double officialRate;
}
