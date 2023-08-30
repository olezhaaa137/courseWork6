package barbershop.security;


import barbershop.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;

@Data
public class RegistrationForm {

    @Pattern(regexp = "[a-zA-Z]+", message =  "Логин введен неверно")
    private String username;
    @NotBlank(message = "Введите пароль")
    private String password;
    private String role = "client";


    @Pattern(regexp = "[а-яА-Я]+", message =  "Имя введено неверно")
    private String name;

    @Pattern(regexp = "[а-яА-Я]+", message =  "Фамилия введена неверно")
    private String surname;

    @NotBlank(message = "введите эл. почту")
    @Email(message = "неверная эл. почта")
    private String email;


    @Pattern(regexp = "[+]37544[0-9]{7}|[+]37529[0-9]{7}|[+]37524[0-9]{7}" +
            "|[+]37533[0-9]{7}", message = "Телефон введен неверно!")
    private String phone;
    public User toUser(PasswordEncoder passwordEncoder) {
        return  User.builder().username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .name(name)
                .surname(surname)
                .email(email)
                .phone(phone).build();
    }
}
