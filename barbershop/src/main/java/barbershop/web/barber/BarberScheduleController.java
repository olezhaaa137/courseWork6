package barbershop.web.barber;

import barbershop.data.AppointmentRepository;
import barbershop.data.BarberRepository;
import barbershop.entities.Appointment;
import barbershop.entities.User;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/barber/viewSchedule")
public class BarberScheduleController {

    private AppointmentRepository appointmentRepo;

    @Autowired
    public BarberScheduleController(AppointmentRepository appointmentRepo){
        this.appointmentRepo = appointmentRepo;
    }

    @ModelAttribute("appointments")
    public List<Appointment> addAppointments(@AuthenticationPrincipal User user){
        return appointmentRepo.findAllByStatusAndBarberUserId(null, user.getId());
    }


    @GetMapping
    public String showPage(){
        return "viewBarberSchedule";
    }

    @GetMapping("/tagAsDone")
    public String tagAppointmentAsDone(@RequestParam(value = "appointmentId", required = false) Long appointmentId){
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        appointment.setStatus("выполнено");
        appointmentRepo.save(appointment);

        return "redirect:/barber/viewSchedule";
    }

    @GetMapping("/tagAsNotDone")
    public String tagAppointmentAsNotDone(@RequestParam(value = "appointmentId", required = false) Long appointmentId){
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        appointment.setStatus("невыполнено");
        appointmentRepo.save(appointment);

        return "redirect:/barber/viewSchedule";
    }
}
