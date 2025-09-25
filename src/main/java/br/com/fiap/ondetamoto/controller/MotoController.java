package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.dto.MotoRequest;
import br.com.fiap.ondetamoto.dto.MotoResponse;
import br.com.fiap.ondetamoto.model.Moto;
import br.com.fiap.ondetamoto.repository.MotoRepository;
import br.com.fiap.ondetamoto.service.MotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value ="/api/motos", produces = {"application/json"})
@Tag(name = "api-motos")
public class MotoController {

    @Autowired
    MotoRepository motoRepository;

    @Autowired
    MotoService motoService;

    @Operation(summary = "Criar uma nova Moto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Moto cadastrada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MotoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Atributos informados são inválidos",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<MotoResponse> createMoto(@Valid @RequestBody MotoRequest motoRequest){
        Moto motoSalva = motoService.createMoto(motoRequest);
        MotoResponse response = motoService.motoToResponse(motoSalva, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Retorna uma lista de motos")
    @GetMapping
    public ResponseEntity<Page<MotoResponse>> readMotos(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("placa").ascending());
        return new ResponseEntity<>(motoService.findAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Retorna uma moto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moto encontrada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MotoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhuma moto encontrada",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<MotoResponse> readMoto(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(motoService.findById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Atualiza uma moto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moto encontrada e atualizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MotoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Nenhuma moto encontrada para atualizar",
                    content = @Content(schema = @Schema()))
    })
    @PutMapping("/{id}")
    public ResponseEntity<MotoResponse> updateMoto(@PathVariable Long id, @Valid @RequestBody MotoRequest motoRequest) {
        Moto motoExistente = motoService.findMotoById(id);

        Moto motoAtualizada = motoService.updateMotoFromRequest(motoExistente, motoRequest);
        MotoResponse response = motoService.motoToResponse(motoAtualizada, true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Exclui uma moto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Nenhuma moto encontrada para excluir",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "204", description = "Moto excluída com sucesso",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoto(@PathVariable Long id) {
        Optional<Moto> motoExistente = motoRepository.findById(id);
        if (motoExistente.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        motoService.deleteMoto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
