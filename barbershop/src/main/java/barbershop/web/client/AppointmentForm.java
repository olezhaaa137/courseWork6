package barbershop.web.client;

import barbershop.data.BarberRepository;
import barbershop.entities.Appointment;
import barbershop.entities.Barber;
import barbershop.entities.User;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentForm {

    private String specialization;
    private String barberUsername;

    private String date;

    private String time;

    private User user;

    public Appointment convertToAppointment( BarberRepository repository, User user,
                                            String date, String time, String barberUsername){
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setUser(user);
        appointment.setBarber(repository.findByUserUsername(barberUsername));
        return appointment;
    }
}
