package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.repository.EstabelecimentoRepository;
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
@RequestMapping("/estabelecimentos")
public class EstabelecimentoWebController {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @GetMapping("/listar")
    public String listarEstabelecimentos(Model model) {
        Page<Estabelecimento> estabelecimentos = estabelecimentoRepository.findAll(PageRequest.of(0, 10));
        model.addAttribute("estabelecimentos", estabelecimentos);
        return "estabelecimento/listar_estabelecimentos";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        // Correção: sempre cria uma nova instância de Estabelecimento para novas entradas
        model.addAttribute("estabelecimento", new Estabelecimento());
        return "estabelecimento/form_estabelecimento";
    }

    @PostMapping("/salvar")
    public String salvarEstabelecimento(@Valid @ModelAttribute("estabelecimento") Estabelecimento estabelecimento, RedirectAttributes redirectAttributes) {
        try {
            estabelecimentoRepository.save(estabelecimento);
            redirectAttributes.addFlashAttribute("mensagem", "Estabelecimento salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao salvar o estabelecimento: " + e.getMessage());
        }
        return "redirect:/estabelecimentos/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Estabelecimento> estabelecimentoOptional = estabelecimentoRepository.findById(id);
        if (estabelecimentoOptional.isPresent()) {
            model.addAttribute("estabelecimento", estabelecimentoOptional.get());
        } else {
            // Retorna para a lista se o estabelecimento não for encontrado
            return "redirect:/estabelecimentos/listar";
        }
        return "estabelecimento/form_estabelecimento";
    }

    @GetMapping("/excluir/{id}")
    public String excluirEstabelecimento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (estabelecimentoRepository.existsById(id)) {
                estabelecimentoRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("mensagem", "Estabelecimento excluído com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Estabelecimento não encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao excluir o estabelecimento: " + e.getMessage());
        }
        return "redirect:/estabelecimentos/listar";
    }
}