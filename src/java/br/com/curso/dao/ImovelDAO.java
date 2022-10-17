/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.dao;

import br.com.curso.model.Imovel;
import br.com.curso.utils.SingleConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author igorb
 */
public class ImovelDAO implements GenericDAO {
    
     private Connection conexao;
    
    public ImovelDAO() throws Exception {
        conexao = SingleConnection.getConnection();
    }

    @Override
    public Boolean cadastrar(Object objeto) {
        Imovel oImovel = (Imovel) objeto;
        boolean retorno = false;
        if (oImovel.getIdImovel() == 0){
            retorno = inserir(oImovel);
        }else{
            retorno = alterar(oImovel);
        }
        return retorno;  
    }

    @Override
    public Boolean inserir(Object objeto) {
        Imovel oImovel = (Imovel) objeto;
        PreparedStatement stmt = null;
        String sql = "insert into imovel (descricao, endereco, valorAluguel) values (?,?,?)";
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oImovel.getDescricao());
            stmt.setString(2, oImovel.getEndereco());
            stmt.setDouble(3, oImovel.getValorAluguel());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao cadastrar Imovel na dao! Erro: "+e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback de imovel ao cadastrar na dao! "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Boolean alterar(Object objeto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean excluir(int numero) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object carregar(int numero) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object> listar() {
         PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Object> resultado = new ArrayList<>();
        Imovel oImovel = null;
        String sql = "select * from imovel";
         try{
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                oImovel = new Imovel();
                oImovel.setIdImovel(rs.getInt("idimovel"));
                oImovel.setDescricao(rs.getString("descricao"));
                oImovel.setEndereco(rs.getString("endereco"));
                oImovel.setValorAluguel(rs.getDouble("valoraluguel"));
                resultado.add(oImovel);
                
            }
        }catch (Exception e){
            System.out.println("Problema ao listar imovel na dao! Erro: "+e.getMessage());
            e.printStackTrace();
        }
        return resultado;
    }
    
}
