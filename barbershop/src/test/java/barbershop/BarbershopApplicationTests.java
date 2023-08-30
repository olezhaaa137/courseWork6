package barbershop;

import barbershop.web.admin.BarberController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class BarbershopApplicationTests {

    @Autowired
    BarberController barberController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    public void testBarberControllerIsNotNull() throws Exception{
        assertThat(barberController).isNotNull();
    }

    @Test
    public void testThatLoginPageContainsString() throws Exception{
        this.mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Вход")));
    }

    @Test
    public void testThatRegistrationPageContainsString() throws Exception{
        this.mockMvc.perform(get("/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Регистрация")));
    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void loginAdminTest() throws Exception{
        this.mockMvc.perform(formLogin().user("admin").password("admin"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void loginClientTest() throws Exception{
        this.mockMvc.perform(formLogin().user("client").password("client"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void loginBarberTest() throws Exception{
        this.mockMvc.perform(formLogin().user("barber").password("barber"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("username", "jonh"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRegister() throws Exception {
       this.mockMvc.perform(multipart("/register").param("username", "admin")
               .param("password", "admin")
               .param("name", "фыва")
               .param("surname", "фыва")
               .param("email", "olesh@gmail.com")
               .param("phone", "+375445720453").with(csrf()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Это имя пользователя уже занято!")));
    }

    @Test
    public void testLoginWithNullUsername() throws Exception{
        this.mockMvc.perform(formLogin().user("").password("client"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void testLoginWithNullPassword() throws Exception{
        this.mockMvc.perform(formLogin().user("client").password(""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }


    @Test
    public void testRegisterWithNullFields() throws Exception {
        this.mockMvc.perform(multipart("/register").param("username", "")
                        .param("password", "admin")
                        .param("name", "фыва")
                        .param("surname", "фыва")
                        .param("email", "olesh@gmail.com")
                        .param("phone", "+375445720453").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Логин введен неверно")));
    }

    @Test
    public void testRegisterWithNullPassword() throws Exception {
        this.mockMvc.perform(multipart("/register").param("username", "asdf")
                        .param("password", "")
                        .param("name", "фыва")
                        .param("surname", "фыва")
                        .param("email", "olesh@gmail.com")
                        .param("phone", "+375445720453").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Введите пароль")));
    }

    @Test
    public void testRegisterWithNullName() throws Exception {
        this.mockMvc.perform(multipart("/register").param("username", "asdf")
                        .param("password", "asdf")
                        .param("name", "")
                        .param("surname", "фыва")
                        .param("email", "olesh@gmail.com")
                        .param("phone", "+375445720453").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Имя введено неверно")));
    }

    @Test
    public void testRegisterWithNullSurname() throws Exception {
        this.mockMvc.perform(multipart("/register").param("username", "asdf")
                        .param("password", "asdf")
                        .param("name", "фыва")
                        .param("surname", "")
                        .param("email", "olesh@gmail.com")
                        .param("phone", "+375445720453").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Фамилия введена неверно")));
    }

    @Test
    public void testRegisterWithNullEmail() throws Exception {
        this.mockMvc.perform(multipart("/register").param("username", "asdf")
                        .param("password", "asdf")
                        .param("name", "фыва")
                        .param("surname", "фыва")
                        .param("email", "")
                        .param("phone", "+375445720453").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("введите эл. почту")));
    }

    @Test
    public void testRegisterWithNullPhone() throws Exception {
        this.mockMvc.perform(multipart("/register").param("username", "asdf")
                        .param("password", "asdf")
                        .param("name", "фыва")
                        .param("surname", "фыва")
                        .param("email", "oleshka@gmail.com")
                        .param("phone", "").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Телефон введен неверно!")));
    }

    @Test
    public void testLoginPage() throws Exception{
        this.mockMvc.perform(get("/login"))
                .andExpect(view().name("login"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEmptyPage() throws Exception{
        this.mockMvc.perform(get("/"))
                .andExpect(view().name("emptyPage"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterPage() throws Exception{
        this.mockMvc.perform(get("/register"))
                .andExpect(view().name("registration"))
                .andExpect(status().isOk());
    }



}
