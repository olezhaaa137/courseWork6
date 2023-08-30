package barbershop.web.admin;

import barbershop.data.AppointmentRepository;
import barbershop.data.SpecializationRepository;
import barbershop.entities.Specialization;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/viewSpecializationsStat")
public class SpecializationsStatController {

    private AppointmentRepository appointmentRepo;
    private SpecializationRepository specializationRepo;

    @Autowired
    public SpecializationsStatController(AppointmentRepository appointmentRepo,
                                         SpecializationRepository specializationRepo){
        this.appointmentRepo = appointmentRepo;
        this.specializationRepo = specializationRepo;
    }

    @ModelAttribute("specializationsStat")
    public List<SpecializationStat> addBarbersStat(){
        List<Specialization> specializations = specializationRepo.findAll();
        List<SpecializationStat> specializationsStat = new ArrayList<>();
        int allDoneAppointments = appointmentRepo.countByStatus("выполнено") + appointmentRepo.countByStatus("оплачено");
        for(Specialization specialization:specializations){
            specializationsStat.add(new SpecializationStat(specialization.getSpecialization(),
                    (float)(appointmentRepo.countByBarberSpecializationSpecializationAndStatus(specialization.getSpecialization(), "выполнено") +
                            appointmentRepo.countByBarberSpecializationSpecializationAndStatus(specialization.getSpecialization(), "оплачено"))/allDoneAppointments*100));
        }
        return specializationsStat;
    }

    @GetMapping
    public String showPage(){
        return "specializationsStat";
    }
}
