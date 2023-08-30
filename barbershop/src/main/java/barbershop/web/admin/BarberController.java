package barbershop.web.admin;

import barbershop.data.*;
import barbershop.entities.*;
import barbershop.security.RegistrationForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/manageBarbers")
public class BarberController {

    private BarberRepository barberRepo;
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepo;

    private SpecializationRepository specializationRepo;

    private TimetableRepository timetableRepo;

    private AppointmentRepository appointmentRepo;

    @Autowired
    public BarberController(BarberRepository barberRepo, PasswordEncoder passwordEncoder
    ,SpecializationRepository specializationRepo, UserRepository userRepo, TimetableRepository timetableRepo,
                            AppointmentRepository appointmentRepo){
        this.barberRepo = barberRepo;
        this.passwordEncoder = passwordEncoder;
        this.specializationRepo = specializationRepo;
        this.userRepo = userRepo;
        this.timetableRepo = timetableRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @ModelAttribute
    public void addUsersToModel(Model model){
        model.addAttribute("barbers", barberRepo.findAllByUserRole("barber"));
    }

    @ModelAttribute(name = "formToAddBarber")
    public FormToAddBarber form(){return new FormToAddBarber();}

    @ModelAttribute
    public void addSpecializations(Model model){
        model.addAttribute("specializations", specializationRepo.findAll());
    }

    @GetMapping
    public String showUsers(){
        return "barbers";
    }

    @GetMapping("/addBarber")
    public String showPageForAddUser(){
        return "addBarber";
    }

    @PostMapping("/addBarber")
    public String processBarber(@Valid FormToAddBarber form, Errors errors, BindingResult result){


        if(errors.hasErrors()){
            for (int i = 0;i<form.getTimetables().size(); i++){
                if (errors.hasFieldErrors("timetables[" + i + "].time")){
                    String err = "Пожалуйста, введите время в формате (07:00-15:00). Помните, что продолжительность рабочего дня\n не должна превышать восьми часов!";
                    ObjectError error = new ObjectError("globalError2", err);
                    result.addError(error);
                    break;
                }
            }

            return "addBarber";
        }

        if(userRepo.findByUsername(form.getUsername())!=null){
            String err = "Это имя пользователя уже занято!";
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }

        for (int i = 0;i<form.getTimetables().size(); i++){
            if(countDifferenceBetweenTime(form.getTimetables().get(i).getTime())<0||
                    countDifferenceBetweenTime(form.getTimetables().get(i).getTime())>28800000){
                String err = "Пожалуйста, введите время в формате (07:00-15:00). Помните, что продолжительность рабочего дня\n не должна превышать восьми часов!";
                ObjectError error = new ObjectError("globalError1", err);
                result.addError(error);
                break;
            }
        }


        if(result.hasErrors()){
            return "addBarber";
        }
        Barber barber = form.toBarber(passwordEncoder);
        barberRepo.save(barber);
        for(Timetable timetable:form.getTimetables()){
            timetable.setBarber(barber);
            timetableRepo.save(timetable);
        }


        return "redirect:/admin/manageBarbers";
    }

    @GetMapping("/deleteBarber")
    public String deleteBarber(Long id){

        if(id!=null){
            List<Timetable> timetables = timetableRepo.findAllByBarberId(id);
            timetableRepo.deleteAll(timetables);
            List<Appointment> appointments = appointmentRepo.findAllByBarberId(id);
            appointmentRepo.deleteAll(appointments);
            barberRepo.deleteById(id);
        }


        return "redirect:/admin/manageBarbers";
    }

    @GetMapping("/setSchedule")
    public String setBarberSchedule(Long id, Model model){
        model.addAttribute("barberToSetSchedule", barberRepo.findById(id));
        ScheduleForm scheduleForm = new ScheduleForm();
        scheduleForm.setTimetables(timetableRepo.findAllByBarberId(id));
        model.addAttribute("scheduleForm", scheduleForm);
        return "setBarberSchedule";
    }

    @PostMapping("/setSchedule")
    public String processScheduleForm(@ModelAttribute ScheduleForm scheduleForm, BindingResult result, Model model){
        for(Timetable timetable: scheduleForm.getTimetables()){
            if(timetable.getTime().equals(null)||!timetable.getTime().matches("[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2}")||
                countDifferenceBetweenTime(timetable.getTime())<0||countDifferenceBetweenTime(timetable.getTime())>28800000){
                String err = "Пожалуйста, введите время в формате (07:00-15:00). Помните, что продолжительность рабочего дня\n не должна превышать восьми часов!";
                ObjectError error = new ObjectError("globalError2", err);
                result.addError(error);
                break;
            }
        }

        if(result.hasErrors()){
            return "setBarberSchedule";
        }

        Barber barber = (Barber)model.getAttribute("barberToSetSchedule");

        for(Timetable timetable:scheduleForm.getTimetables()){
            timetable.setBarber(timetableRepo.findById(timetable.getId()).get().getBarber());
            timetableRepo.save(timetable);
        }

        return "redirect:/admin/manageBarbers";
    }

    @GetMapping("/sort")
    public String sort(@RequestParam(value = "sortType", required = false) String sortType
            , @ModelAttribute("barbers") List<Barber> barbers){
        Comparator<Barber> comparator = null;
        if(sortType.equals("")){
            return "barbers";
        }
        if(sortType.equals("name")){
            comparator = new Comparator<Barber>() {
                @Override
                public int compare(Barber o1, Barber o2) {
                    return o1.getUser().getName().compareTo(o2.getUser().getName());
                }
            };
        }

        if(sortType.equals("surname")){
            comparator = new Comparator<Barber>() {
                @Override
                public int compare(Barber o1, Barber o2) {
                    return o1.getUser().getSurname().compareTo(o2.getUser().getSurname());
                }
            };
        }

        if (sortType.equals("username")){
            comparator = new Comparator<Barber>() {
                @Override
                public int compare(Barber o1, Barber o2) {
                    return o1.getUser().getUsername().compareTo(o2.getUser().getUsername());
                }
            };
        }

        barbers.sort(comparator);

        return "barbers";
    }

    public long countDifferenceBetweenTime(String time){
        String []times = getTwoTimes(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try{
            Date date1 = simpleDateFormat.parse(times[0]);
            Date date2 = simpleDateFormat.parse(times[1]);
            return (date2.getTime() - date1.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public String[] getTwoTimes(String unsplitted){
        return unsplitted.split("-");
    }
}
