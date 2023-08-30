package barbershop.web.client;

import barbershop.data.AppointmentRepository;
import barbershop.data.BarberRepository;
import barbershop.data.SpecializationRepository;
import barbershop.data.TimetableRepository;
import barbershop.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/client/makeAnAppointment")
public class MakeAppointmentController {

    private SpecializationRepository specializationRepo;

    private BarberRepository barberRepo;

    private AppointmentRepository appointmentRepo;

    private TimetableRepository timetableRepo;

    private HashMap<String, String> daysOfWeek = new HashMap<>();

    @Autowired
    public MakeAppointmentController(SpecializationRepository specializationRepo,
                                     BarberRepository barberRepo,
                                     AppointmentRepository appointmentRepo,
                                     TimetableRepository timetableRepo){
        this.specializationRepo = specializationRepo;
        this.barberRepo = barberRepo;
        this.appointmentRepo = appointmentRepo;
        this.timetableRepo = timetableRepo;
        daysOfWeek.put("MONDAY", "Понедельник");
        daysOfWeek.put("TUESDAY", "Вторник");
        daysOfWeek.put("WEDNESDAY", "Среда");
        daysOfWeek.put("THURSDAY", "Четверг");
        daysOfWeek.put("FRIDAY", "Пятница");
    }

    @ModelAttribute("appointment")
    public Appointment addAppointment(){
        return new Appointment();
    }

    @ModelAttribute("appointmentForm")
    public AppointmentForm addAppointmentForm(){
        return new AppointmentForm();
    }

    @ModelAttribute
    public void addSpecializations(Model model){
        List<Specialization> specializationsList = specializationRepo.findAll();
        model.addAttribute("specializations", specializationsList);
        model.addAttribute("price", specializationsList.get(0).getPrice());
    }

    @ModelAttribute("dates")
    public List<LocalDate> addDate(){
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            dates.add(LocalDate.now().plusDays(i));
        }

        List<LocalDate> dates1 = new ArrayList<>();
        for (LocalDate date:dates){
            String day = date.getDayOfWeek().toString();
            if(!date.getDayOfWeek().toString().equals("SATURDAY")&&
            !date.getDayOfWeek().toString().equals("SUNDAY")){
                dates1.add(date);
            }
        }

