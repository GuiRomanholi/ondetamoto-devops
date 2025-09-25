package br.com.fiap.ondetamoto.service;

import br.com.fiap.ondetamoto.controller.EstabelecimentoController;
import br.com.fiap.ondetamoto.controller.MotoController;
import br.com.fiap.ondetamoto.controller.SetoresController;
import br.com.fiap.ondetamoto.dto.EstabelecimentoResponse;
import br.com.fiap.ondetamoto.dto.MotoRequest;
import br.com.fiap.ondetamoto.dto.MotoResponse;
import br.com.fiap.ondetamoto.dto.SetoresResponse;
import br.com.fiap.ondetamoto.model.Moto;
import br.com.fiap.ondetamoto.model.Setores;
import br.com.fiap.ondetamoto.repository.MotoRepository;
import br.com.fiap.ondetamoto.repository.SetoresRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MotoService {
    private final MotoRepository motoRepository;
    private final SetoresRepository setoresRepository;

    public MotoService(MotoRepository motoRepository, SetoresRepository setoresRepository){
        this.motoRepository = motoRepository;
        this.setoresRepository = setoresRepository;
    }

    public Moto requestToMoto(MotoRequest motoRequest) {
        Moto moto = new Moto();
        moto.setMarca(motoRequest.getMarca());
        moto.setPlaca(motoRequest.getPlaca());
        moto.setTag(motoRequest.getTag());

        if (motoRequest.getIdSetores() != null) {
            Optional<Setores> setores = setoresRepository.findById(motoRequest.getIdSetores());
            setores.ifPresent(moto::setSetores);
        }
        return moto;
    }

    public MotoResponse motoToResponse(Moto moto, boolean self){
        Link link;
        if (self){
            link = linkTo(
                    methodOn(MotoController.class).readMoto(moto.getId())
            ).withSelfRel();
        } else {
            link = linkTo(
                    methodOn(MotoController.class).readMotos(0)
            ).withRel("Lista de Motos");
        }

        SetoresResponse setoresResponse = null;
        if (moto.getSetores() != null) {
            // Crie o DTO de Estabelecimento primeiro
            EstabelecimentoResponse estabelecimentoResponse = new EstabelecimentoResponse(
                    moto.getSetores().getEstabelecimento().getId(),
                    moto.getSetores().getEstabelecimento().getEndereco(),
                    linkTo(methodOn(EstabelecimentoController.class).readEstabelecimento(moto.getSetores().getEstabelecimento().getId())).withSelfRel()
            );

            // Crie o DTO de Setores sem a lista de motos
            setoresResponse = new SetoresResponse(
                    moto.getSetores().getId(),
                    moto.getSetores().getNome(),
                    moto.getSetores().getTipo(),
                    moto.getSetores().getTamanho(),
                    estabelecimentoResponse,
                    linkTo(methodOn(SetoresController.class).readSetor(moto.getSetores().getId())).withSelfRel()
            );
        }

        return new MotoResponse(moto.getId(), moto.getPlaca(), moto.getMarca(), moto.getTag(), setoresResponse, link);
    }


    public List<MotoResponse> motosToResponse(List<Moto> motos) {
        List<MotoResponse> motosResponse = new ArrayList<>();
        for (Moto moto : motos) {
            motosResponse.add(motoToResponse(moto, true));
        }
        return motosResponse;
    }

    @Cacheable(value = "motos", key = "#pageable.pageNumber")
    public Page<MotoResponse> findAll(Pageable pageable) {
        return motoRepository.findAll(pageable)
                .map(moto -> motoToResponse(moto, true));
    }

    @Cacheable(value = "moto", key = "#id")
    public MotoResponse findById(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        return motoToResponse(moto, false);
    }

    public Moto findMotoById(Long id) {
        return motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
    }

    @CacheEvict(value = {"motos", "moto", "motosRaw", "motoRaw"}, allEntries = true)
    public void deleteById(Long id) {
        motoRepository.deleteById(id);
    }


    @CacheEvict(value = {"motos", "moto"}, allEntries = true)
    public Moto updateMotoFromRequest(Moto motoExistente, MotoRequest motoRequest) {
        motoExistente.setMarca(motoRequest.getMarca());
        motoExistente.setPlaca(motoRequest.getPlaca());
        motoExistente.setTag(motoRequest.getTag());

        if (motoRequest.getIdSetores() != null) {
            Optional<Setores> setores = setoresRepository.findById(motoRequest.getIdSetores());
            setores.ifPresent(motoExistente::setSetores);
        }

        return motoRepository.save(motoExistente);
    }

    @CacheEvict(value = "motos", allEntries = true)
    public Moto createMoto(MotoRequest motoRequest) {
        Moto moto = requestToMoto(motoRequest);
        return motoRepository.save(moto);
    }

    @Cacheable(value = "motosRaw", key = "#pageable.pageNumber")
    public Page<Moto> findAllRaw(Pageable pageable) {
        return motoRepository.findAll(pageable);
    }

    @Cacheable(value = "motoRaw", key = "#id")
    public Optional<Moto> findByIdRaw(Long id) {
        return motoRepository.findById(id);
    }

    @CacheEvict(value = {"motos", "moto", "motosRaw", "motoRaw"}, allEntries = true)
    public Moto saveRaw(Moto moto) {
        return motoRepository.save(moto);
    }

    @CacheEvict(value = {"motos", "moto"}, allEntries = true)
    public void deleteMoto(Long id) {
        motoRepository.deleteById(id);
    }
}

