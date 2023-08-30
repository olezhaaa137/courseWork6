package barbershop;

import barbershop.data.SpecializationRepository;
import barbershop.data.UserRepository;
import barbershop.entities.Specialization;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BarbershopApplication {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(BarbershopApplication.class, args);
    }

   /* @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository){
        return args -> {
            userRepository.save(new User("admin", passwordEncoder.encode("admin"), "admin", "Олег", "Буйко", "oleshkabyiko@gmail.com", "+375445720453"));
        };
    }*/

}
