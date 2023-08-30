package barbershop.web.barber;


import barbershop.data.UserRepository;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/barber/viewAllClients")
public class ViewAllClientsController {

    private UserRepository userRepo;

    @Autowired
    public ViewAllClientsController(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @ModelAttribute("clients")
    public List<User> addClientsToModel(){
        return userRepo.findAllByRole("client");
    }

    @GetMapping
    public String showPage(){
        return "viewAllClients";
    }
}