        return dates1;
    }


    public List<String> getTimes(Barber barber, String date){
        List<Appointment> appointments = appointmentRepo.findAllByBarberUserUsernameAndDate(barber.getUser().getUsername(), date);
        //Timetable timetable = timetableRepo.findByBarberUserUsernameAndDay(barbers.get(0).getUser().getUsername(), daysOfWeek.get(dates.get(0).getDayOfWeek()));
        String[] yearMonthDay = date.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(yearMonthDay[0]), Integer.parseInt(yearMonthDay[1]), Integer.parseInt(yearMonthDay[2]));
        String time = timetableRepo.findByBarberUserUsernameAndDay(barber.getUser().getUsername(), daysOfWeek.get(localDate.getDayOfWeek().toString())).getTime();
        List<String> busyTimes = new ArrayList<>();
        if(appointments!=null){
            for(Appointment appointment:appointments){
                busyTimes.add(appointment.getTime());
            }
        }


        return getFreeTimes(time, busyTimes);
    }

    @ModelAttribute("times")
    public List<String> addTimes(@ModelAttribute("barbers") List<Barber> barbers, @ModelAttribute("dates") List<LocalDate> dates){
        return getTimes(barbers.get(0), dates.get(0).toString());
    }



    @ModelAttribute
    public void addBarbers(Model model,@ModelAttribute("specializations") List<Specialization> specializations){
        List<Barber> barbers = barberRepo.findAllBySpecializationSpecialization(specializations.get(0).getSpecialization());
        model.addAttribute("barbers", barbers);
    }
    @GetMapping
    public String showPage(@ModelAttribute("specializations") List<Specialization> specializations, Model model){


        return "makeAnAppointment";
    }

    @GetMapping("/onChangeBarberUsername")
    public String onChangeBarber(@RequestParam(value = "barber", required = false) String barberUsername,
                                 Model model){
        String service = barberRepo.findByUserUsername(barberUsername).getSpecialization().getSpecialization();

        List<Specialization> specializations = specializationRepo.findAll();
        for (int i = 0; i < specializations.size(); i++) {
            if(specializations.get(i).getSpecialization().equals(service)){
                Specialization temp = specializations.get(0);
                specializations.set(0, specializations.get(i));
                specializations.set(i, temp);
            }
        }

        List<Barber> barbers = barberRepo.findAllBySpecializationSpecialization(specializations.get(0).getSpecialization());
        for (int i = 0; i < barbers.size() ; i++) {
            if(barbers.get(i).getUser().getUsername().equals(barberUsername)){
                Barber temp = barbers.get(0);
                barbers.set(0, barbers.get(i));
                barbers.set(i, temp);
            }

        }


        model.addAttribute("barbers", barbers);

        model.addAttribute("specializations", specializations);
        model.addAttribute("price", specializations.get(0).getPrice());

        List<LocalDate> dates = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            dates.add(LocalDate.now().plusDays(i));
        }

        List<LocalDate> dates1 = new ArrayList<>();
        for (LocalDate date:dates){
            String day = date.getDayOfWeek().toString();
            if(!date.getDayOfWeek().toString().equals("SATURDAY")&&
                    !date.getDayOfWeek().toString().equals("SUNDAY")){
                dates1.add(date);
            }
        }

        model.addAttribute("dates", dates1);
        model.addAttribute("times", getTimes(barbers.get(0), dates1.get(0).toString()));


        return "makeAnAppointment";
    }

    @GetMapping("/onChangeService")
    public String onChangeService(@RequestParam(value = "service", required = false) String service,
                                  @RequestParam(value = "date", required = false) String date
                                    , Model model){
        List<Barber> barbers = barberRepo.findAllBySpecializationSpecialization(service);
        if (barbers.isEmpty()){
            model.addAttribute("times", new ArrayList<>());
            model.addAttribute("dates", new ArrayList<>());
        }
        model.addAttribute("barbers", barbers);
        List<Specialization> specializations = specializationRepo.findAll();
        for (int i = 0; i < specializations.size(); i++) {
            if(specializations.get(i).getSpecialization().equals(service)){
                Specialization temp = specializations.get(0);
                specializations.set(0, specializations.get(i));
                specializations.set(i, temp);
            }
        }
        model.addAttribute("specializations", specializations);
        model.addAttribute("price", specializations.get(0).getPrice());
        if(!barbers.isEmpty()&&!date.isEmpty()){
            model.addAttribute("times", getTimes(barbers.get(0), date));
        }
        if(!barbers.isEmpty()&&date.isEmpty()){
            List<LocalDate> dates = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                dates.add(LocalDate.now().plusDays(i));
            }

            List<LocalDate> dates1 = new ArrayList<>();
            for (LocalDate date1:dates){
                String day = date1.getDayOfWeek().toString();
                if(!date1.getDayOfWeek().toString().equals("SATURDAY")&&
                        !date1.getDayOfWeek().toString().equals("SUNDAY")){
                    dates1.add(date1);
                }
            }
            model.addAttribute("times", getTimes(barbers.get(0), dates1.get(0).toString()));
        }

        return "makeAnAppointment";
    }

    @GetMapping("/onChangeDate")
    public String onChangeDate(@RequestParam(value = "date", required = false) String date, Model model,
                                   @ModelAttribute("dates") List<LocalDate> dates,
                               @RequestParam(value = "barber", required = false) String barberUsername){
        for (int i = 0; i < dates.size(); i++) {
            if(dates.get(i).toString().equals(date)){
                LocalDate temp = dates.get(0);
                dates.set(0, dates.get(i));
                dates.set(i, temp);
            }
        }
        String service = barberRepo.findByUserUsername(barberUsername).getSpecialization().getSpecialization();

        List<Specialization> specializations = specializationRepo.findAll();
        for (int i = 0; i < specializations.size(); i++) {
            if(specializations.get(i).getSpecialization().equals(service)){
                Specialization temp = specializations.get(0);
                specializations.set(0, specializations.get(i));
                specializations.set(i, temp);
            }
        }

        List<Barber> barbers = barberRepo.findAllBySpecializationSpecialization(specializations.get(0).getSpecialization());
        for (int i = 0; i < barbers.size() ; i++) {
            if(barbers.get(i).getUser().getUsername().equals(barberUsername)){
                Barber temp = barbers.get(0);
                barbers.set(0, barbers.get(i));
                barbers.set(i, temp);
            }

        }


        model.addAttribute("barbers", barbers);

        model.addAttribute("specializations", specializations);

        model.addAttribute("price", specializations.get(0).getPrice());

        model.addAttribute("dates", dates);

        model.addAttribute("times", getTimes(barbers.get(0), dates.get(0).toString()));

        barbers = barberRepo.findAllBySpecializationSpecialization(specializations.get(0).getSpecialization());

        for (int i = 0; i < barbers.size() ; i++) {
            if(barbers.get(i).getUser().getUsername().equals(barberUsername)){
                Barber temp = barbers.get(0);
                barbers.set(0, barbers.get(i));
                barbers.set(i, temp);
            }

        }

        return "makeAnAppointment";
    }

    @PostMapping
    public String processAppointment(@AuthenticationPrincipal User user, AppointmentForm form, BindingResult result,
                                     Model model){
        if(form.getDate() == null || form.getTime()==null || form.getBarberUsername()==null){
            String err = "Пожалуйста, заполните все поля";
            ObjectError error = new ObjectError("globalError2", err);
            result.addError(error);
            model.addAttribute("specializations", specializationRepo.findAll());
            if(form.getBarberUsername() == null){
                model.addAttribute("barbers", new ArrayList<>());
            }
            if(form.getDate() == null){
                model.addAttribute("dates", new ArrayList<>());
            }
            if(form.getTime() == null){
                model.addAttribute("times", new ArrayList<>());
            }
            return "makeAnAppointment";
        }
        Appointment appointment = form.convertToAppointment(barberRepo, user, form.getDate(), form.getTime(), form.getBarberUsername());
        appointmentRepo.save(appointment);
        return "redirect:/home";
    }

    public List<String> getFreeTimes(String time, List<String> busyTimes){
        String [] times = time.split("-");
        /*if(time == null){
            System.out.println("saturday or sunday");
        }*/
        LocalTime localTime1 = LocalTime.parse(times[0], DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime localTime2 = LocalTime.parse(times[1], DateTimeFormatter.ofPattern("HH:mm"));
        List<String> timesToAdd = new ArrayList<>();
        while (!localTime1.equals(localTime2)){
            int flag = 0;
            for(String busyTime:busyTimes){
                if ((localTime1 + "-" + localTime1.plusMinutes(30)).equals(busyTime)) {
                    flag = 1;
                    break;
                }
            }
            if(flag == 1){
                localTime1 = localTime1.plusMinutes(60);
                continue;
            }else{
                timesToAdd.add(localTime1 + "-" + localTime1.plusMinutes(30));
                localTime1 = localTime1.plusMinutes(60);
            }
        }
        return timesToAdd;
    }
}
