/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.dao;

import br.com.curso.model.Cidade;
import br.com.curso.model.Cliente;
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
public class ClienteDAO implements GenericDAO  {

    private Connection conexao;
    
    public ClienteDAO() throws Exception{
        conexao = SingleConnection.getConnection();
    }
    

    @Override
    public Boolean cadastrar(Object objeto) {
        Boolean retorno = false;
        try{
            Cliente oCliente = (Cliente) objeto;
            if(oCliente.getIdCliente() == 0){ //inserção
                //verifica se ja existe pessoa com esse cpf cadastrada
                int idCliente = this.verificarCpf(oCliente.getCpfCnpj());
                if (idCliente == 0){
                    //se nao encontrou insere
                    retorno = this.inserir(oCliente);
                }else{
                   // se encontrou o adminidstrador com o cpf altera
                   oCliente.setIdCliente(idCliente);
                   retorno = this.alterar(oCliente);
                }
            }else{
                retorno = this.alterar(oCliente);
            }
            
        }catch(Exception ex){
            System.out.println("Problema no cadastrar clienteDAO! Erro: "+ex.getMessage());
        }
        return retorno;
    }

    @Override
    public Boolean inserir(Object objeto) {
        Cliente oCliente = (Cliente) objeto;
        PreparedStatement stmt = null;   
        String sql = "INSERT INTO cliente (idpessoa, observacao, situacao, permitelogin)" +
            "VALUES (?, ?, ?, ?)";
        try{
            PessoaDAO oPessoaDAO = new PessoaDAO();
            //manda infomracoes para cadastrar pessoa
            int idPessoa = oPessoaDAO.cadastrar(oCliente);
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPessoa);
            stmt.setString(2, oCliente.getObservacao());
            stmt.setString(3, "A");
            stmt.setString(4, oCliente.getPermiteLogin());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao inserir cliente na clienteDAO  Erro " +e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback do inserir cliente na clienteDAO Erro "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Boolean alterar(Object objeto) {
        Cliente oCliente = (Cliente) objeto;
        PreparedStatement stmt = null;
        String sql = "update cliente set observacao=?, permitelogin=? where idcliente=?";
        try{
            PessoaDAO oPessoaDAO = new PessoaDAO();
            oPessoaDAO.cadastrar(oCliente); //envia pra classe pessoadao
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, oCliente.getObservacao());
            stmt.setString(2, oCliente.getPermiteLogin());
            stmt.setInt(3, oCliente.getIdCliente());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao alterar cliente na dao! Erro: "+e.getMessage());
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema no rollback do alterar cliente na dao Erro: "+ex.getMessage());
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
            ClienteDAO oClienteDAO = new ClienteDAO();
            Cliente oCliente = (Cliente) oClienteDAO.carregar(numero);
            
            String situacao = "A"; // verifica e troca a situacao do administrador
            
            if(oCliente.getSituacao().equals(situacao))
                situacao = "I";
            else situacao = "A";
            
            String sql = "update cliente set situacao = ? where idcliente=?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, situacao);
            stmt.setInt(2, oCliente.getIdCliente());
            stmt.execute();
            conexao.commit();
            return true;
        }catch(Exception e){
            try{
                System.out.println("Problema ao excluir cliente na cliente dao Erro: ");
                e.printStackTrace();
                conexao.rollback();
            }catch(SQLException ex){
                System.out.println("Problema ao executar rollback no cliente dao Erro: "+ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }
    
    }

    @Override
    public Object carregar(int numero) {
        int idCliente = numero;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente oCliente = null;
        String sql = "select * from cliente c, pessoa p where c.idpessoa = p.idpessoa and c.idcliente = ?";
        try{
            stmt= conexao.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();
            while(rs.next()){
              //busca a cidade
              Cidade oCidade = null;
              try{
                  CidadeDAO oCidadeDAO = new CidadeDAO();
                  oCidade = (Cidade) oCidadeDAO.carregar(rs.getInt("idcidade"));
              }catch(Exception ex){
                  System.out.println("Problmea ao carregar cidade na clientedao Erro: "+ex.getMessage());
                  ex.printStackTrace();
              }
              
              oCliente = new Cliente(rs.getInt("idcliente"), 
                      rs.getString("observacao"), 
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
            System.out.println("Problemas ao carregar cliente na clientedao Erro: "+e.getMessage());
            e.printStackTrace();
        }
        return oCliente;        
    }

    @Override
    public List<Object> listar() {
        List<Object> resultado = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select p.*, c.idcliente, c.observacao, c.permitelogin, c.situacao from cliente c, "
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
                  System.out.println("Problema ao listar cidade na clientedao Erro: "+ex.getMessage());
                  ex.printStackTrace();
              }
              
              Cliente oCliente = new Cliente(rs.getInt("idcliente"), 
                      rs.getString("observacao"), 
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
              
              resultado.add(oCliente);
            }
        }catch(SQLException ex){
            System.out.println("Problema ao listar cliente na clienteDAO! Erro: "+ex.getMessage());
        }
        return resultado;
    }
    
    public int verificarCpf(String cpf){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idAdministrador = 0;
        String sql = "select c.* from cliente c, pessoa p"
                + " where c.idpessoa = p.idpessoa and p.cpfcnpj=?";
        try{
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            while(rs.next()){
                idAdministrador = rs.getInt("idcliente");
            }
            return idAdministrador;
        }catch(SQLException ex){
            System.out.println("Problema ao carregar pessoa na verificarCpf do clienteDAO! Erro: "+ex.getMessage());
            return idAdministrador;
        }
    }
    
}
