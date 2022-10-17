<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<jsp:include page="/header.jsp"/>
<jsp:include page="/cadastros/menuLogado.jsp"/>

<div class="container-fluid">
    <!-- Page Heading -->
    <h1 class="h3 mb-2 text-gray-800">Imóveis</h1>
    <p class="mb-4">Formulário de Cadastro</p>

    <a class="btn btn-secondary mb-4" href="${pageContext.request.contextPath}/ImovelListar">
        <i class="fas fa-undo-alt"></i>
        <strong>Voltar</strong>
    </a>
    <div class="row">
           <div class="col-lg-5">
                <div class="card shadow mb-4">
                    <div class="card-body">
                        <div class="form-group">
                            <label>Id</label>
                            <input class="form-control" type="text" name="idImovel" id="idImovel"
                                   value="${imovel.idImovel}" readonly="readonly"/>
                        </div>
                        <div class="form-group">
                            <label> Descrição do Imóvel </label>
                            <input class="form-control" type="text" name="descricao" id="descricao"
                                   value="${imovel.descricao}" size="100" maxlength="100"/>
                        </div>
                        <div class="form-group">
                            <label> Endereço: </label>
                            <input class="form-control" type="text" name="endereco" id="endereco"
                                   value="${imovel.endereco}" size="100" maxlength="100"/>
                        </div>
                        
                         <div class="form-group">
                            <label> Valor do Aluguel:  </label>
                            <input class="form-control" type="text" name="valorAluguel" id="valorAluguel"
                               style="text-align:left;"
                               value="<fmt:formatNumber value='${imovel.valorAluguel}' type='currency'/>"/>
                        </div>
                        <!-- Botão de confirmação -->
                        <div class="form-group">
                            <button class="btn btn-success" type="submit" id="submit" onclick="validarCampos()">
                                Salvar </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <style type="text/css">
        .inputfile{
width: 0.1px;
height: 0.1px;
opacity: 0;
overflow: hidden;
position: absolute;
z-index: -1;
}

.inputfile:focus + label{
ouline: 1px dottes #000;
outline: -webkit-focus-ring-color auto 5px;}

.inputfile + label * {
pointer-events: none; }

.borda{
position: relative;
margin: 0 20px 30px 0;
padding: 10px;
border: 1px solid #e1e1e1;
border-radius: 3px;
background: #fff;
-webkit-box-shadow: 0px 0px 3px rgdba(0,0,0,0.06);
-moz-box-shadow: 0px 0px 3px rgba(0,0,0,0.06);
box-shadow: 0px 0px 3px rgba(0,0,0,0.06); }

    </style>
    <script>
        
          
        function validarCampos(){
            console.log('entrei na validação de campos:');
            
            if(document.getElementById("descricao").value == ""){
                Swal.fire({
                   position: 'center',
                   icon: 'error',
                   title: 'Verifique a descrição do imovel!',
                   showConfirmButton: false,
                   timer: 1000
                });
                $("#descricao").focus();
            }else if (document.getElementById("endereco").value == ""){
                Swal.fire({
                   position: 'center',
                   icon: 'error',
                   title: 'Verifique o endereco do imovel!',
                   showConfirmButton: false,
                   timer: 1000
                });
                $("#endereco").focus();
            }else if (document.getElementById("valorAluguel").value == ""){
                Swal.fire({
                   position: 'center',
                   icon: 'error',
                   title: 'Verifique o valor do imovel!',
                   showConfirmButton: false,
                   timer: 1000    
                });
                $("#valorAluguel").focus();
            } else{
                gravarDados();
            }
        }
        
      
        function gravarDados(){
            console.log("Gravando dados....");
            $.ajax({
                type : 'post',
                url :'ImovelCadastrar',
                data:{
                    idImovel : $('#idImovel').val(),
                    descricao : $('#descricao').val(),
                    endereco : $('#endereco').val(),
                    valorAluguel : $('#valorAluguel').val()
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
                                text: 'Imovel gravado com sucesso',
                                showConfirmButton: true,
                                timer : 10000
                            }).then(function(){
                                window.location.href="${pageContext.request.contextPath}/ImovelListar";
                            })  
                        }else{
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Erro',
                                text: 'Nao foi possível gravar imovel!',
                                showConfirmButton: true,
                                timer : 10000
                            }).then(function(){
                                window.location.href="${pageContext.request.contextPath}/ImovelListar";
                            })             
                        }
                    },
                error: 
                    function (data){
                       window.location.href="${pageContext.request.contextPath}/ImovelListar";
                    }
            });
        }
            
        $(document).ready(function() {
            console.log('entrei ready');
            
            $('#valorAluguel').maskMoney({
                prefix: 'R$',
                suffix: '',
                allowZero : false,
                allowNegative: true,
                allowEmpty : false,
                doubleClickSelection : true,
                selectAllOnFocus : true,
                thousands: '.',
                decimal :',',
                precision: 2,
                affixesStay : true,
                bringCareAtEndOnFocus: true
            })
                
            $('#datatable').DataTable({
                "oLanguage" : {
                    "sProcessing": "Processando...",
                    "sLengthMenu" : "Mostrar _MENU_ registros",
                    "sZeroRecords" : "Nenhum registro encontrado",
                    "sInfo" : "Mostrando de _START_ até _END_ de _TOTAL_ registros",
                    "sInfoEmpty" : "Mostrando de 0 até 0 de 0 registros",
                    "sInfoFiltered" : "",
                    "sInfoPostFix" : "",
                    "sSearch" : "Buscar:",
                    "sUrl" :"",
                    "oPaginate" : {
                        "sFirst" : "Primeiro",
                        "sPrevious" : "Anterior",
                        "sNext" : "Seguinte",
                        "sLast" : "Último"
                    }
                }
            });              
        });
        
        
        
    </script>
    <%@include file="/footer.jsp" %>
    
   
    
            
