<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="iso-8859-1" %>
<jsp:include page="/header.jsp"/>
<jsp:include page="/cadastros/menuLogado.jsp"/>

<div class="container-fluid">
    <!-- Page  heading -->
    <h1 class="h3 mb-2 text-gray 800">Fornecedor</h1>
    <p class="mb-4"> Planilha de Registros </p>
    <a href="#modaladicionar" class="btn btn-success mb-4 adicionar" data-toggle="modal" data-id="" onclick="setDadosModal(${0})">
        <i class="fas fa-plus fa-fw"></i> Adicionar </a>
    <div class="card shadow">
        <div class="card-body">
            <table id="datatable" class="display">
                <thead>
                    <tr>
                        <th align="center">ID</th>
                        <th align="center">Nome</th>
                        <th align="center">CPF/CNPJ</th>
                        <th align="center">Cidade - UF</th>
                        <th align="center">Alterar</th>
                        <th align="center">Excluir</th>                                     
                    </tr> 
                </thead>
                <tbody>
                    <c:forEach var="fornecedor" items="${fornecedores}">
                        <tr>
                            <td align="center">${fornecedor.idFornecedor}</td>
                            <td align="center">${fornecedor.nome}</td>
                            <td align="center">${fornecedor.cpfCnpj}</td>
                            <td align="center">${fornecedor.cidade.nomeCidade} - ${fornecedor.cidade.estado.siglaEstado}</td>
                            <td align="center">
                                <a href="#modaladicionar" class="btn btn-success adicionar" data-toggle="modal" data-id="" 
                                   onclick="setDadosModal(${fornecedor.idFornecedor})">
                                    <i class="fas fa-plus fa-fw"></i> Alterar </a>
                            </td>
                            <td align="center">
                                <a href="#" onclick="deletar(${fornecedor.idFornecedor})">
                                <button class="btn 
                                            <c:out value="${fornecedor.situacao == 'A' ? 'btn-danger' : 'btn-success'}"/>">
                                        <i class="fas fa-fw"
                                           <c:out value="${fornecedor.situacao == 'A' ? 'fa-times' : 'fa-plus'}"/>"></i>
                                           <strong>
                                               <c:out value="${fornecedor.situacao == 'A' ? 'Inativar' : 'Ativar'}"/>                                               
                                           </strong>
                                </button></a>
                            </td>
                        </tr>          
                    </c:forEach>    
                </tbody>      
            </table>      
        </div>
    </div>
        
        <div class="modal fade" id="modaladicionar" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Adicionar</h5>
                        <button type="button" class="close" data-dismiss="modal" arial-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>                        
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <img alt="imagem" class="" name="foto" id="foto" width="85" heigth="100">
                            <input type="file" id="gallery-photo-add" class="inputfile" onchange="uploadFile();"/>
                            <label for="gallery-photo-add" class="btn btn-success">
                            <i class="fas fa-file-upload"></i>
                            Selecionar Foto...
                            </label>
                        </div>
                        <div class="form-group">
                            <input class="form-control" type="text" name="idpessoa" id="idpessoa" value="" readonly="readonly"/>
                            <input class="form-control" type="text" name="idfornecedor" id="idfornecedor" value="" readonly="readonly"/>
                            <input class="form-control" type="text" name="situacao" id="situacao" value="" readonly="readonly"/>                                                   
                        </div>
                        <div class="form-group">
                            <label>CPF/CNPJ</label>
                            <input class="form-control" type="text" name="cpfcnpjpessoa" id="cpfcnpjpessoa" value=""/>    
                        </div>
                        
                        <div class="form-group">
                            <label>Nome/Raz?o Social</label>
                            <input class="form-control" type="text" name="nomepessoa" id="nomepessoa"/>    
                        </div>
                        
                        <div class="form-group">
                            <div class="form-line row">
                                <div class="col-sm">
                                    <label class="m-t-0 header title">Data Nascimento</label>
                                    <input class="form-control" type="date" name="datanascimento" id="datanascimento" value=""/>
                                </div>
                                 <div class="col-sm">
                                    <label>Estado</label>
                                    <select class="form-control" name="idestado" id="idestado"
                                        onchange="BuscarCidadesPorEstado()" required>
                                        <option value ="nulo">Selecione</option>  
                                        <c:forEach var="estado" items="${estados}">
                                            <option value="${estado.idEstado}"
                                                    ${fornecedor.cidade.estado.idEstado == estado.idEstado ? "selected" : ""}>
                                                ${estado.nomeEstado}
                                            </option>                                            
                                        </c:forEach>      
                                    </select>              
                                </div>
                                <div class="col-sm">
                                    <label>Cidade</label>
                                    <select class="form-control" name="idcidade" id="idcidade" required>
                                        <option value ="nulo">Selecione</option>  
                                        <c:forEach var="cidade" items="${cidades}">
                                            <option value="${cidade.idCidade}"
                                                    ${fornecedor.cidade.idCidade == cidade.idCidade ? "selected" : ""}>
                                                ${cidade.nomeCidade}
                                            </option>                                            
                                        </c:forEach>      
                                    </select>                 
                                </div>      
                            </div>    
                        </div>
                        <div class="form-group">
                            <div class="form-line row">
                                <div class="col-sm">
                                    <label>Login</label>
                                    <input class="form-control" type="text" name="login" id="login" value="" size="20" maxlength="20" required/>           
                                </div>
                                
                                <div class="col-sm">
                                    <label>Senha</label>
                                    <input class="form-control" type="password" name="senha" id="senha" value="" size="20" maxlength="20" required/>           
                                </div>
                                
                                <div class="col-sm">
                                    <label>Permite Login</label>
                                    <select class="form-control" name="permitelogin" id="permitelogin">
                                        <option value="N" ${fornecedor.permiteLogin == 'N' ? "selected" : ""}>N?o</option>
                                        <option value="S" ${fornecedor.permiteLogin == 'S' ? "selected" : ""}>Sim</option> 
                                    </select>        
                                </div>          
                            </div>
                        </div>                                    
                        <div class="form-group">
                            <label>Endere?o web: </label>
                            <input class="form-control" type="text" name="enderecoweb" id="enderecoweb" value=""/>    
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                            <a href="#" onclick="validarCampos()">
                                <button type="button" class="btn btn-success">Salvar</button>
                            </a>                       
                        </div>                    
                    </div>        
                </div>      
            </div>   
        </div>
