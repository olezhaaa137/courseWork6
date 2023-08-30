package barbershop.web.barber;

import barbershop.entities.Barber;
import barbershop.entities.Specialization;
import barbershop.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BarberSelfInfoForm {


    @Pattern(regexp = "[a-zA-Z]+", message =  "Логин введен неверно")
    private String username;
    private String role = "barber";


    @Pattern(regexp = "[а-яА-Я]+", message =  "Имя введено неверно")
    private String name;


    @Pattern(regexp = "[а-яА-Я]+", message =  "Фамилия введена неверно")
    private String surname;


    @Email(message = "неверная эл. почта")
    private String email;


    @Pattern(regexp = "[+]37544[0-9]{7}|[+]37529[0-9]{7}|[+]37524[0-9]{7}" +
            "|[+]37533[0-9]{7}", message = "Телефон введен неверно!")
    private String phone;


    @Pattern(regexp = "[2-4]\sгода|[1]\sгод|[5-9]\sлет]|[0-9]{2}\\sлет"
            ,message = "Поле опыта работы заполнено неверно")
    private String experience;

    Barber toBarber(String password, Specialization specialization){
        return new Barber(experience, new User(username,password,role,name,surname,email,phone), specialization);
    }
}
