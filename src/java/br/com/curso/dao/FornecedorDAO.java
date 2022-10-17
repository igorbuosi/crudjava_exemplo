/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.dao;

import br.com.curso.model.Cidade;
import br.com.curso.model.Fornecedor;
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
public class FornecedorDAO implements GenericDAO {
    private Connection conexao;
    
    public FornecedorDAO() throws Exception{
        conexao = SingleConnection.getConnection();
    }
    

    @Override
    public Boolean cadastrar(Object objeto) {
        Boolean retorno = false;
        try{
            Fornecedor oFornecedor = (Fornecedor) objeto;
            if(oFornecedor.getIdFornecedor() == 0){ //inserção
                //verifica se ja existe pessoa com esse cpf cadastrada
                int idFornecedor = this.verificarCpf(oFornecedor.getCpfCnpj());
                if (idFornecedor == 0){
                    //se nao encontrou insere
                    retorno = this.inserir(oFornecedor);
                }else{
                   // se encontrou o adminidstrador com o cpf altera
                   oFornecedor.setIdFornecedor(idFornecedor);
                   retorno = this.alterar(oFornecedor);
                }
            }else{
                retorno = this.alterar(oFornecedor);
            }
            
        }catch(Exception ex){
            System.out.println("Problema no cadastrar fornecedorDAO! Erro: "+ex.getMessage());
        }
        return retorno;
    }

    @Override
    public Boolean inserir(Object objeto) {
        Fornecedor oFornecedor = (Fornecedor) objeto;
        PreparedStatement stmt = null;   
        String sql = "INSERT INTO fornecedor(idpessoa, enderecoweb, situacao, permitelogin)" +
            " VALUES (?, ?, ?, ?);";
        try{
            PessoaDAO oPessoaDAO = new PessoaDAO();
            //manda infomracoes para cadastrar pessoa
            int idPessoa = oPessoaDAO.cadastrar(oFornecedor);
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPessoa);
            stmt.setString(2, oFornecedor.getEnderecoWeb());
            stmt.setString(3, "A");
            stmt.setString(4, oFornecedor.getPermiteLogin());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao inserir fornecedor na fornecedorDAO  Erro " +e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback do inserir fornecedor na fornecedorDAO Erro "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Boolean alterar(Object objeto) {
        Fornecedor oFornecedor = (Fornecedor) objeto;
        PreparedStatement stmt = null;
        String sql = "update fornecedor set enderecoweb=?, permitelogin=? where idfornecedor=?";
        try{
            PessoaDAO oPessoaDAO = new PessoaDAO();
            oPessoaDAO.cadastrar(oFornecedor); //envia pra classe pessoadao
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oFornecedor.getEnderecoWeb());
            stmt.setString(2, oFornecedor.getPermiteLogin());
            stmt.setInt(3, oFornecedor.getIdFornecedor());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao alterar fornecedor na dao! Erro: "+e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback do alterar fornecedor na dao Erro: "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Boolean excluir(int numero) {
        PreparedStatement stmt = null;
        try{
            //carregar dados de fornecedor
            FornecedorDAO oFornecedorDAO = new FornecedorDAO();
            Fornecedor oFornecedor = (Fornecedor) oFornecedorDAO.carregar(numero);
            
            String situacao = "A"; // verifica e troca a situacao do administrador
            
            if(oFornecedor.getSituacao().equals(situacao))
                situacao = "I";
            else situacao = "A";
            
            String sql = "update fornecedor set situacao = ? where idfornecedor=?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, situacao);
            stmt.setInt(2, oFornecedor.getIdFornecedor());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao excluir fornecedor na cliente dao Erro: ");
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema ao executar rollback no fornecedor dao Erro: "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Object carregar(int numero) {
        int idFornecedor = numero;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Fornecedor oFornecedor= null;
        String sql = "select * from fornecedor c, pessoa p where c.idpessoa = p.idpessoa and c.idfornecedor = ?";
        try{
            stmt= conexao.prepareStatement(sql);
            stmt.setInt(1, idFornecedor);
            rs = stmt.executeQuery();
            while(rs.next()){
              //busca a cidade
              Cidade oCidade = null;
              try{
                  CidadeDAO oCidadeDAO = new CidadeDAO();
                  oCidade = (Cidade) oCidadeDAO.carregar(rs.getInt("idcidade"));
              }catch(Exception ex){
                  System.out.println("Problema ao carregar cidade na nafornecedordAO Erro: "+ex.getMessage());
                  ex.printStackTrace();
              }
              
              oFornecedor = new Fornecedor(
                      rs.getInt("idfornecedor"),
                      rs.getString("enderecoweb"),  
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
            System.out.println("Problemas ao carregar fornecedor na fornecedorDAO Erro: "+e.getMessage());
            e.printStackTrace();
        }
        return oFornecedor;        
    }

    @Override
    public List<Object> listar() {
        List<Object> resultado = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select p.*, c.idfornecedor, c.enderecoweb, c.permitelogin, c.situacao from fornecedor c, "
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
                  System.out.println("Problema ao listar cidade na fornecedordao Erro: "+ex.getMessage());
                  ex.printStackTrace();
              }
              
               Fornecedor oFornecedor = new Fornecedor(
                      rs.getInt("idfornecedor"),
                      rs.getString("enderecoweb"),  
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
               
              resultado.add(oFornecedor);
            }
        }catch(SQLException ex){
            System.out.println("Problema ao listar fornecedor na fornecedorDAO! Erro: "+ex.getMessage());
        }
        return resultado;
    }
    
    public int verificarCpf(String cpf){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idAdministrador = 0;
        String sql = "select c.* from fornecedor c, pessoa p"
                + " where c.idpessoa = p.idpessoa and p.cpfcnpj=?";
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            while(rs.next()){
                idAdministrador = rs.getInt("idfornecedor");
            }
            return idAdministrador;
        }catch(SQLException ex){
            System.out.println("Problema ao carregar pessoa na verificarCpf do fornecedorDAO! Erro: "+ex.getMessage());
            return idAdministrador;
        }
    }
    
}
