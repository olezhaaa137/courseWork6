package barbershop.web.admin;

import barbershop.data.UserRepository;
import barbershop.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientByUsernameConverter implements Converter<String, User> {

    private UserRepository userRepo;

    @Autowired
    public ClientByUsernameConverter(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public User convert(String username) {
        return userRepo.findByUsername(username);
    }
}
