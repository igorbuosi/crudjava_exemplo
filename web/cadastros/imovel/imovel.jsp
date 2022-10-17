<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="iso-8859-1" %>
<jsp:include page="/header.jsp"/>
<jsp:include page="/cadastros/menuLogado.jsp"/>

<div class="container-fluid">
    <!-- Page Heading -->
    <h1 class="h3 mb-2 text-gray-800"> Imóveis </h1>
    <p class="mb-4"> Planilha de Registros </p>
    
    <a class="btn btn-success mb-4" href="${pageContext.request.contextPath}/ImovelNovo"
       <i class="fas fa-stick-note"></i>
    <strong> Novo </strong>
    </a>
       
    <div class="card shadow">
        <div class="card-body">       
            <table id="datatable" class="display">
                <thead>
                    <tr>
                        <th align="left">ID</th>
                        <th align="left">Descrição</th>
                        <th align="center">Endereço</th>
                        <th align="center">ValorAluguel</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="imovel" items="${imoveis}">
                        <tr>
                            <td align="right">${imovel.idImovel}</td>
                            <td align="left">${imovel.descricao}</td>
                            <td align="left">${imovel.endereco}</td>
                            <td align="left"><fmt:formatNumber value="${imovel.valorAluguel}" type="currency" /> </td>
                            </td> 
                        </tr>
                    </c:forEach>            
                </tbody>
            </table>
        </div>
    </div>
</div>
   

    <script>
        $(document).ready(function() {
            console.log('entrei ready');
            
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
    
            
