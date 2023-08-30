package barbershop.web.admin;

import barbershop.data.SpecializationRepository;
import barbershop.data.UserRepository;
import barbershop.entities.Specialization;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SpecializationConverter implements Converter<String,Specialization> {

    private SpecializationRepository specializationRepo;

    @Autowired
    public SpecializationConverter(SpecializationRepository specializationRepo){
        this.specializationRepo = specializationRepo;
    }

    @Override
    public Specialization convert(String specialization) {
        return specializationRepo.findSpecializationBySpecialization(specialization);
    }
}
