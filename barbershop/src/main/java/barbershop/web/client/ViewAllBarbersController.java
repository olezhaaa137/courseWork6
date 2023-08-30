package barbershop.web.client;


import barbershop.data.BarberRepository;
import barbershop.data.UserRepository;
import barbershop.entities.Barber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/client/viewAllBarbers")
public class ViewAllBarbersController {

    private BarberRepository barberRepo;

    @Autowired
    public ViewAllBarbersController(BarberRepository barberRepo){
        this.barberRepo = barberRepo;
    }

    @ModelAttribute()
    public void addBarbersToModel(Model model){
        model.addAttribute("barbers", barberRepo.findAllByUserRole("barber"));
    }

    @GetMapping
    public String showPage(){
        return "viewAllBarbers";
    }

    @GetMapping("/sort")
    public String sort(@RequestParam(value = "sortType", required = false) String sortType
            , @ModelAttribute("barbers") List<Barber> barbers){
        Comparator<Barber> comparator = null;
        if(sortType.equals("")){
            return "viewAllBarbers";
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

        return "viewAllBarbers";
    }
}
