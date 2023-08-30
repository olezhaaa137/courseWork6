package barbershop.data;

import barbershop.entities.Barber;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BarberRepository extends CrudRepository<Barber, Long> {
    List<Barber> findAllByUserRole(String role);
    Barber findByUserUsername(String username);

    List<Barber> findAllBySpecializationSpecialization(String specialization);

    Barber findByUserId(Long id);
}
