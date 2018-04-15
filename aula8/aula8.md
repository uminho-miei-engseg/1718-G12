# Exercício 1

## Pergunta P1.1

1. Assumindo que existirão entre 5 a 50 _bugs_ por cada 1000 linhas de código:
   - o Facebook, com 61 milhões linhas de código, terá entre 305 mil e 3.05 milhões de _bugs_;  
   - o software de automóveis modernos, com 100 milhões linhas de código, terá entre 500 mil e 5 milhões de _bugs_;  
   - Linux 3.1, com 15 milhões linhas de código, terá entre 75 mil e 750 mil _bugs_;  
   - todos os serviçoes Internet da Google, com 2 mil milhões de linhas de código, terá entre 10 milhões e 100 milhões de _bugs_.

2. É difícil saber exatamente quantos *bugs* de cada sistema conduzem a vulnerabilidades, bem como quais dessas vulnerabilidades são passíveis de serem exploradas, simplesmente porque não é óbvio por vezes se cada falha de *software* tem ou não implicações de segurança do sistema.

## Pergunta P1.2

1. Vulnerabilidades de projeto:
	1. Falha no levantamento de requisitos, por exemplo, não incluir no plano do projeto uma fase de verificação de acesso por parte das várias entidades que possam ter acesso ao sistema;

	2. Durante a fase de planeamento do projeto, pode ficar decidido implementar um sistema que use algoritmos com vulnerabilidades conhecidas, que representarão em si uma vulnerabilidade para todo o sistema.

	3. Algumas vulnerabilidades podem ser introduzidas deliberadamente. Por exemplo, se a *password* de um *router* for fraca de forma a que seja facilmente lembrada pelos utilizadores, isso constitui uma vulnerabilidade, mas apenas porque foi decidido previamente que assim seria.

2. Vulnerabilidades de codificação:
	1. Uma vez que uma vulnerabilidade de codificação pode resultar, por exemplo, de um bug, podemos considerar uma vulnerabilidade de codificação o caso do *buffer overflow*, caso em que o programador não teve os devidos cuidados na forma como a memória do programa é gerida;

	2. Outra vulnerabilidade pode ser, por exemplo, a do caso do *heartbleed*, uma vulnerabilidade ao nível da implementação do código na biblioteca OpenSSL que levou ao *leak* de vários dados privados (como chaves privadas) de vários serviços que utilizam essa biblioteca. Esta vulnerabilidade é particularmente grave uma vez que se localiza ao nível da implementação de uma biblioteca que é muito usada para vários serviços em que a segurança de dados e correspondente privacidade são essenciais.

3. Vulnerabilidades operacionais:

	1. Um exemplo de vulnerabilidade operacional será o de, por exemplo, num dado ambiente como o de uma empresa, poder haver entidades estranhas ao sistema a terem acesso a informações confidenciais por descuido de quem tem acesso a tais informações (documentos confidenciais abertos num computador com o ecrã ligado e desbloqueado).

	2. Poderá também haver possíveis vulnerabilidades que resultem da configuração de funcionalidades do sistema por parte de entidades sem conhecimentos para tal, o que pode comprometer a segurança de todo o sistema.
	

A correção das vulnerabilidades de projeto pode implicar uma restruturaçao do sistema todo, o que poderia resultar em gastos de dinheiro e tempo proibitivos para uma organização, mas também pode implicar que apenas seja necessário modificar componentes específicos do sistema, o que pode ser exequível do ponto de vista da organização.

Na correção de vulnerabilidades de codificação a dificuldade pode muitas vezes estar associada à deteção da vulnerabilidade, que por vezes pode ser difícil. A correção de um *bug* de *software* que esteja a introduzir uma vulnerabilidade deste tipo poderá normalmente passar por um ou mais *patches*. No entanto, essa medida nem sempre resolve o problema em mãos, podendo introduzir mais vulnerabilidades ou podendo simplesmente não ser exequível a aplicação de um *patch* em certos sistemas.

A correção de vulnerabilidades operacionais passa por haver uma política rigorosa de práticas corretas a ter quando se lida com dados confienciais, de forma a que entidades terceiras não lhes possam aceder, quer deliberadamente, quer por acidente.

## Pergunta P1.3

Vulnerabilidades dia-zero são vulnerabilidades pre-existentes no código que os _developers_ do _software_ não conhecem, e que são descobertas e atacadas antes que sejam desenvolvidos _patches_ para as mesmas.