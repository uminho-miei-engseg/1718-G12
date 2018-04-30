## P1.1

Tanto no programa *Java* como no *Python* existe já um controlo de acesso de memória que impede a escrita numa zona de memória que não esteja previamente declarada para o efeito. Assim, se se declara que um *buffer* tem tamanho 10, se se tentar escrever no índice 10 (posição 11), os programas são erro, uma vez que a posição de memória que à qual se está a tentar aceder não pertence ao *buffer*.

No programa em C++ o mesmo não acontece, uma vez que não existe controlo de acesso automático por parte do sistema. Assim, ao tentar escrever no índice 10 do mesmo *buffer*, o programa simplesmente escreve ná posição de memória adjacente. Como as variáveis locais do programa são guardadas na *stack*, a variável declarada anteriormente (variável *test*) situa-se adjacente à última posição do *buffer*, pelo que é nessa variável que será escrito o valor que está destinado ao *buffer*. É devido à falta de controlo de acessos que o programa em C++. contrariamente aos outros 2, não dá erro.


## P1.2

O que sucede nesta situação é semelhante ao que acontecia nos programas anteriores, em que tanto no *Java* como no *Python* os programas impedem o acesso a regiões de memória fora do que foi declarado na criação do *buffer*. No entanto, no programa *C++* o programa permite o acesso a posições de memória fora do *buffer* sem qualquer problema.

Se pedirmos para guardar 10 valores, de forma a preencher o *buffer*, e de seguida pedirmos o valor de um índice maior, o que sucede é que conseguimos aceder a um valor numa certa posição de memória que não pertence ao *buffer* (pode inclusive, e é o que acontece neste caso, aceder a um valor fora da *stack*).

## P1.3

Nos dois programas existe uma vulnerabilidade de *buffer overflow* na *stack*.

No programa *0-simple.c* o objetivo é conseguir modificar uma variável *control*, com o valor 0, que está adjacente a um *buffer*, tal como acontecia nos casos anteriores. Para obter a mensagem *"You Win"*, é necessário que essa variável deixe de ter esse valor. O programa pede um *input* ao utilizador para guardar no *buffer*. Como o *input* não é verificado, basta o utilizador introduzir uma *string* suficientemente grande para aceder à variável *control* através da indexação do *buffer*.

No programa *RootExploit.c* a situação é semelhante, sendo o objetivo o de conseguir alterar a variável *pass* de forma a conter um valor diferente de 0 e, assim, permitir obter a mensagem de sucesso. Para alterar a *pass*, sem para isso colocar a *password* correta, é preciso aceder à variável *pass* através o *buffer* adjacente onde é colocada a *password* introduzida no *input*. Como é apenas necessário mudar o valor de *pass* para algo que seja diferente de 0, e como o *buffer* tem tamanho 4 (4 *bytes*, porque é *char*), basta introduzir uma *password* em que o quinto elemento seja um carater com o valor **ASCII** acima de 0. Ao introduzir essa *password*, será devolvida uma mensagem a dizer que a *password* está errada e depois outra mensagem a dizer que se obteve o acesso.


## P1.4

A primeira situação que é fácil de reparar é a de que o valor de *p* passa a ser igual ao de *buf* caso a função *fgets* não retorne *NULL*.

A segunda situação, que é sem dúvida mais importante, é a de que o programa não verifica qual o tamanho máximo permitido de carateres a serem introduzidos como *input*. Assim, um utilizador poderia meter como limite máximo, por exemplo, 150 carateres e, de seguida, o programa iria buscar 150 carateres ao *buffer*, o que, como só tem capacidade 100, significaria que o programa iria ler 50 *bytes* de memória fora da *stack* (porque o *buffer* é a primeira variável local do programa). Isso seria particularmente perigoso no caso em que pudessem estar chaves privadas guardadas algures em memória, porque o utilizador poderia pedir um valor grande de elementos ao programa e adquirir informação de zonas de memória distantes a *stack* do programa.


## P1.5

A dificuldade de modificar um valor de uma variável específica para um valor específico vem da necessidade de saber exatamente onde esta está, qual o seu tipo (quanto espaço ocupa), se a arquitetura é *little endian* ou *big endian* e saber como colocar lá o valor.

Sabendo que a arquitetura em ambientes **UNIX** é *little endian*, sabemos que para introduzir dois ou mais *bytes* para obter um número específico estes têm de ser colocados numa ordem tal que permita que o *byte* menos significativo fique numa posição de memória com um enderço mais baixo. Assim, para introduzir o valor *0x61626364*, é necessário intorduzir o *byte* 0x64 primeiro, e assim sucessivamente.

Como a variável está próxima do *buffer*, basta pazer *print* em cada índice do *buffer* de um valor qualquer até chegar ao cocal da memória em que se quer colocar um valor específico, e depois fazer *print* do valor nesse local (se for um *int*, é preciso fazer *print* de 4 *bytes* respeitando a arquitetura *little endian*).


## P1.6

Para conseguir invocar a função *win* foi necessário colocar o seu endereço no valor de *fp*.

Para conseguir colocar o valor nessa variável foi necessário obter o valor do endereço da função em hexadecimal e, de seguida, colocar como *input* para o buffer os *bytes* necessários para aceder a *fp*. Os carateres colocados nessa posição de memória correspondem aos *bytes* do endereço da função *win*. Ao conseguir colocar em *fp* os *bytes* corretos quando o endereço de memória em *fp* é usado na invocação este corresponderá ao endereço de *win*.


## P1.7

No exercício anterior, o objetivo era conseguir invocar uma função através do seu endereço. No entanto, o código em si, certa altura, chamava uma função à qual teria de se atribuir o endereço de *win*.

Neste exercício a função *main* nunca invoca qualquer função, logo de forma a conseguir invocar a função *win* é necessário substituir o endereço na posição de memória que corresponde ao endereço de retorno na própria *main*. Assim, quando o programa terminar, executa a função *win*.

Conhecendo a estrutura básica da *stack* de uma função, sabemos que, depois de alocados os parâmetros, são alocados, respetivamente, o endereço de retorno e o endereço do *base pointer* da *stack*, cada um com 8 *bytes* de memória. De seguida, são alocadas as variáveis locais.

A única variável local do programa é o *buffer* de 64 *bytes*, que é onde a *string* de *input* é guardada.

Para redirecionar o retorno da *main* para a função *win*, é necessário substituir o endereço de retorno a 8 *bytes* de distância do final do *buffer*, de forma a ultrapassar também o *base pointer*.

Recorrendo ao *gdb*, ficamos a saber que o endereço de *win* é 0x555555554740 que, em *little endian*, corresponde à *string* *@GUUUU*.

Assim, é fácil substituir os valores do endereço de retorno normais pelos desejados, bastando inserir 72 (64 + 8) *bytes* arbitrários como *input* antes de inserir a *string* *@GUUUU*.


### Nota

Foram tirados alguns *screenshots* durante o trabalho de forma a poder mostrar que, de facto, os problemas foram resolvidos. Esses *screenshots* correspondem às imagens em anexo.