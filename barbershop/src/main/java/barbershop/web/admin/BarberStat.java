package barbershop.web.admin;

import lombok.Data;

@Data
public class BarberStat {
    String name;
    String surname;
    int count;

    public BarberStat(String name, String surname, int count){
        this.name = name;
        this.surname = surname;
        this.count = count;
    }
}
