# Projeto de Leilões

## Descrição Geral do Sistema

O projeto tem como objetivo a criação de um sistema de leilões de carta fechada, ou seja, leilões em que cada licitação feita por um participante é desconhecida por todas as restantes entidades do sistema, quer estas sejam outros participantes, quer sejam entidades responsáveis pelo sistema.

De forma a garantir o correto funcionamento do sistema, é necessário garantir, antes de mais, que a informação secreta presente em cada licitação se mantém secreta, sendo protegida de todas as entidades participantes no sistema.
Cada licitação deverá ser, assim, cifrada de uma forma que impeça que o seu conteúdo (valor da licitação) seja acedido antes do final do leilão.

Uma possível solução, que achamos estar de acordo com as boas práticas de segurança, é utilizar criptografia assimétrica para proteger os conteúdos de cada licitação. A nível de implementação, a que talvez faça mais sentido usar poderá ser a cifra RSA (para efeito de exemplificação, referir-nos-emos sempre à cifra RSA quando descrevermos uma cifra assimétrica).

Assim, para cada leilão, existirá um par único de chaves RSA, em que a chave pública é conhecida por todos os utilizadores.

A chave privada de cada leilão corresponde à informação necessária para decifrar todas as licitações cifradas durante o período em que esteve ativo. Por isso, de forma a garantir que nenhuma entidade possui a informação necessária para decifrar licitações antes do tempo, optamos por utilizar a técnica *Shamir Secret Sharing*, em que a chave privada de cada leilão é separada em várias componentes distribuidas por várias entidades diferentes. No final de cada leilão, todas as entidades que possuem uma parte da chave disponibilizam essa parte para poder decifrar o conteúdo das licitações.

É importante salientar que a incapacidade de decifrar as licitações antes do tempo constitui o cerne do sistema criado. O objetivo é que não haja qualquer forma de determinar o tipo de licitações que são feitas durante o leilão em questão.

Cada utilizador pode, sempre que desejar, não só participar num leilão mas também criar um leilão, para vender um certo artigo. Um utilizador que tenha o título de vendedor num certo leilão possui, ele próprio, uma parte da chave privada desse leilão (constitui, assim, uma das entidades externas ao sistema de leilões que tem parte da chave).

O *backend* da aplicação, ou seja, a "entidade" principal do sistema, pode ser imaginada como sendo a empresa de leilões, e trata da gestão de todo o sistema de criação de leilões, comunicação com os utilizadores e comunicação com entidades externas (devido à divisão de chaves).


Podemos, então, definir o sistema de uma forma geral como sendo composto por 4 entidades diferentes (já um pouco na prespetiva da programação):

1. Utilizador (comprador ou vendedor);
2. Empresa de Leilões (*backend*, possui todos os leilões criados e informações (chaves públicas) de cada utilizador);
3. Leilão (classe que contém licitações, participantes, etc);
4. Entidade Externa de Confiança (1 ou mais).


## Anonimato

Como foi descrito acima, as licitações são protegidas através de criptografia assimétrica, garantindo assim a segurança da informação. No entanto, é necessário especificar outro componente essencial quando se trata de segurança de *software*, nomeadamente o anonimato dos intervenientes.

O sistema a criar deve conseguir identificar os intervenientes de uma forma única e inequívoca, de forma a poder saber exatamente quem licitou e quanto, para determinar um vencedor. No entanto, de forma a proteger ao máximo a identidade de quem participa no sistema, optamos por utilizar mecanismos que não exigem que se conheça informação pessoal de nenhum utilizador.

Para que tal seja possível, é, mais uma vez, necessário recorrer a técnicas de criptografia assimétrica ou, mais precisamente, técnicas de assinatura digital.

Qualquer utilizador que queira participar num dado leilão pede ao sistema (*backend*) para se registar nesse mesmo leilão, sendo a única informação sua que manda a sua chave pública de assinatura (referir-nos-emos ao par de chaves como chaves DSA, mas não tem de ser necessariamente este o algoritmo). Esta chave serve como *ID* do utilizador no sistema, e é a única informação que o sistema tem de cada utilizador. Qualquer prova de identidade é dada pela assinatura do utilizador, que pode ser sempre comprovada pelo sistema, visto ter sempre a chave pública correspondente.

## RGPD

Uma vez que o sistema não guarda nem processa qualquer informação pessoal do utilizador (mesmo endereços *IP* não são guardados), não há nenhum tipo de cuidado adicional no tratamento desses dados, pelo que o **RGPD** não é aplicável neste caso concreto.


## Descrição dos Diagramas

De forma a ter uma maior noção de como funcionará todo o sistema, foram criados vários diagramas que especificam de que forma as várias entidades interagem entre si.

### Utilizador - *Backend*

O primeiro diagrama diz respeito à interação entre o Utilizador, no papel de participante, e o *Backend*. Qualquer comunicação entre os dois intervenientes é feita através de uma ligação SSL.

A ligação SSL é utilizada não só para proteger a informação de adversários passivos (da forma como o sistema está previsto, um adversário passivo nada pode fazer mesmo sem uma comunicação SSL) como também de adversários ativos.

