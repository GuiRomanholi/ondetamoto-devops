package br.com.fiap.ondetamoto.service;

import br.com.fiap.ondetamoto.controller.EstabelecimentoController;
import br.com.fiap.ondetamoto.controller.SetoresController;
import br.com.fiap.ondetamoto.dto.EstabelecimentoResponse;
import br.com.fiap.ondetamoto.dto.SetoresRequest;
import br.com.fiap.ondetamoto.dto.SetoresResponse;
import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.model.Setores;
import br.com.fiap.ondetamoto.repository.EstabelecimentoRepository;
import br.com.fiap.ondetamoto.repository.SetoresRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SetoresService {
    private final SetoresRepository setoresRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;

    public SetoresService(SetoresRepository setoresRepository, EstabelecimentoRepository estabelecimentoRepository) {
        this.setoresRepository = setoresRepository;
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    public Setores requestToSetores(SetoresRequest setoresRequest) {
        Setores setores = new Setores();
        setores.setNome(setoresRequest.getNome());
        setores.setTipo(setoresRequest.getTipo());
        setores.setTamanho(setoresRequest.getTamanho());

        if (setoresRequest.getIdEstabelecimento() != null) {
            Optional<Estabelecimento> estabelecimento = estabelecimentoRepository.findById(setoresRequest.getIdEstabelecimento());
            estabelecimento.ifPresent(setores::setEstabelecimento);
        }

        return setores;
    }

    public SetoresResponse setoresToResponse(Setores setores, boolean self) {
        Link link;
        if (self) {
            link = linkTo(methodOn(SetoresController.class).readSetor(setores.getId())).withSelfRel();
        } else {
            link = linkTo(methodOn(SetoresController.class).readSetores(0)).withRel("Lista de Setores");
        }

        EstabelecimentoResponse estabelecimentoResponse = null;
        if (setores.getEstabelecimento() != null) {
            Estabelecimento est = setores.getEstabelecimento();
            Link estLink = linkTo(methodOn(EstabelecimentoController.class).readEstabelecimento(est.getId())).withSelfRel();
            estabelecimentoResponse = new EstabelecimentoResponse(est.getId(), est.getEndereco(), estLink);
        }

        return new SetoresResponse(
                setores.getId(),
                setores.getNome(),
                setores.getTipo(),
                setores.getTamanho(),
                estabelecimentoResponse,
                link
        );
    }

    // 🔹 Retorna entidade pura para usar no Thymeleaf
    public Page<Setores> findAllRaw(Pageable pageable) {
        return setoresRepository.findAll(pageable);
    }

    public Optional<Setores> findByIdRaw(Long id) {
        return setoresRepository.findById(id);
    }

    @CacheEvict(value = {"setor", "setores"}, allEntries = true)
    public Setores saveRaw(Setores setor) {
        return setoresRepository.save(setor);
    }

    public List<SetoresResponse> setoresToResponse(List<Setores> setores) {
        List<SetoresResponse> setoresResponse = new ArrayList<>();
        for (Setores setor : setores) {
            setoresResponse.add(setoresToResponse(setor, true));
        }
        return setoresResponse;
    }

    @Cacheable(value = "setores", key = "#pageable.pageNumber")
    public Page<SetoresResponse> findAll(Pageable pageable) {
        return setoresRepository.findAll(pageable)
                .map(setor -> setoresToResponse(setor, true));
    }

    @Cacheable(value = "setor", key = "#id")
    public SetoresResponse findById(Long id) {
        Setores setor = setoresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Setor não encontrado"));
        return setoresToResponse(setor, false);
    }

    @CacheEvict(value = {"setor", "setores"}, allEntries = true)
    public void deleteById(Long id) {
        setoresRepository.deleteById(id);
    }

    @CacheEvict(value = {"setor", "setores"}, allEntries = true)
    public SetoresResponse updateSetor(Long id, SetoresRequest request) {
        Setores setorExistente = setoresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Setor não encontrado"));

        if (request.getNome() != null) setorExistente.setNome(request.getNome());
        if (request.getTipo() != null) setorExistente.setTipo(request.getTipo());
        if (request.getTamanho() != null) setorExistente.setTamanho(request.getTamanho());

        if (request.getIdEstabelecimento() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(request.getIdEstabelecimento())
                    .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));
            setorExistente.setEstabelecimento(estabelecimento);
        }

        Setores setorAtualizado = setoresRepository.save(setorExistente);
        return setoresToResponse(setorAtualizado, false);
    }

    @CacheEvict(value = "setores", allEntries = true)
    public SetoresResponse createSetor(SetoresRequest setoresRequest) {
        Setores setores = requestToSetores(setoresRequest);
        setores = setoresRepository.save(setores);
        return setoresToResponse(setores, false); // Retorna SetoresResponse
    }
}