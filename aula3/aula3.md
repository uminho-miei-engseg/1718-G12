# Exercício 1

## P1.1

As modificações no código feitas em cada um dos ficheiros tiveram o objetivo, como foi pedido no enunciado, de simplificar o seu uso.

Com a exceção da introdução da mensagem inicial, todos os programas operam agora sem que seja preciso o utilizador introduzir informação manualmente, funcionando à base da leitura e escrita de ficheiros.

O ficheiro initSigner-app.py tem agora dois modos: modo de inicialização, em que se usa a flag -init como argumento para inicializar os componentes e os guardar em ficheiro (guarda num ficheiro chamado "init.txt"). O mesmo programa, se iniciado sem argumentos retorna o valor de R' (pRDashComponents).

O ficheiro generateBlindData-app.py lê o valor de R' (pRDashComponents) que foi gravado em ficheiro pelo programa anterior e gera três valores: blindComponents, pRComponents e blindM. Estes componentes são guardados em ficheiro (requester_componentes.txt) e o componente blindM é impresso no ecrã.

O ficheiro generateBlindSignature-app.py recebe como argumento uma chave privada e, de seguida, pede uma passphrase ao utilizador e lê a blind message (blindM) do ficheiro "requester_components.txt" e o initComponents de "init.txt". Essa blind message é assinada e a blind signature é guardada em ficheiro ("blind_signature.txt") e impressa no ecrã.

O ficheiro unblindSignature-app.py lê três componentes de três ficheiros diferentes: lê a blind signature do ficheiro "blind_signature.txt", o blindComponents de "requester_components.txt" e o pRDashComponents de "init.txt". A assinatura é de seguida desofuscada (unblinded) e é guardada no ficheiro "signature.txt" e impressa no ecrã.

Por fim, o ficheiro verifySignature-app.py recebe uma chave pública (tem de ser a chave pública correspondente à chave privada usada anteriormente em generateBlindSignature-app.py) e pede ao utilizador para introduzir a mensagem original que foi assinada. De seguida, é lida a assinatura de "signature.txt" e os componentes blindComponents e pRDashComponents de "requester_components.txt". Se tudo for válido, é impresso no ecrã o veredicto, ou seja, se a assinatura corresponde à mensagem introduzida ou não. A imagem anexada `p1.1.png` mostra todo o processo no terminal da máquina virtual.

# Exercício 2
## P2.1

Grupo 12 - empresas não bancárias não portuguesas que estão cotadas na Euronext.

Empresas escolhidas: Nokia, Volkswagen, Atari e Axa.


### 1\. 
(anexos estão na pasta /imagens)

### 2\. 
O site com pior cotação foi o site da Atari. Se analisarmos o relatório dado pelo ssllabs, podemos ver que o site tem um número considerável de vulnerablidades.

Em primeiro lugar, é vulnerável ao ataque POODLE, que é um ataque famoso proveniente da utilização so SSL3. Normalmente essa vulnerabilidade pode ser mitigada através da desativação do SSL3, usando o TLS 1.1 e seguintes.
Em segundo lugar, o site usa cifras RC4 com protocolos mais antigos, o que é desaconselhável. Segundo o ssllabs, o RC4 no TLS foi quebrado.
Por último, o site suporta uma série de Cipher Suites que são fracas (WEAK), e estas são identificadas no relatório do ssllabs.

Portanto, é claro que existem sérios problemas de segurança no site da ATARI, o que não é completamente inesperado, sendo que o site não oferece serviços que necessitem de máxima segurança, como serviços bancários. No entanto, as vulnerabilidades devem ser corrigidas para evitar ataques futuros.

### 3\. 
A vulnerabilidade ROBOT permite operações de assinatura e decifragem RSA utilizando a chave privada dum servidor TLS. Isto não permite obter a chave privada do servidor, apenas permite assinar e decifrar mensagens utilizando-a.
Em termos práticos, se um servidor for vulnerável a este ataque, significa que estará a utilizar o algoritmo RSA para cifragem. No entanto, a maioria das conexões TLS modernas utilizam RSA apenas para assinaturas, cifrando com ECDH.

# Exercício 3
## P3.1

Grupo 12 - empresas não portuguesas cotadas na Euronext.

Empresas escolhidas: Nokia, Volkswagen, Atari e Axa.

### 1\.
(anexos estão na pasta /ssh-audit_results)

### 2\.
Os servidores escolhidos da Volkswagen e da Nokia utilizam OpenSSH 5.3.
O servidor escolhido da Atari utiliza OpenSSH 6.6.1p1.
O servidor escolhido da Axa não indica qual o software que utilizam, mas é compatível com OpenSSH 3.9-6.6 e Dropbear SSH 0.53+.

### 3\.
De acordo com o CVE details, o OpenSSH 5.3 tem 10 vulnerabilidades, e o OpenSSH 6.6.1p1 (que se assume ser a versão 6.6 P1) tem 5 vulnerabilidades

### 4\.
Com um _CVSS score_ de 7.8, o OpenSSH 6.6 P1 tem a vulnerabilidade vais grave, seguido do OpenSSH 5.3 com 2 vulnerabilidades com um _CVSS score_ de 7.5.

### 5\.
A vulnerabilidade [CVE-2016-0778](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2016-0778) permite a servidores remotos causarem um _denial of service_. Esta vulnerabilidade foi _patched_ na versão 7.1 P2.