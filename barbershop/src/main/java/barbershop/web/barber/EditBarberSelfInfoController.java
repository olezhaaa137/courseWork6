package barbershop.web.barber;

import barbershop.data.BarberRepository;
import barbershop.data.UserRepository;
import barbershop.entities.Barber;
import barbershop.entities.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/barber/editSelfInfo")
public class EditBarberSelfInfoController {

    private BarberRepository barberRepo;

    private UserRepository userRepo;

    @Autowired
    public EditBarberSelfInfoController(BarberRepository barberRepo,
                                        UserRepository userRepo){
        this.barberRepo = barberRepo;
        this.userRepo = userRepo;
    }

    @ModelAttribute("barberSelfInfoForm")
    public BarberSelfInfoForm addFormToModel(Model model, @AuthenticationPrincipal User user){
        Barber barber = barberRepo.findByUserId(user.getId());
        BarberSelfInfoForm selfInfoForm = new BarberSelfInfoForm();
        selfInfoForm.setUsername(barber.getUser().getUsername());
        selfInfoForm.setName(barber.getUser().getName());
        selfInfoForm.setSurname(barber.getUser().getSurname());
        selfInfoForm.setEmail(barber.getUser().getEmail());
        selfInfoForm.setPhone(barber.getUser().getPhone());
        selfInfoForm.setExperience(barber.getExperience());
        return selfInfoForm;
    }
    @GetMapping
    public String showPage(Model model){

        return "editBarberSelfInfo";
    }

    @PostMapping
    public String processForm(@Valid BarberSelfInfoForm  form, BindingResult result, @AuthenticationPrincipal User user,
                              Errors errors){

        if(errors.hasErrors()){
            return "editBarberSelfInfo";
        }

        if(userRepo.findByUsername(form.getUsername())!=null){
            if(!userRepo.findById(user.getId()).get().getUsername().equals(form.getUsername())){
                String err = "Это имя пользователя уже занято!";
                ObjectError error = new ObjectError("globalError", err);
                result.addError(error);
            }
        }

        if(result.hasErrors()){
            return "editBarberSelfInfo";
        }

        Barber barber = barberRepo.findByUserId(user.getId());

        Barber barber1 = form.toBarber(user.getPassword(), barber.getSpecialization());

        barber1.getUser().setId(user.getId());

        barber1.setId(barber.getId());

        barberRepo.save(barber1);

        return "redirect:/home";
    }

}
