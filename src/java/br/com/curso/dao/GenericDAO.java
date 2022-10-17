/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.dao;

import java.util.List;

/**
 *
 * @author igorb
 */
public interface GenericDAO {
    public Boolean cadastrar(Object objeto);    
    public Boolean inserir(Object objeto);     
    public Boolean alterar(Object objeto);     
    public Boolean excluir(int numero);     
    public Object carregar(int numero);     
    public List<Object> listar();
    
    /*PAREI NA PAGINA 65*/
}
