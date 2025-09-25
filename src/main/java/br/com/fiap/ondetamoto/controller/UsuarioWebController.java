package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.model.Usuario;
import br.com.fiap.ondetamoto.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        Page<Usuario> usuarios = usuarioRepository.findAll(PageRequest.of(0, 10));
        model.addAttribute("usuarios", usuarios);
        return "usuario/listar_usuarios";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        // Correção: sempre cria uma nova instância de Usuario para novas entradas
        model.addAttribute("usuario", new Usuario());
        return "usuario/form_usuario";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao salvar o usuário: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
        } else {
            // Retorna para a lista se o usuário não for encontrado
            return "redirect:/usuarios/listar";
        }
        return "usuario/form_usuario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (usuarioRepository.existsById(id)) {
                usuarioRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao excluir o usuário: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }
}