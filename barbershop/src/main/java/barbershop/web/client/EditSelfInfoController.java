package barbershop.web.client;

import barbershop.data.UserRepository;
import barbershop.entities.User;
import barbershop.security.RegistrationForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/client/editSelfInfo")
public class EditSelfInfoController {
    private UserRepository userRepo;

    @Autowired
    public EditSelfInfoController(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @GetMapping
    public String showPageForEditing(Model model, @AuthenticationPrincipal User user){
        User user1 = userRepo.findById(user.getId()).get();
        SelfInfoForm selfInfoForm = new SelfInfoForm();
        selfInfoForm.setUsername(user1.getUsername());
        selfInfoForm.setName(user1.getName());
        selfInfoForm.setSurname(user1.getSurname());
        selfInfoForm.setPhone(user1.getPhone());
        selfInfoForm.setEmail(user1.getEmail());
        model.addAttribute("selfInfoForm", selfInfoForm);
        return "editClientSelfInfo";
    }

    @PostMapping
    public String processForm(@Valid SelfInfoForm form, BindingResult result,@AuthenticationPrincipal User user,
                              Errors errors){
        if(errors.hasErrors()){
            return "editClientSelfInfo";
        }

        if(userRepo.findByUsername(form.getUsername())!=null){
            if(!userRepo.findById(user.getId()).get().getUsername().equals(form.getUsername())){
                String err = "Это имя пользователя уже занято!";
                ObjectError error = new ObjectError("globalError", err);
                result.addError(error);
            }
        }

        if(result.hasErrors()){
            return "editClientSelfInfo";
        }
        User user1 = form.toUser(user.getPassword());
        user1.setId(user.getId());
        userRepo.save(user1);

        return "redirect:/home";

    }
}
