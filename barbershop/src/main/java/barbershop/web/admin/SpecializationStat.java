package barbershop.web.admin;

import lombok.Data;

@Data
public class SpecializationStat {

    private String specialization;
    private float popularity;

    public SpecializationStat(String specialization, float popularity) {
        this.specialization = specialization;
        this.popularity = popularity;
    }
}
