/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controller.cliente;

import br.com.curso.dao.ClienteDAO;
import br.com.curso.model.Cidade;
import br.com.curso.model.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author igorb
 */
@WebServlet(name = "ClienteCadastrar", urlPatterns = {"/ClienteCadastrar"})
public class ClienteCadastrar extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
           response.setContentType("text/html;charset=iso-8859-1");
       String mensagem = null;
       try{
           //busca parametros do formulario ajax - view
           int idPessoa = Integer.parseInt(request.getParameter("idpessoa"));
           int idCliente = Integer.parseInt(request.getParameter("idcliente"));
           String cpfCnpjPessoa = request.getParameter("cpfcnpjpessoa");
           String nomePessoa = request.getParameter("nomepessoa");
           Date dataNascimento = Date.valueOf(request.getParameter("datanascimento"));
           int idCidade = Integer.parseInt(request.getParameter("idcidade"));
           String login = request.getParameter("login");
           String senha = request.getParameter("senha");
           String permitelogin = request.getParameter("permitelogin");
           String situacao = request.getParameter("situacao");
           String fotoPessoa = request.getParameter("fotopessoa");
           String observacao = request.getParameter("observacao");
    
           //cria objeto de cidade
           Cidade oCidade = new Cidade();
           oCidade.setIdCidade(idCidade);
           
           //gera objeto de cliente
           Cliente oCliente = new Cliente(idCliente, observacao, permitelogin, situacao, idPessoa, cpfCnpjPessoa, nomePessoa, dataNascimento, oCidade, login, senha, fotoPessoa);
                   

           //instancia camada dao de cliente
           ClienteDAO dao = new ClienteDAO();
           
           if (dao.cadastrar(oCliente)){
               response.getWriter().write("1");
           }else{
               response.getWriter().write("0");
           }
           
       }catch(Exception e){
           System.out.println("Problemas no servlet cadastrar cliente Erro: "+e.getMessage());
           e.printStackTrace();
       }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
