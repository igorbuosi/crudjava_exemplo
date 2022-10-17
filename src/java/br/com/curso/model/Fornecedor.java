/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.model;

import br.com.curso.utils.Conversao;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author igorb
 */
public class Fornecedor extends Pessoa{
    private int idFornecedor;
    private String enderecoWeb;
    private String permiteLogin;
    private String situacao;

    public Fornecedor(int idFornecedor, String enderecoWeb, String permiteLogin, String situacao, int idPessoa, String cpfCnpj, String nome, Date dataNascimento, Cidade cidade, String login, String senha, String foto) {
        super(idPessoa, cpfCnpj, nome, dataNascimento, cidade, login, senha, foto);
        this.idFornecedor = idFornecedor;
        this.enderecoWeb = enderecoWeb;
        this.permiteLogin = permiteLogin;
        this.situacao = situacao;
    }
    
     public static Fornecedor fornecedorVazio() throws ParseException{
        Cidade oCidade = new Cidade();
        Date dataNascimento = Conversao.dataAtual();
        Fornecedor oFornecedor = new Fornecedor(0,"","S","A",0,"","",dataNascimento,oCidade,"","",null);
        return oFornecedor;      
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getEnderecoWeb() {
        return enderecoWeb;
    }

    public void setEnderecoWeb(String enderecoWeb) {
        this.enderecoWeb = enderecoWeb;
    }

    public String getPermiteLogin() {
        return permiteLogin;
    }

    public void setPermiteLogin(String permiteLogin) {
        this.permiteLogin = permiteLogin;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
     
    
    
}
