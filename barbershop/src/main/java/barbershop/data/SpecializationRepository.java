package barbershop.data;

import barbershop.entities.Specialization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecializationRepository extends CrudRepository<Specialization, Long> {
    Specialization findSpecializationBySpecialization(String specialization);
    List<Specialization> findAll();

    List<Specialization> findAllBySpecializationLike(String name);
}
