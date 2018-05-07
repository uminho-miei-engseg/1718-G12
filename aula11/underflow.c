#include <stdio.h>
#include <stdlib.h>
#include <string.h>

const int MAX_SIZE = 2048;


void vulneravel (char *origem, size_t tamanho) {
        size_t tamanho_real;
        char *destino;
        if (tamanho < MAX_SIZE) {
                tamanho_real = tamanho - 1; // Não copiar \0 de origem para destino
                destino = (char *) malloc(tamanho_real);
                memcpy(destino, origem, tamanho_real);
        }
}

int main() {
	char * string = (char *) malloc(2);
	int i;
	for(i = 0; i < 35; ++i){
		string[i] = '0' + i;
	}
	vulneravel(string, MAX_SIZE-1);
	return 0;
}
