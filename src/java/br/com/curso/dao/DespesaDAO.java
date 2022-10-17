/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.dao;

import br.com.curso.model.Despesa;
import br.com.curso.utils.Conversao;
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
public class DespesaDAO implements GenericDAO {
    
    private Connection conexao;
    
    public DespesaDAO() throws Exception {
        conexao = SingleConnection.getConnection();
    }

    @Override
    public Boolean cadastrar(Object objeto) {
        Despesa oDespesa = (Despesa) objeto;
        boolean retorno = false;
        if (oDespesa.getIdDespesa() == 0){
            retorno = inserir(oDespesa);
        }else{
            retorno = alterar(oDespesa);
        }
        return retorno;        
    }

    @Override
    public Boolean inserir(Object objeto) {
        Despesa oDespesa = (Despesa) objeto;
        PreparedStatement stmt = null;
        String sql = "insert into despesa(descricao, valorDespesa, valorPago,"
                +"datadocumento, imagemdocumento) values (?,?,?,?,?)";
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oDespesa.getDescricao());
            stmt.setDouble(2, oDespesa.getValorDespesa());
            stmt.setDouble(3, oDespesa.getValorPago());
            stmt.setDate(4, new java.sql.Date(oDespesa.getDataDocumento().getTime()));
            stmt.setString(5, oDespesa.getImagemDocumento());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao cadastrar despesa na dao! Erro: "+e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback de despesa ao cadastrar na dao! "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }

    }

    @Override
    public Boolean alterar(Object objeto) {
        Despesa oDespesa = (Despesa) objeto;
        PreparedStatement stmt = null;
        String sql = "update despesa set descricao=?, valorDespesa=?, valorPago=?,"
                + " datadocumento=?, imagemdocumento=? where iddespesa=?";
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oDespesa.getDescricao());
            stmt.setDouble(2, oDespesa.getValorDespesa());
            stmt.setDouble(3, oDespesa.getValorPago());
            stmt.setDate(4, new java.sql.Date(oDespesa.getDataDocumento().getTime()));
            stmt.setString(5, oDespesa.getImagemDocumento());
            stmt.setInt(6, oDespesa.getIdDespesa());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao alterar despesa na dao! Erro: "+e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback de despesa ao alterar na dao! "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Boolean excluir(int numero) {
        int idDespesa = numero;
        PreparedStatement stmt = null;
        String sql = "delete from despesa where iddespesa=?";
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idDespesa);
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
           try{
                System.out.println("Problema ao excluir despesa na dao! Erro: "+e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback de excluir despesa na dao! "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Object carregar(int numero) {
        int idDespesa = numero;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Despesa oDespesa = null;
        String sql = "select * from despesa where iddespesa = ?";
        
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idDespesa);
            rs = stmt.executeQuery();
            while(rs.next()){
                oDespesa = new Despesa();
                oDespesa.setIdDespesa(rs.getInt("iddespesa"));
                oDespesa.setDescricao(rs.getString("descricao"));
                oDespesa.setValorDespesa(rs.getDouble("valorDespesa"));
                oDespesa.setValorPago(rs.getDouble("valorPago"));
                oDespesa.setDataDocumento(rs.getDate("datadocumento"));
                oDespesa.setImagemDocumento(rs.getString("imagemdocumento"));
                
            }
        }catch (Exception e){
            System.out.println("Problema ao carregar despesa na dao! Erro: "+e.getMessage());
            e.printStackTrace();
        }
        return oDespesa;
    }

    @Override
    public List<Object> listar() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Object> resultado = new ArrayList<>();
        Despesa oDespesa = null;
        String sql = "select * from despesa";
         try{
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                oDespesa = new Despesa();
                oDespesa.setIdDespesa(rs.getInt("idDespesa"));
                oDespesa.setDescricao(rs.getString("descricao"));
                oDespesa.setValorDespesa(rs.getDouble("valorDespesa"));
                oDespesa.setValorPago(rs.getDouble("valorPago"));
                oDespesa.setDataDocumento(rs.getDate("datadocumento"));
                oDespesa.setImagemDocumento(rs.getString("imagemdocumento"));
                resultado.add(oDespesa);
                
            }
        }catch (Exception e){
            System.out.println("Problema ao listar despesa na dao! Erro: "+e.getMessage());
            e.printStackTrace();
        }
        return resultado;
    
    }
    
    public String listarJSON(){
        String strJson="";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Object> resultado = new ArrayList<>();
        Despesa oDepesa = null;
        String sql = "select * from despesa";
        
        try{
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            strJson = "[";
            int i = 0;
            while(rs.next()){
                if (i>0) strJson+=",";
                strJson +="{\"idDespesa\":"+rs.getInt("iddespesa")+","
                        + "\"descricao\":\""+rs.getString("descricao")+"\","
                        + "\"dataDocumento\":\""+Conversao.data2String(rs.getDate("datadocumento"))+"\","
                        +"\"valorDespesa\":\""+Conversao.valorDinheiro(rs.getDouble("valorDespesa"),"BR")+"\","
                        +"\"valorPago\":\""+Conversao.valorDinheiro(rs.getDouble("valorPago"),"BR")+"\"}";
                i++;           
            }
            strJson+="]";
        }catch (Exception e){
            System.out.println("Problema ao listar despesa no metodo listarJSON da dao Erro: "+e.getMessage());
            e.printStackTrace();
        }
        System.out.println(strJson);  
        return strJson;
    }
    
}
