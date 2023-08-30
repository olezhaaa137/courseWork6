package barbershop.web.client;

import barbershop.data.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/viwAllServices")
public class ViewAllServicesController {

    private SpecializationRepository specializationRepo;

    @Autowired
    public ViewAllServicesController(SpecializationRepository specializationRepo){
        this.specializationRepo = specializationRepo;
    }

    @ModelAttribute
    public void addSpecializationsToModel(Model model){
        model.addAttribute("specializations", specializationRepo.findAll());
    }

    @GetMapping
    public String showPage(){
        return "viewAllServices";
    }
}
