package barbershop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "введите название")
    @Pattern(regexp = "^[а-яА-Я][а-яА-Я\\s]+", message = "название введено неверно")
    private String specialization;

    @NotBlank(message = "введите цену")
    @Pattern(regexp = "^[0-9]*(\\.[0-9]{0,2})?$", message = "цена введена неверно")
    private String price;
    public Specialization(){}


    public Specialization(String service){
        this.specialization = service;
    }

    /*@OneToOne(mappedBy = "specialization")
    private Barber barber;*/
}