</div>
                                    
<style type="text/css">
    
    .inputfile{
        width:0.1px;
        height: 0.1px;
        opacity: 0;
        overflow: hidden;
        position: absolute;
        z-index: -1;
    }
    .inputfile:focus + label{
        outline: 1px dotted #000;
        outline: -webkit-focus-ring-color auto 5px;
    }
    .inputfile + label *{
        pointer-events: none;
    }
    
    .borda{
        position: relative;
        margin: 0 20px 30px 0;
        padding: 10px;
        border: 1px solid #e1e1e1;
        border-radius: 3px;
        background: #fff;
        -webkit-box-shadow: 0px 0px 3px rgba(0,0,0,0.06);
        -moz-box-shadow: 0px 0px 3px rgba(0,0,0,0.06);
        box-shadow: 0px 0px 3px rgba(0,0,0,0.06);
    }
</style>   

<script>
    
     $(document).ready(function() {
            console.log('entrei ready do js local');
            
            $('#datatable').DataTable({
                "oLanguage" : {
                    "sProcessing": "Processando...",
                    "sLengthMenu" : "Mostrar _MENU_ registros",
                    "sZeroRecords" : "Nenhum registro encontrado",
                    "sInfo" : "Mostrando de _START_ at? _END_ de _TOTAL_ registros",
                    "sInfoEmpty" : "Mostrando de 0 at? 0 de 0 registros",
                    "sInfoFiltered" : "",
                    "sInfoPostFix" : "",
                    "sSearch" : "Buscar:",
                    "sUrl" :"",
                    "oPaginate" : {
                        "sFirst" : "Primeiro",
                        "sPrevious" : "Anterior",
                        "sNext" : "Seguinte",
                        "sLast" : "?ltimo"
                    }
                }
            });              
        });
        
        var cidade = ''; // variavel para controle do carregametno de cidade      
        function limparDadosModal(){
             console.log('ENTROU NA FUNCAO LIMPARDADOSMODAL');
            $('#idpessoa').val("0");
            $('#idfornecedor').val("0");
            $('#situacao').val("");
            $('#cpfcnpjpessoa').val("");
            $('#nomepessoa').val("");
            $('#datanascimento').val("");
            $('#idestado').val("0");
            $('#login').val("");
            $('#senha').val("");
            $('#permitelogin').val("S");
            $('#enderecoweb').val("");
            foto.src="";
        }
        
        function setDadosModal(valor){
            console.log('ENTROU NA FUNCAO SET DADOS MODAL');
            limparDadosModal();
            var foto = document.getElementById("foto");
            document.getElementById('idpessoa').value = valor;
            document.getElementById('idfornecedor').value = valor;
            var idFornecedor = valor;            
            if(idFornecedor != "0"){
                //existe administrador pra buscar (alteracao)
                $.getJSON('FornecedorCarregar', {idFornecedor : idFornecedor}, function(respostaServlet){
                    console.log(respostaServlet);
                    var id = respostaServlet.idFornecedor;
                    if (id != "0"){
                        $('#idpessoa').val(respostaServlet.idPessoa);
                        $('#idfornecedor').val(respostaServlet.idFornecedor);
                        $('#situacao').val(respostaServlet.situacao);
                        $('#cpfcnpjpessoa').val(respostaServlet.cpfCnpj);
                        $('#nomepessoa').val(respostaServlet.nome);
                        $('#datanascimento').val(respostaServlet.dataNascimento);
                        $('#idestado').val(respostaServlet.cidade.estado.idEstado);
                        cidade = respostaServlet.cidade.idCidade;
                        BuscarCidadesPorEstado(); // atualiza a lista de cidades
                        $('#login').val(respostaServlet.login);
                        $('#senha').val(respostaServlet.senha);
                        $('#permitelogin').val(respostaServlet.permiteLogin);
                        $('#enderecoweb').val(respostaServlet.enderecoWeb);
                        foto.src = respostaServlet.foto;
                    }                              
                });
            }
        }
        
        function carregarPessoa(v){
            console.log('Entrou na funcao carregarPessoa do fornecedor.jsp | paramtro v: '+v)
            var idM = v;
            var tipoPessoa = 'fornecedor';   
            $(document).ready(function(){
                $.getJSON('PessoaBuscarCpfCnpj', {cpfcnpjpessoa: idM, tipopessoa: tipoPessoa}, function (respostaFornecedor){
                    console.log('resposta fornecedor ' + respostaFornecedor);
                    
                    if (respostaFornecedor != null){
                        $('#idfornecedor').val(respostaFornecedor.idFornecedor);
                        $('#idpessoa').val(respostaFornecedor.idPessoa);
                        $('#permiteLogin').val(respostaFornecedor.permiteLogin);
                        $('#situacao').val(respostaFornecedor.situacao);
                        $('#idpessoa').val(respostaFornecedor.idPessoa);
                        $('#nomepessoa').val(respostaFornecedor.nome);
                        $('#login').val(respostaFornecedor.login);
                        $('#observacao').val(respostaFornecedor.observacao);
                        var datanasc = respostaFornecedor.dataNascimento;
                        console.log(datanasc);
                        $('#datanascimento').val(datanasc);
                        $('#idestado').val(respostaFornecedor.cidade.estado.idEstado);
                        cidade = respostaFornecedor.cidade.idCidade;
                        BuscarCidadesPorEstado();
                        var foto = document.getElementById("foto");
                        foto.src = respostaFornecedor.foto;
                    }else{                       
                        tipoPessoa = 'pessoa';
                        $.getJSON('PessoaBuscarCpfCnpj',{cpfcnpjpessoa: idM, tipopessoa: tipoPessoa}, function(respostaPessoa){
                            console.log(respostaPessoa);
                            var id = respostaPessoa.idPessoa;
                            if (id != "0"){
                                $('#idpessoa').val(respostaPessoa.idPessoa);
                                $('#nomepessoa').val(respostaPessoa.nome);
                                $('#login').val(respostaPessoa.login);
                                var datanasc = respostaPessoa.dataNascimento;
                                console.log(datanasc);
                                $('#datanascimento').val(datanasc);
                                $('#idestado').val(respostaPessoa.cidade.estado.idEstado);
                                cidade = respostaPessoa.cidade.idCidade;
                                BuscarCidadesPorEstado();
                                var foto = document.getElementById("foto");
                                foto.src = respostaPessoa.foto;   
                            }
                        });
                    }    
                });    
                
            });
        }
        
            function deletar(codigo){
            var id = codigo;
            console.log(codigo);
            Swal.fire({
                title:'Voc? tem certeza ?',
                text: 'Voc? deseja realmente inativar/ativar o fornecedor ?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sim',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed){
                    $.ajax({
                        type:'post',
                        url: '${pageContext.request.contextPath}/FornecedorExcluir',
                        data:{
                            idFornecedor:id
                        },
                        success: 
                            function(data){
                                if (data == 1){
                                    Swal.fire({
                                        position: 'center',
                                        icon: 'success',
                                        title: 'Sucesso',
                                        text: 'Fornecedor inativado com sucesso',
                                        showConfirmButton: true,
                                        timer : 10000                                        
                                    }).then(function(){
                                        window.location.href="${pageContext.request.contextPath}/FornecedorListar";
                                    })          
                                }else{
                                    Swal.fire({
                                        position: 'center',
                                        icon: 'error',
                                        title: 'Erro',
                                        text: 'N?o foi poss?vel inativar fornecedor',
                                        showConfirmButton: true,
                                        timer : 10000           
                                    }).then(function(){
                                        window.location.href="${pageContext.request.contextPath}/FornecedorListar";
                                    })
                                }
                            },
                        error:
                            function(data){
                                window.location.href="${pageContext.request.contextPath}/FornecedorListar";      
                            }
                    });               
                };
            });
        }
        
    
        
        function validarCampos(){        
         console.log('entrei na valida??o de campos:');
            
            if(document.getElementById("nomepessoa").value == ""){
                Swal.fire({
                   position: 'center',
                   icon: 'error',
                   title: 'Verifique o nome do fornecedor!',
                   showConfirmButton: true,
                   timer: 2000
                });
                $("#nomepessoa").focus();
            }else if (document.getElementById("datanascimento").value == ""){
                Swal.fire({
                   position: 'center',
                   icon: 'error',
                   title: 'Verifique a data de nascimento!',
                   showConfirmButton: true,
                   timer: 2000
                });
                $("#datanascimento").focus();
            }else if (document.getElementById("idcidade").value == 'nulo'){
                Swal.fire({
                   position: 'center',
                   icon: 'error',
                   title: 'Verifique a cidade!',
                   showConfirmButton: true,
                   timer: 2000    
                });
                $("#idcidade").focus();
            } else{
                gravarDados();
            }
        }
        
        function gravarDados(){
         console.log("Gravando dados...");
         var target = document.getElementById("foto").src; 
         $.ajax({
            type: 'post',
            url :'FornecedorCadastrar',
            data: {
              idfornecedor : $('#idfornecedor').val(), 
              idpessoa : $('#idpessoa').val(),
              cpfcnpjpessoa : $('#cpfcnpjpessoa').unmask().val(),
              nomepessoa : $('#nomepessoa').val(),
              datanascimento : $('#datanascimento').val(),
              idcidade : $('#idcidade').val(),
              login : $('#login').val(),
              senha : $('#senha').val(),
              enderecoweb : $('#enderecoweb').val(),
              permitelogin : $('#permitelogin').val(),
              situacao : $('#situacao').val(),
              fotopessoa : target              
            },
            success:
                    function (data){
                        console.log('resposta servlet ->');
                        console.log(data);
                        
                        if (data == 1){
                             Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: 'Sucesso',
                                text: 'Fornecedor gravado com sucesso',
                                showConfirmButton: true,
                                timer : 10000
                            }).then(function(){
                                window.location.href="${pageContext.request.contextPath}/FornecedorListar";
                            })  
                        }else{
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Erro',
                                text: 'Nao foi poss?vel gravar fornecedor!',
                                showConfirmButton: true,
                                timer : 10000
                            }).then(function(){
                                window.location.href="${pageContext.request.contextPath}/FornecedorListar";
                            })             
                        }
                    },
                     error:
                            function(data){
                                window.location.href="${pageContext.request.contextPath}/FornecedorListar";      
                            }        
            });
        }
        
        function BuscarCidadesPorEstado(){
            $('#idcidade').empty();
            idEst = $('#idestado').val();
            console.log("entrou buscar estado");

            if (idEst != 'null'){
                console.log("estado = "+ idEst);
                url = "CidadeBuscarPorEstado?idestado="+idEst;
                
                $.getJSON(url, function(result){
                    $.each(result, function(index, value){
                        $('#idcidade').append('<option id="cidade_' + value.idCidade + '"value="'+value.idCidade+'">'+ value.nomeCidade + '</option>');
                        if (cidade !== ''){
                            $('#cidade_' + cidade).prop({selected: true});
                        }else{
                            $('#cidade_').prop({selected: true});   
                        }      
                    });
                }).fail(function (obj, textStatus, error){
                    alert('Erro do servidor: '+textStatus + ', ' + error);
                });
            }    
        }
        
        function uploadFile(){
          var target = document.getElementById("foto");
          target.src = "";
          var file = document.querySelector("input[type='file']").files[0];
          if (file){
              var reader = new FileReader();
              reader.readAsDataURL(file);
              reader.onloadend = function(){
                  target.src = reader.result;
              }; 
          }else{
              target.src = "";     
          }          
        } 
</script>
<%@include file="/footer.jsp" %>
                                 