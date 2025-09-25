package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.dto.EstabelecimentoRequest;
import br.com.fiap.ondetamoto.dto.EstabelecimentoResponse;
import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.repository.EstabelecimentoRepository;
import br.com.fiap.ondetamoto.service.EstabelecimentoService;
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
@RequestMapping(value = "/api/estabelecimentos", produces = {"application/json"})
@Tag(name = "api-estabelecimentos")
public class EstabelecimentoController {

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    EstabelecimentoService estabelecimentoService;

    @Operation(summary = "Criar um novo Estabelecimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estabelecimento cadastrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estabelecimento.class))}),
            @ApiResponse(responseCode = "400", description = "Atributos informados são inválidos",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<EstabelecimentoResponse> createEstabelecimento(@Valid @RequestBody EstabelecimentoRequest estabelecimentoRequest) {
        Estabelecimento estabelecimentoSalvo = estabelecimentoService.requestToEstabelecimento(estabelecimentoRequest);
        estabelecimentoSalvo = estabelecimentoRepository.save(estabelecimentoSalvo);
        EstabelecimentoResponse response = estabelecimentoService.estabelecimentoToResponse(estabelecimentoSalvo, false);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Retorna uma lista de estabelecimentos")
    @GetMapping
    public ResponseEntity<Page<EstabelecimentoResponse>> readEstabelecimentos(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("endereco").ascending());
        return new ResponseEntity<>(estabelecimentoService.findAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Retorna um estabelecimento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estabelecimento encontrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstabelecimentoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum estabelecimento encontrado",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> readEstabelecimento(@PathVariable Long id) {
        Optional<Estabelecimento> estabelecimento = estabelecimentoRepository.findById(id);
        if (estabelecimento.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EstabelecimentoResponse response = estabelecimentoService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um estabelecimento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estabelecimento encontrado e atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Estabelecimento.class))}),
            @ApiResponse(responseCode = "400", description = "Nenhum estabelecimento encontrado para atualizar",
                    content = @Content(schema = @Schema()))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> updateEstabelecimento(@PathVariable Long id,
                                                                         @Valid @RequestBody EstabelecimentoRequest estabelecimentoRequest) {
        Optional<Estabelecimento> estabelecimentoExistente = estabelecimentoRepository.findById(id);
        if (estabelecimentoExistente.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EstabelecimentoResponse response = estabelecimentoService.updateEstabelecimento(id, estabelecimentoRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Exclui um estabelecimento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Nenhum estabelecimento encontrado para excluir",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "204", description = "Estabelecimento excluído com sucesso",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstabelecimento(@PathVariable Long id) {
        Optional<Estabelecimento> estabelecimentoExistente = estabelecimentoRepository.findById(id);
        if (estabelecimentoExistente.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        estabelecimentoService.deleteEstabelecimento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

