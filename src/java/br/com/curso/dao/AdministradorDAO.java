/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.dao;

import br.com.curso.model.Administrador;
import br.com.curso.model.Cidade;
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
public class AdministradorDAO implements GenericDAO {
    
    private Connection conexao;
    
    public AdministradorDAO() throws Exception{
        conexao = SingleConnection.getConnection();
    }
    

    @Override
    public Boolean cadastrar(Object objeto) {
        Boolean retorno = false;
        try{
            Administrador oAdministrador = (Administrador) objeto;
            if(oAdministrador.getIdAdministrador() == 0){ //inserção
                //verifica se ja existe pessoa com esse cpf cadastrada
                int idAdministrador = this.verificarCpf(oAdministrador.getCpfCnpj());
                if (idAdministrador == 0){
                    //se nao encontrou insere
                    retorno = this.inserir(oAdministrador);
                }else{
                   // se encontrou o adminidstrador com o cpf altera
                   oAdministrador.setIdAdministrador(idAdministrador);
                   retorno = this.alterar(oAdministrador);
                }
            }else{
                retorno = this.alterar(oAdministrador);
            }
            
        }catch(Exception ex){
            System.out.println("Problema no cadastrar administradorDAO! Erro: "+ex.getMessage());
        }
        return retorno;
    }

    @Override
    public Boolean inserir(Object objeto) {
        Administrador oAdministrador = (Administrador) objeto;
        PreparedStatement stmt = null;
        String sql = "insert into administrador(idpessoa, situacao, permitelogin) values (?,?,?)";
        try{
            PessoaDAO oPessoaDAO = new PessoaDAO();
            //manda infomracoes para cadastrar pessoa
            int idPessoa = oPessoaDAO.cadastrar(oAdministrador);
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPessoa);
            stmt.setString(2, "A");
            stmt.setString(3, oAdministrador.getPermiteLogin());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao inserir administrador na administradorDAO  Erro " +e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback do inserir administrador na administradordao Erro "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Boolean alterar(Object objeto) {
        Administrador oAdministrador = (Administrador) objeto;
        PreparedStatement stmt = null;
        String sql = "update administrador set permitelogin=? where idadministrador=?";
        try{
            PessoaDAO oPessoaDAO = new PessoaDAO();
            oPessoaDAO.cadastrar(oAdministrador); //envia pra classe pessoadao
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oAdministrador.getPermiteLogin());
            stmt.setInt(2, oAdministrador.getIdAdministrador());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao alterar administrador na dao! Erro: "+e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback do alterar administrador na dao Erro: "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Boolean excluir(int numero) {
        PreparedStatement stmt = null;
        try{
            //carregar dados de administrador
            AdministradorDAO oAdministradorDAO = new AdministradorDAO();
            Administrador oAdministrador = (Administrador) oAdministradorDAO.carregar(numero);
            String situacao = "A"; // verifica e troca a situacao do administrador
            
            if(oAdministrador.getSituacao().equals(situacao))
                situacao = "I";
            else situacao = "A";
            
            String sql = "update administrador set situacao = ? where idadministrador=?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, situacao);
            stmt.setInt(2, oAdministrador.getIdAdministrador());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao excluir administrador na administrador dao Erro: ");
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema ao executar rollback na administrado dao Erro: "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Object carregar(int numero) {
        int idAdministrador = numero;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Administrador oAdministrador = null;
        String sql = "select * from administrador c, pessoa p where c.idpessoa = p.idpessoa and c.idadministrador = ?";
        try{
            stmt= conexao.prepareStatement(sql);
            stmt.setInt(1, idAdministrador);
            rs = stmt.executeQuery();
            while(rs.next()){
              //busca a cidade
              Cidade oCidade = null;
              try{
                  CidadeDAO oCidadeDAO = new CidadeDAO();
                  oCidade = (Cidade) oCidadeDAO.carregar(rs.getInt("idcidade"));
              }catch(Exception ex){
                  System.out.println("Problmea ao carregar cidade na administradordao Erro: "+ex.getMessage());
                  ex.printStackTrace();
              }
              
              oAdministrador = new Administrador(rs.getInt("idadministrador"),
                      rs.getString("permitelogin"),
                      rs.getString("situacao"),
                      rs.getInt("idpessoa"),
                      rs.getString("cpfcnpj"),
                      rs.getString("nome"),
                      rs.getDate("datanascimento"),
                      oCidade, 
                      rs.getString("login"),
                      rs.getString("senha"),
                      rs.getString("foto"));   
            }
        }catch(SQLException e){
            System.out.println("Problemas ao carregar administrador na administradordao Erro: "+e.getMessage());
            e.printStackTrace();
        }
        return oAdministrador;        
    }

    @Override
    public List<Object> listar() {
        List<Object> resultado = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select p.*, c.idadministrador, c.situacao, c.permitelogin from administrador c, "
                + "pessoa p where c.idpessoa = p.idpessoa order by idpessoa";
        
        try{
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
              //busca a cidade
              Cidade oCidade = null;
              try{
                  CidadeDAO oCidadeDAO = new CidadeDAO();
                  oCidade = (Cidade) oCidadeDAO.carregar(rs.getInt("idcidade"));
              }catch(Exception ex){
                  System.out.println("Problema ao listar cidade na administradordao Erro: "+ex.getMessage());
                  ex.printStackTrace();
              }
              
              Administrador oAdministrador = new Administrador(rs.getInt("idadministrador"),
                      rs.getString("permitelogin"),
                      rs.getString("situacao"),
                      rs.getInt("idpessoa"),
                      rs.getString("cpfcnpj"),
                      rs.getString("nome"),
                      rs.getDate("datanascimento"),
                      oCidade, 
                      rs.getString("login"),
                      rs.getString("senha"),
                      rs.getString("foto"));   
              
              resultado.add(oAdministrador);
            }
        }catch(SQLException ex){
            System.out.println("Problema ao listar administrador ! Erro: "+ex.getMessage());
        }
        return resultado;
    }
    
    public int verificarCpf(String cpf){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idAdministrador = 0;
        String sql = "select c.* from administrador c, pessoa p"
                + " where c.idpessoa = p.idpessoa and p.cpfcnpj=?";
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            while(rs.next()){
                idAdministrador = rs.getInt("idadministrador");
            }
            return idAdministrador;
        }catch(SQLException ex){
            System.out.println("Problema ao carregar pessoa na verificarCpf do administradordAO! Erro: "+ex.getMessage());
            return idAdministrador;
        }
    }
    
    
}
