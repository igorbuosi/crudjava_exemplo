/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.filter;

import br.com.curso.model.Usuario;
import br.com.curso.utils.SingleConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author igorb
 */
@WebFilter(urlPatterns=("/*"))
public class FilterAutenticacao implements Filter {
    private static Connection conexao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        conexao = SingleConnection.getConnection();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        try{
            HttpServletRequest req = (HttpServletRequest) request;
            //pega a sessao vigente no contexto do servidor
            HttpSession sessao = req.getSession(false);
            String urlParaAutenticar = req.getServletPath(); //url que está sendo acessada
            
            if ((sessao != null && sessao.getAttributeNames().hasMoreElements()) ||
                    (urlParaAutenticar.equalsIgnoreCase("/index.jsp") ||
                    urlParaAutenticar.equalsIgnoreCase("/home.jsp") ||
                    urlParaAutenticar.equalsIgnoreCase("/header.jsp") ||
                    urlParaAutenticar.equalsIgnoreCase("/footer.jsp") ||
                    urlParaAutenticar.equalsIgnoreCase("/login.jsp") ||
                    urlParaAutenticar.equalsIgnoreCase("/UsuarioBuscarPorLogin") ||
                    urlParaAutenticar.equalsIgnoreCase("/UsuarioLogar") ||
                    urlParaAutenticar.equalsIgnoreCase("/js/jquery-3.1.1.min.js") ||
                    urlParaAutenticar.equalsIgnoreCase("/js/jquery.mask.min.js") ||
                    urlParaAutenticar.equalsIgnoreCase("/js/jquery.maskMoney.min.js") ||
                    urlParaAutenticar.equalsIgnoreCase("/js/app.js"))){
               
                // valida controle de usuario
                if (Usuario.verificaUsuario(urlParaAutenticar, sessao)){
                     // passou pela validacao de seguranca encaminha para execucao
                    chain.doFilter(request, response);    
                } else{
                    // se a sessao for nula volta pra tela sem logar
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    
                    return; // para a executação e redireciona para o index.jsp    
                }
                
            }else{
                // se a sessao for nula volta pra tela sem logar
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return; // para a executação e redireciona para o index.jsp
            }
            
        } catch(Exception e){
            System.out.println("Erro no do filter ! Erro: "+e.getMessage());
            e.printStackTrace();
        }
        
    }

    @Override
    public void destroy() {
        try{
            conexao.close();
        }catch(SQLException ex){
            System.out.println("Erro no destroy: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
