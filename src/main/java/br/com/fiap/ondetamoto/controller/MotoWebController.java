package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.model.Moto;
import br.com.fiap.ondetamoto.model.Setores;
import br.com.fiap.ondetamoto.repository.MotoRepository;
import br.com.fiap.ondetamoto.repository.SetoresRepository;
import br.com.fiap.ondetamoto.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/motos")
public class MotoWebController {

    @Autowired
    private MotoService motoService;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @GetMapping("/listar")
    public String listarMotos(Model model) {
        Page<Moto> motos = motoService.findAllRaw(PageRequest.of(0, 10));
        model.addAttribute("motos", motos);
        return "moto/listar_motos";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("setores", setoresRepository.findAll());
        return "moto/form_moto";
    }

    @PostMapping("/salvar")
    public String salvarMoto(@Valid @ModelAttribute("moto") Moto moto,
                             RedirectAttributes redirectAttributes) {
        try {
            if (moto.getSetores() != null && moto.getSetores().getId() != null) {
                Optional<Setores> setor = setoresRepository.findById(moto.getSetores().getId());
                setor.ifPresent(moto::setSetores);
            }

            motoService.saveRaw(moto);

            redirectAttributes.addFlashAttribute("mensagem", "Moto salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar moto: " + e.getMessage());
        }
        return "redirect:/motos/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Moto> motoOptional = motoService.findByIdRaw(id);
        if (motoOptional.isPresent()) {
            model.addAttribute("moto", motoOptional.get());
            model.addAttribute("setores", setoresRepository.findAll());
        } else {
            return "redirect:/motos/listar";
        }
        return "moto/form_moto";
    }

    @PostMapping("/excluir/{id}")
    public String excluirMoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (motoService.findByIdRaw(id).isPresent()) {
                motoService.deleteById(id);
                redirectAttributes.addFlashAttribute("mensagem", "Moto excluída com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Moto não encontrada.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao excluir a moto: " + e.getMessage());
        }

        return "redirect:/motos/listar";
    }
}
