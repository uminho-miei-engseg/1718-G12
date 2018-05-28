# Aula14

## P 1.1 (String *SQL* Injection)

Neste primeiro problema o objetivo é conseguir obter uma tabela com todos os números de cartão de crédito. Para isso, colocou-se a *string* **' or '1'='1** no campo de utilizador para que o **SELECT** na base de dados o faça para todos os utilizadores, em vez de ser para o utilizador que é suposto estar a aceder.

## P 1.2 (Numeric *SQL* Injection)

Neste problema o objetivo é conseguir obter a tabela de todos os dados meteorológicos. No entanto, como o valor de *station* não pode ser escolhido pelo utilizador, foi necessário aceder ao código **HTML** da página e alterar o código de uma das regiões (Columbia) de 101 para **101 or 1=1**.

## P 1.3 (Database Backdoors)

Neste problema o objetivo é conseguir alterar o valor do salário do utilizador com o *ID* 101. Para tal, primeiro consultou-se a tabela para conhecer os campos do utilizador ou, mais especificamente, o valor do salário. De seguida, para conseguir alterar o valor do salário colocou-se um segundo comando *SQL* no campo destinado ao *ID* de utilizador. Assim, o comando final ficou algo do género **SELECT * FROM employee WHERE userid = 101; UPDATE employee SET salary=6000000 WHERE userid=101**, em que **101; UPDATE employee SET salary=6000000 WHERE userid=101** é o valor do campo destinado apenas ao *ID* de quem faz a *query*.

## P 2.1

O objetivo deste pproblema é conseguir executar um *script* no *browser* do utilizador através do formulário no *site*. Depois de testar vários campos, detetou-se que no campo do código secreto do cartão havia potencial para poder executar um *script*, pelo que se colocou o *script* **\<script>alert("SSA!!!")\</script>** nesse campo e se conseguiu assim obter a mensagem **SSA!!!** no *browser*.

## P 3.1

O objetivo deste problema é conseguir obter a *password* de um utilizador do sistema explorando a falha no sistema que não põe limite ao número de tentativas erradas de tentar adivinhar a resposta à pergunta secreta que o serviço implementa. Como normalmente num sistema que se tenta atacar é útil conseguir ter privilégios de administrador, o utilizador que escolhemos para descobrir a *password* foi o *admin*. Depois de duas tentativas, descobrimos que a *password* era *green*.