Toda a validação de dados feita tanto do lado do *Backend* como do Utilizador é feita através de assinaturas digitais. Do lado do *Backend*, a validação da assinatura do Utilizador é feita através da chave pública fornecida por ele aquando do registo num dado leilão. Do lado do Utilizador, a verificiação de assinaturas do *Backend* é feita através da sua chave pública, conhecida por todos os intervenientes no sistema (a distribuição da chave pública do *Backend* pode ser feita de várias formas, por exemplo, através da apresentação de certificados ou então disponibilizando-a de origem em cada aplicação pertencente ao sistema).

O Utilizador, no início da comunicação, antes do registo, gera um número aleatório que é associado a todos os dados por si assinados (e associado também ao valor licitado, de forma a não poder ser deduzido qual o valor através da cifragem do mesmo valor por outro Utilizador). Esse número aleatório é associado ao Utilizador para cada Leilão.

### Vendedor - *Backend*

Cada Utilizador, como foi referido anteriormente, pode desempenhar, simultaneamente, as funções de participante e vendedor de um leilão, mas, obviamente, nunca no mesmo Leilão.

A interação entre o Vendedor e o *Backend* não difere muito da anterior, havendo comunicação SSL entre as duas entidades e verificação de autenticidade e integridade através de assinaturas digitais, seguindo o mesmo esquema que foi referido na subsecção anterior.

Cada Vendedor, quando solicita a criação de um novo Leilão ao *Backend*, fornece a sua chave pública DSA, que serve de *ID* único no sistema. Cada Leilão tem, assim, registada a chave pública do proprietário do produto. Qualquer licitação feita pela entidade detentora dessa chave é rejeitada.

O Vendedor, ao criar um Leilão, recebe uma parte da chave privada correspondente, mantendo-a até ao final desse Leilão.


### Entidade Externa de Confiança - *Backend*

O sistema pode ser consituído por uma ou mais entidades externas, cujo único objetivo é manter alguns dos fragmentos de chaves privadas de Leilões, podendo assim separar a informação devidamente, não dando a possibilidade de a aplicação de *Backend* poder aceder às licitações de um Leilão antes do tempo.

Como foi já discutido com o professor, para simplificar a implementação, cada uma das entidades externas, que em situações reais seriam aplicações separadas e independentes do *Backend*, poderão ser antes classes ou objetos pertencentes ao sistema. É importante salientar que esta solução não seria aceitável em situações reais, sendo considerada apenas para demonstração do conceito. Mesmo assim, cada entidade terá a sua parte da sua chave privada como variável *private*, cujo método para aceder terá restrições equivalentes às que se esperaria se cada entidade externa fosse uma aplicação independente com comunicação SSL com o *Backend*.


### *Backend* - Leilão

O sistema de *backend* tem um conjunto de Leilões (existe a classe Leilão, e cada Leilão é uma instância dessa classe), em que cada Leilão é criado aquando do pedido de um Vendedor que quer vender um determinado artigo. Como o Leilão é um objeto pertencente ao *backend*, toda a comunicação entre estas entidades se resume ao uso de funções implementadas na classe Leilão, daí não ser preciso haver qualquer necessidade de recorrer a certificados ou comunicações por SSL ou uso de qualquer técnica criptográfica para proteger dados de "mensagem", pois não há qualquer transferência de dados pela rede.

Cada Leilão, quando é criado, recebe uma série de parâmetros (Data no diagrama) que correpondem a informações que identificam o vendedor, o início e o término do Leilão, a artigo ou artigos a serem vendidos, entre outras informações.

De cada vez que um utilizador licita, o *backend*, após validar a licitação, insere-a no Leilão, associada ao utilizador respetivo (depois do utilizador ser registado).

A responsabilidade de terminar o Leilão pertence ao *backend* (esta opção poderá ser alterada mais tarde, no decorrer do projeto, se se considerar que não é a melhor opção), que, depois de invocar o término, fornece a chave privada desse Leilão (correspondente à chave pública usada pelos utilizadores para cifrar as licitações) de forma a que todas as licitações possam ser decifradas e verificadas, de forma a encontrar o vencedor. Depois de encontrado, o vencedor é comunicado ao *backend*, que por sua vez é responsável de informar o respetivo utilizador, bem como o vendedor.

No final de cada Leilão, o *backend* guarda um *log* do que aconteceu no Leilão. No entanto, não guarda informações relativas a licitações, excetuando a chave pública do vencedor, como prova do sucedido. O *log* será sempre assinado de forma a garantir a sua integridade e autenticidade, podendo também ser cifrado para garantir confidencialidade.

### Notas finais

Toda a projeção feita até agora poderá ser alterada na implementação de acordo as necessidades na altura, ou devido a novas ideias que o grupo acredite serem melhores que as que lhes precedem ou que complementem o sistema de alguma forma.

Os diagramas, no mesmo sentido, são apenas uma forma de representar graficamente a projeção geral do sistema, não representando exatamente o que o sistema fará depois de implementado.
