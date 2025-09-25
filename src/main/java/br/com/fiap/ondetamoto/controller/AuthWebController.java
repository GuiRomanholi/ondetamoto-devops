package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.dto.RegisterDTO;
import br.com.fiap.ondetamoto.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@RequestMapping("/")
public class AuthWebController {

    private final String REGISTER_API_URL = "http://localhost:8081/api/auth/register";

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Email ou senha inválidos.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO(null, null, null));
        model.addAttribute("allowedRoles", Arrays.asList(UserRole.values()));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDTO") RegisterDTO registerDTO, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    REGISTER_API_URL,
                    new org.springframework.http.HttpEntity<>(registerDTO, headers),
                    Void.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("successMessage", "Usuário registrado com sucesso! Agora você pode fazer login.");
                return "redirect:/login";
            } else {
                model.addAttribute("errorMessage", "Ocorreu um erro inesperado durante o registro. Código de status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                if (errorBody != null && !errorBody.isEmpty() && errorBody.contains("Email já cadastrado")) {
                    model.addAttribute("errorMessage", "Email já cadastrado. Por favor, escolha outro.");
                } else if (errorBody != null && !errorBody.isEmpty() && errorBody.contains("Role inválida")) {
                    model.addAttribute("errorMessage", "Role inválida. Por favor, selecione uma role válida.");
                } else {
                    model.addAttribute("errorMessage", "Erro na requisição: " + errorBody);
                }
            } else {
                model.addAttribute("errorMessage", "Erro ao registrar usuário: " + e.getStatusCode() + " - " + errorBody);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao se conectar com o servidor: " + e.getMessage());
        }

        model.addAttribute("allowedRoles", Arrays.asList(UserRole.values()));
        return "register";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}