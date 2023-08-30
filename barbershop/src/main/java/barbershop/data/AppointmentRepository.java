package barbershop.data;

import barbershop.entities.Appointment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    List<Appointment> findAllByBarberUserUsernameAndDate(String username, String date);

    List<Appointment> findAllByUserUsername(String username);

    List<Appointment> findAllByUserUsernameAndStatus(String username, String status);

    List<Appointment> findAllByStatusAndBarberUserId(String status, Long id);

    List<Appointment> findAllByStatus(String status);

    int countByBarberUserUsernameAndStatus(String username, String status);

    int countByBarberSpecializationSpecializationAndStatus(String specialization, String status);

    int countByStatus(String status);

    List<Appointment> findAllByStatusAndDateLike(String status, String date);

    List<Appointment> findAllByBarberId(long id);

    List<Appointment> findAllByUserId(long id);
}
