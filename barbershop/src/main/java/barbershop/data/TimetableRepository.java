package barbershop.data;

import barbershop.entities.Timetable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TimetableRepository extends CrudRepository<Timetable, Long> {
    List<Timetable> findAllByBarberId(Long id);
    Timetable findByBarberUserUsernameAndDay(String username, String day);
}
