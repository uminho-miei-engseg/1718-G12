# 1. Risco

## P1.1

A avaliação do risco a que cada uma das entidades está sujeita resulta dos valores que considerarmos no seu cálculo através das fórmulas fornecidas no enunciado:

```

risco =	probabilidade de ataque	ter	sucesso	* impacto

probabilidade do ataque	ter	sucesso	= nível	da ameaça * grau de vulnerabilidade

```

A avaliação do risco estará sempre dependente do tipo de peso que dermos a cada uma das variáveis, o qual depende um pouco da análise subjetiva do problema.
As três componentes que fazem parte da avaliação de risco são:

1. Impacto;
2. Nível de ameaça;
3. Grau de vulnerabilidade.

Se considerarmos que um sistema de *homebanking* de um Banco tem um **grau de vulnerabilidade** baixo, na medida em que tem um sistema de segurança mais avançado e, à partida, bem desenvolvido e adaptado para lidar com vários tipos de ameaças, podemos assumir que a probabilidade de um ataque ter sucesso será mais baixa do que num PC doméstico. No entanto, o nível da ameaça é maior num sistema de *homebanking*, pois haverá sem dúvida muito mais entidades a tentarem atacar esse sistema. o entanto, sabendo que, estatisticamente, há tentativas de ataque a qualquer computador genérico constantemente (algo à volta de um ataque por cada 30 segundos), podemos admitir que o nível de ameaça a um PC doméstico não é tão baixo que compense pelo aumento do grau de vulnerabilidade. Assim, um sistema de *homebanking* poderá ter uma **probabilidade do ataque ter sucesso mais baixa**, pois tem um grau de vulnerabilidade bastante inferior do que o de um PC doméstico e um nível de ameaça que não apresenta uma diferença tão grande para um PC doméstico como a diferença existente no grau de vulnerabilidade.

No entanto, tendo em atenção que a probabilidade do ataque ter sucesso poderá não ter diferenças muito significativas entre um PC doméstico e um sistema de *homebanking*, o cálculo do **risco** estará fortemente dependente do impacto causado.
O impacto causado por um ataque num PC doméstico, numa escala geral, é muitíssimo inferior do que o impacto causado num sistema de *homebanking*, medindo o número de pessoas afetadas pelo ataque e as consequências desse mesmo ataque.

Assim, apesar da probabilidade de sucesso ser maior no PC doméstico, a diferença acentuada no impacto do ataque leva a que o risco seja maior no sistema de *homebanking*.

## P1.2

1. A descoberta e encarceramento de cibercriminosos reduz o número de entidades que possam ameaçar o sistema, pelo que a variável cujo valor é reduzido é o **nível da ameaça**.

2. Ao remover vulnerabilidades do sistema o fator da fórmula que se diminui é o **grau de vulnerabilidade**.

## P2.1

O **RGPD** deve ser tido em conta na fase de requisitos, uma vez que é nessa fase que se faz o levantamento de requisitos de segurança, tendo em conta a legislação em vigor e normas internacionais (de acordo com a página 14 dos slides da aula 9).

Claro está que a preocupação em manter o desenvolvimento do projeto de acordo com o **RGPD** deve existir em qualquer fase, de forma a garantir que o que foi definido nos requisitos está conforme com o que é implementado.

## P2.2

No modelo *Microsoft Security Development Lifecycle* a fase em que se deve ser tido em conta o **RGPD** é também a fase de requisitos, que, como diz na página 16 dos slides, se deve dar de forma análoga à fase de requisitos do modelo em cascata. No entanto, é provavelmente boa ideia ter já o **RGPD** em conta na fase de formação, de forma a treinar os membros da equipa de uma forma que engloba as políticas presentes nesse regulamento.

Como no caso do modelo em cascata, o cuidado de implementar o *software* tendo em conta o **RGPD** é também fundamental.

## P2.3

1. O **RGPD** deve ser tido em conta na função de negócio *Governance*, na prática de segurança *Policy and Complience*, mas também na função de negócio *Construction* na prática de segurança *Security Requirements*.

2. O nível de maturidade das duas práticas de segurança deve ser o 3, uma vez que é necessário que cada membro da equipa tenha uma compreensão sólida e coerente das práticas de segurança necessárias para pôr em prática as políticas do **RGPD**, e ainda que tenha conhecimentos (demonstráveis) sobre as implicações que tais políticas possam ter no *software* a desenvolver, bem como conhecimentos de como implementar essas políticas.

