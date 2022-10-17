/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  igorb
 * Created: 06/10/2021
 */

create table estado (
idestado serial primary key,
nomeestado varchar(100) not null, 
siglaestado varchar(2) not null
);

insert into estado (nomeestado, siglaestado) values ('São Paulo','SP');

create or replace view usuario as 
	select p.idpessoa, p.nome, p.cpfcnpj, p.login, p.senha, 
	c.idcliente as id, 'Cliente' as tipo
from pessoa p, cliente c
where c.idpessoa = p.idpessoa and c.situacao = 'A' and c.permitelogin = 'S'
union 
select p.idpessoa, p.nome, p.cpfcnpj, p.login, p.senha, 
f.idfornecedor as id, 'Fornecedor' as tipo
from pessoa p, fornecedor f
where f.idpessoa = p.idpessoa and f.situacao = 'A' and f.permitelogin = 'S'
union 
select p.idpessoa, p.nome, p.cpfcnpj, p.login, p.senha, 
a.idadministrador as id, 'Administrador' as tipo
from pessoa p, administrador a
where a.idpessoa = p.idpessoa and a.situacao = 'A' and a.permitelogin = 'S'