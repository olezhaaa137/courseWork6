package barbershop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Pattern(regexp = "[а-яА-Я]+")
    private String day;


    @Pattern(regexp = "[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2}")
    private String time;

    @ManyToOne//(cascade = CascadeType.ALL)
    private Barber barber;

}
