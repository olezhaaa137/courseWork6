package barbershop.web.admin;

import lombok.Data;

@Data
public class EarningStat {

    private String month;

    private float earnings;

    public EarningStat(String month, float earnings) {
        this.month = month;
        this.earnings = earnings;
    }
}
