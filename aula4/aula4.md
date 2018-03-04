# Exercício 1

## P1.1

1. Ao executar o comando **sudo anonsurf start** não é possível garantir que se está localizado nos EUA.

2. Ao ligar à rede TOR, o utilizador não tem controlo sobre quais os OR (*onion routers*) a que se conecta. A aplicação TOR do lado do cliente (OP ou *onion proxy*) faz um pedido ao *Directory Server*. Este responde com uma lista de (normalmente 3) OR e o OP cria um circuito composto por esses OR através de um algoritmo que implementa um dado critério de escolha. Nesta ação não existe qualquer tipo de escolha por parte do utilizador que se liga à rede.


# Exercício 2

## P2.1

1. O site acedido foi *https://www.facebookcorewwwi.onion/*. O circuito criado na rede TOR foi:
  * United Kingdom (185.21.216.198)
  * France (5.196.239.114)
  * Canada (192.99.34.48)
  * (relay)
  * (relay)
  * (relay)
  * Onion site

2. Os 6 "saltos" são o resultado de haver anonimato de ambas as partes envolvidas na conexão. Os primeiros 3 saltos correspondem aos OR fornecidos pelo *Directory Server* para o utilizador (Alice) se conectar ao serviço de destino (Bob). Os 3 *relays* correpondem aos OR do circuito criado pelo OP de destino (Bob). Assim, o utilizador acede ao serviço através de um *rendezvous point*, que neste caso corresponde provavelmente ao terceiro OR do circuito (Canada). No caso do utilizador, que é o papel desempenhado neste exercício, tudo o que esteja para além do *rendezvous point* deixa de ser visível, sendo apenas do conhecimento do servidor alvo da conexão. O mesmo acontece no caso do servidor, em que o circuito do utilizador não é conhecido.