package barbershop.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String date;

    private String time;

    private String status;

    @ManyToOne
    private User user;

    @ManyToOne
    Barber barber;

}
