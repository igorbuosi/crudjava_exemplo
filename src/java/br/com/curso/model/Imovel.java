/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.model;

/**
 *
 * @author igorb
 */
public class Imovel {
    private int idImovel;
    private String descricao;
    private String endereco;
    private Double valorAluguel;

    public Imovel() {
        idImovel = 0;
        descricao = "";
        endereco = "";
        valorAluguel = 0.0;
    }

    public Imovel(int idImovel, String descricao, String endereco, Double valorAluguel) {
        this.idImovel = idImovel;
        this.descricao = descricao;
        this.endereco = endereco;
        this.valorAluguel = valorAluguel;
    }

    public int getIdImovel() {
        return idImovel;
    }

    public void setIdImovel(int idImovel) {
        this.idImovel = idImovel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(Double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }
    
    
    
    
    
}
