package barbershop.web.admin;

import barbershop.data.AppointmentRepository;
import barbershop.data.BarberRepository;
import barbershop.data.SpecializationRepository;
import barbershop.data.TimetableRepository;
import barbershop.entities.Appointment;
import barbershop.entities.Barber;
import barbershop.entities.Specialization;
import barbershop.entities.Timetable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class SpecializationController {

    private SpecializationRepository specializationRepo;

    private BarberRepository barberRepository;

    private TimetableRepository timetableRepo;

    private AppointmentRepository appointmentRepo;

    @Autowired
    public SpecializationController(SpecializationRepository specializationRepo,
                                    BarberRepository barberRepository,
                                    TimetableRepository timetableRepo,
                                    AppointmentRepository appointmentRepo){

        this.specializationRepo = specializationRepo;
        this.barberRepository = barberRepository;
        this.timetableRepo = timetableRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @ModelAttribute(name = "specialization")
    public Specialization addSpecializationToModel(){
        return new Specialization();
    }

    @ModelAttribute
    public void addSpecializationsToModel(Model model){
        model.addAttribute("specializations", specializationRepo.findAll());
    }

    @GetMapping
    public String showMainPage(){
        return "redirect:/home";
    }

    @GetMapping("/addSpecialization")
    public String showPageToAddSpecialization(){
        return "addSpecialization";
    }

    @GetMapping("/viewAllSpecializations")
    public String showSpecializations(){
        return "specializations";
    }

    @PostMapping("/addSpecialization")
    public String processSpecialization(@Valid Specialization specialization, Errors errors, BindingResult result){
        if(errors.hasErrors()){
            return "addSpecialization";
        }

        if(specializationRepo.findSpecializationBySpecialization(specialization.getSpecialization())!=null){
            String err = "Такая услуга уже есть!";
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }

        if(result.hasErrors()){
            return "addSpecialization";
        }

        specializationRepo.save(specialization);
        return "redirect:/home";
    }

    @GetMapping("/deleteSpecialization")
    public String deleteSpecialization(Long id){

        if(id!=null){
            String specialization = specializationRepo.findById(id).get().getSpecialization();
            List<Barber> barbers = barberRepository.findAllBySpecializationSpecialization(specialization);
            for(Barber barber: barbers){
                List<Timetable> timetables = timetableRepo.findAllByBarberId(barber.getId());
                timetableRepo.deleteAll(timetables);
                List<Appointment> appointments = appointmentRepo.findAllByBarberId(barber.getId());
                appointmentRepo.deleteAll(appointments);
                barberRepository.deleteById(barber.getId());
            }
            specializationRepo.deleteById(id);
        }


        return "redirect:/admin/viewAllSpecializations";
    }

    @GetMapping("/viewAllSpecializations/search")
    public String searchByName(@RequestParam(value = "name", required = false) String name,
                               Model model){
        if(name.isEmpty()){
            return "specializations";
        }

        List<Specialization> specializations = specializationRepo.findAllBySpecializationLike("%" + name + "%");
        model.addAttribute("specializations", specializations);

        return "specializations";
    }

}
