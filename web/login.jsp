<%@taglib prefix="c" uri= "http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="header.jsp"/>

<h1>Sistema Exemplo - CRUD - Deslogado</h1>
<h2>Login Sistema</h2>
    <div>
        <label for="login">Login</label>
        <input type="text" id="login" name="login" required="" placeholder="Coloque seu login"
               onblur="BuscarUsuariosPorNome()">
    </div>
    <div>
        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" required="" placeholder="Coloque sua senha"
               onblur="BuscarUsuariosPorNome()">
    </div>
    <div>
        <label for="tipo">Tipo Usuário</label>
        <select name="tipo" id="tipo" tabindex="2">
            <option value="">Selecione</option>
        </select>
    </div>
<div><button id="submit">Logar</button></div>
</br>
<div id="erro"></div>

<script>
    $(document).ready(function(){
        console.log("Entrei na função...");
        
        $("#submit").on("click", function(){
            console.log("entrei na função click do submit");
            
            if ($('#login').val() === ""){
                $('#login').focus();
                $('#erro').html("<div> Por favor, preencher o campo usuário. </div>").show();
                tempo();
                return false;               
            }
            if ($('#senha').val() === ""){
                $('#senha').focus();
                $('#erro').html("<div> Por favor, preencher o campo senha. </div>").show();
                tempo();
                return false;               
            }
            $('#submit').prop("disabled", true);
            $('#submit').html('<i class="fa fa-spinner" aria-hidden="true"></i> Aguarde....');
            
            $.ajax({
                type : 'post',
                url : 'UsuarioLogar',
                data : {
                    acao :"login",
                    login : $('#login').val(),
                    senha: $('#senha').val(),
                    tipo: $('#tipo').val()
                },
                success: 
                        function(data){
                            if (data == 'ok'){
                                window.location.href = "${pageContext.request.contextPath}/cadastros/homeLogado.jsp"     
                            }else{
                                $('#submit').removeAttr('disabled');
                                $("#submit").html('Entrar');
                                $("#wrapper_error").html("<div class='alert alert-danger'> Usuário ou senha incorreto.</div>").show();
                                tempo();                
                            }
                        },
                error:
                        function (data){
                            RefreshTable();
                        }
            });
        });
        
            function tempo() {
                        setTimeout(function () {
                            $("#wrapper_error").hide();
                        }, 10000); // 3 segundos
                    }
        });
        
        function BuscarUsuariosPorNome(){
            usuario = '';
            console.log("entre na funcion buscarusuariospornome");
            $('#tipo').empty();
            loginUsuario = $('#login').val();
            console.log('Esse é o lgin do usuatio' +loginUsuario);
            
            if (loginUsuario != 'null'){
                console.log("vai rodar o ajax");
                url = "UsuarioBuscarPorLogin?loginUsuario="+loginUsuario;
                console.log('url do ajax: '+url);
                
                $.getJSON(url, function (result){
                    
                    $.each(result, function (index, value){
                        $('#tipo').append(
                        '<option id="usuario_' + value.idUsuario +'"value="' + value.tipo + '">' + value.tipo + '</option>');
                        
                        if (usuario !== ''){
                            $('#usuario_'+usuario).prop({selected: true});
                        }else{
                            $('#usuario_').prop({selected: true});
                        }
                    });     
                    console.log("montou o select"); 
            }).fail(function(obj,textStatus,error){
                alert('Erro do servidor '+textStatus + ', '+ error);
                
            });
        }
    }
</script>
 <%@include file="/footer.jsp"%>
    