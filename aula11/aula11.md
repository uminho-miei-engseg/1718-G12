# Exercício 1

## Pergunta P1.1

1. A vulnerabilidade da função _vulneravel()_ é a diferença entre que valores _size_t_ e _int_ podem tomar. O primeiro ciclo, por exemplo, apenas termina quando _i_ for maior ou igual a _x_. Uma vez que _x_ é do tipo _size_t_ com um tamanho de 8 _bytes_, o seu valor máximo será 2^64, quando o valor máximo de _i_,  um _int_ de 4 _bytes_, será 2^31-1. Caso _x_ contenha um valor maior que o máximo dos _int_, o ciclo tentará incrementar _i_, causando um _overflow_.

2. Está em anexo o ficheiro _overflow.c_ que exemplifica uma tentativa de demonstrar a vulnerabilidade mencionada anteriormente.

3. Esperava-se que o programa desse algum erro relacionado com acesso a apontadores fora do _array_ criado. No entanto, isso não se verificou. Verificou-se, porém, através de _debug_, que o ciclo termina assim que _i_ atinge o valor 2^31 (valor máximo para _int_ + 1),  altura em que a condição do _for_ retorna valor 0.


## Pergunta P1.2

1. A vulnerabilidade da função _vulneravel()_ é o problema que é indicado nas notas do _man memcpy_, isto é, não se verifica se as zonas de memória fazem _overlap_, podendo levar a um funcionamente inesperado da função. Além disso, visto que o tamanho da zona que se pretende copiar não é testado para que seja um valor positivo, pode lançar um _segmentation fault_ caso o parâmetro do tamanho da função tenha valor 0.

2. Está em anexo o ficheiro _underflow.c_ que exemplifica uma tentativa de demonstrar a vulnerabilidade mencionada acima.

3. Uma vez que não é verificado se as zonas de memória que se vai copiar com _memcpy_ se sobrepõem, não é lançado qualquer erro, pelo que o programa executa e termina sem "problemas".


## Pergunta P1.3

1. Tal como no caso do programa _underflow.c_, a função _vulneravel()_ tem a vulnerabilidade associada à função _memcpy_ em que o tamanho das zonas de cópia não são verificadas para detetar sobreposições.

2. Apesar de ser semelhante ao problema anterior, segue em anexo uma tentative de demonstrar a vulnerabilidade mencionada acima.

3. Assim como no problema anterior, e tal como mencionado anteriormente, não são verificadas sobreposições das zonas de memória, pelo que o _memcpy_ executa sem problemas visíveis, apesar do seu comportamente não ser previsível.
