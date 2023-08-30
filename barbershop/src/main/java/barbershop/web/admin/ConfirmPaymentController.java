package barbershop.web.admin;

import barbershop.data.AppointmentRepository;
import barbershop.entities.Appointment;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/confirmPayment")
public class ConfirmPaymentController {

    private AppointmentRepository appointmentRepo;

    @Autowired
    public ConfirmPaymentController(AppointmentRepository appointmentRepo){
        this.appointmentRepo = appointmentRepo;
    }

    @ModelAttribute("appointments")
    public List<Appointment> addAppointments(@AuthenticationPrincipal User user){
        return appointmentRepo.findAllByStatus("выполнено");
    }

    @GetMapping
    public String showPage(){
        return "confirmPayment";
    }

    @GetMapping("/tagAsPayed")
    public String tagAsPayed(@RequestParam(value = "appointmentId", required = false) Long appointmentId){
        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        appointment.setStatus("оплачено");
        appointmentRepo.save(appointment);

        return "redirect:/admin/confirmPayment";
    }
}
