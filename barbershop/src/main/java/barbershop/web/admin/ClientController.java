package barbershop.web.admin;

import barbershop.data.AppointmentRepository;
import barbershop.data.UserRepository;
import barbershop.entities.Appointment;
import barbershop.entities.User;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/manageClients")
public class ClientController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    private AppointmentRepository appointmentRepo;

    @Autowired
    public ClientController(UserRepository userRepo, PasswordEncoder passwordEncoder,
                            AppointmentRepository appointmentRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepo = appointmentRepo;
    }

    @ModelAttribute
    public void addUsersToModel(Model model){
        model.addAttribute("clients", userRepo.findAllByRole("client"));
    }

    @ModelAttribute(name = "registrationForm")
    public RegistrationForm form(){return new RegistrationForm();}

    @GetMapping
    public String showUsers(){
        return "clients";
    }

    @GetMapping("/addClient")
    public String showPageForAddUser(){
        return "addUser";
    }

    @PostMapping("/addClient")
    public String processClient(@Valid RegistrationForm form, Errors errors, BindingResult result){
        if(errors.hasErrors()){
            return "addUser";
        }

        if(userRepo.findByUsername(form.getUsername())!=null){
            String err = "Это имя пользователя уже занято!";
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }

        if(result.hasErrors()){
            return "addUser";
        }

        userRepo.save(form.toUser(passwordEncoder));
        return "redirect:/admin/manageClients";
    }

    @GetMapping("/deleteClient")
    public String deleteClient(Long id){

        if(id!=null){
            List<Appointment> appointments = appointmentRepo.findAllByUserId(id);
            appointmentRepo.deleteAll(appointments);
            userRepo.deleteById(id);
        }


        return "redirect:/admin/manageClients";
    }

    @GetMapping("/sort")
    public String sort(@RequestParam(value = "sortType", required = false) String sortType
    ,@ModelAttribute("clients") List<User> clients){
        Comparator<User> comparator = null;
        if(sortType.equals("")){
            return "clients";
        }
        if(sortType.equals("name")){
                comparator = new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }

        if(sortType.equals("surname")){
            comparator = new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getSurname().compareTo(o2.getSurname());
                }
            };
        }

        if (sortType.equals("username")){
            comparator = new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            };
        }

        clients.sort(comparator);

        return "clients";
    }
}
