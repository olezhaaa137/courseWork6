package barbershop.web.admin;


import barbershop.data.AppointmentRepository;
import barbershop.data.BarberRepository;
import barbershop.data.UserRepository;
import barbershop.entities.Barber;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/viewBarbersStat")
public class BarbersStatController {

    private UserRepository userRepo;
    private AppointmentRepository appointmentRepo;

    @Autowired
    public BarbersStatController(UserRepository userRepo,
                                 AppointmentRepository appointmentRepo){
        this.userRepo = userRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @ModelAttribute("barbersStat")
    public List<BarberStat> addBarbersStat(){
        List<User> barbers = userRepo.findAllByRole("barber");
        List<BarberStat> barbersStat = new ArrayList<>();
        for(User barber:barbers){
            barbersStat.add(new BarberStat(barber.getName(), barber.getSurname(),
                    appointmentRepo.countByBarberUserUsernameAndStatus(barber.getUsername(), "выполнено") +
                            appointmentRepo.countByBarberUserUsernameAndStatus(barber.getUsername(), "оплачено")));
        }
        return barbersStat;
    }

    @GetMapping
    public String showPage(){
        return "barbersStat";
    }
}
