package barbershop.web.admin;

import barbershop.entities.Barber;
import barbershop.entities.Specialization;
import barbershop.entities.Timetable;
import barbershop.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Data
public class FormToAddBarber {

    public FormToAddBarber(){
        this.timetables = new ArrayList<>();
        Timetable timetable1 = new Timetable();
        Timetable timetable2 = new Timetable();
        Timetable timetable3 = new Timetable();
        Timetable timetable4 = new Timetable();
        Timetable timetable5 = new Timetable();
        timetable1.setDay("Понедельник");
        timetable2.setDay("Вторник");
        timetable3.setDay("Среда");
        timetable4.setDay("Четверг");
        timetable5.setDay("Пятница");
        this.timetables.add(timetable1);
        this.timetables.add(timetable2);
        this.timetables.add(timetable3);
        this.timetables.add(timetable4);
        this.timetables.add(timetable5);
    }

    @NotBlank(message = "Введите логин")
    @Pattern(regexp = "[a-zA-Z]+", message =  "Логин введен неверно")
    private String username;
    @NotBlank(message = "Введите пароль")
    private String password;
    private String role = "barber";

    @NotBlank(message = "Введите имя")
    @Pattern(regexp = "[а-яА-Я]+", message =  "Имя введено неверно")
    private String name;

    @NotBlank(message = "Введите фамилию")
    @Pattern(regexp = "[а-яА-Я]+", message =  "Фамилия введена неверно")
    private String surname;

    @NotBlank(message = "Введите почту")
    @Email(message = "неверная эл. почта")
    private String email;

    @NotBlank(message = "Введите телефон")
    @Pattern(regexp = "[+]37544[0-9]{7}|[+]37529[0-9]{7}|[+]37524[0-9]{7}" +
            "|[+]37533[0-9]{7}", message = "Телефон введен неверно!")
    private String phone;

    @NotBlank(message = ("поле \"Опыт работы\" не заполнено"))
    @Pattern(regexp = "[2-4]\sгода|[1]\sгод|[5-9]\sлет]|[0-9]{2}\\sлет"
            ,message = "Поле опыта работы заполнено неверно")
    private String experience;

    private Specialization specialization;

    @Valid
    private List<Timetable> timetables;

    public Barber toBarber(PasswordEncoder passwordEncoder){
        return new Barber(experience, new User(username,passwordEncoder.encode(password),role,name,surname,email,phone), specialization);
    }
}
