package br.com.fiap.ondetamoto.dto;

import jakarta.validation.constraints.NotBlank;

public class EstabelecimentoRequest {

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    public EstabelecimentoRequest() {
    }

    public EstabelecimentoRequest(String endereco) {
        this.endereco = endereco;
    }


    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
