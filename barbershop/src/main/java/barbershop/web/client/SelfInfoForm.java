package barbershop.web.client;

import barbershop.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class SelfInfoForm {

    @NotBlank(message = "Введите логин")
    @Pattern(regexp = "[a-zA-Z]+", message =  "Логин введен неверно")
    private String username;
    private String role = "client";

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
    public User toUser(String password) {
        return new User(username, password, role, name, surname, email, phone);
    }
}
