package barbershop.web.admin;

import barbershop.data.AppointmentRepository;
import barbershop.entities.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/viewEarningsStat")
public class EarningsStatController {

    private AppointmentRepository appointmentRepo;

    @Autowired
    public EarningsStatController(AppointmentRepository appointmentRepo){
        this.appointmentRepo = appointmentRepo;
    }

    @ModelAttribute("earningsStat")
    public List<EarningStat> addEarningsStatToModel(){
        LocalDate localDate = LocalDate.now();
        String month = String.valueOf(localDate.getMonthValue());
        String year = String.valueOf(localDate.getYear());
        int two_month_ago_value = Integer.parseInt(month) -2;
        int month_ago_value = two_month_ago_value + 1;
        int month_value = two_month_ago_value + 2;
        List<Appointment> appointments = appointmentRepo.findAllByStatusAndDateLike("выполнено",
                year + "-%" + two_month_ago_value + "-" + "%");
        appointments.addAll(appointmentRepo.findAllByStatusAndDateLike("оплачено",
                year + "-%" + two_month_ago_value + "-" + "%"));
        List<EarningStat> earningsStat = new ArrayList<>();
        float earnings = 0;
        for (Appointment appointment:appointments){
            earnings+=Float.parseFloat(appointment.getBarber().getSpecialization().getPrice());
        }

        earningsStat.add(new EarningStat("Два месяца назад", earnings));


        appointments = appointmentRepo.findAllByStatusAndDateLike("выполнено",
                year + "-%" + month_ago_value + "-" + "%");
        appointments.addAll(appointmentRepo.findAllByStatusAndDateLike("оплачено",
                year + "-%" + month_ago_value + "-" + "%"));
        earnings = 0;
        for (Appointment appointment:appointments){
            earnings+=Float.parseFloat(appointment.getBarber().getSpecialization().getPrice());
        }

        earningsStat.add(new EarningStat("Месяц назад", earnings));


        appointments = appointmentRepo.findAllByStatusAndDateLike("выполнено",
                year + "-%" + month_value + "-" + "%");
        appointments.addAll(appointmentRepo.findAllByStatusAndDateLike("оплачено",
                year + "-%" + month_value + "-" + "%"));
        earnings = 0;
        for (Appointment appointment:appointments){
            earnings+=Float.parseFloat(appointment.getBarber().getSpecialization().getPrice());
        }

        earningsStat.add(new EarningStat("В этом месяце", earnings));

        return earningsStat;
    }

    @GetMapping
    public String showPage(){
        return "earningsStat";
    }


}
