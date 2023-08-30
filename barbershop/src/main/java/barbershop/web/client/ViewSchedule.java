package barbershop.web.client;

import barbershop.data.AppointmentRepository;
import barbershop.data.UserRepository;
import barbershop.entities.Appointment;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/client/viewSchedule")
public class ViewSchedule {

    private AppointmentRepository appointmentRepo;

    private UserRepository userRepo;

    @Autowired
    public ViewSchedule(AppointmentRepository appointmentRepo,
                        UserRepository userRepo){
        this.appointmentRepo = appointmentRepo;
        this.userRepo = userRepo;
    }

    @ModelAttribute
    public void addAppointmentsToModel(Model model, @AuthenticationPrincipal User user){
        User user1 = userRepo.findById(user.getId()).get();
        List<Appointment> appointments = appointmentRepo.findAllByUserUsernameAndStatus(user1.getUsername(), null);
        model.addAttribute("appointments", appointments);
    }

    @GetMapping
    public String showPage(){
        return "viewClientSchedule";
    }
}


