#include <stdio.h>
#include <stdlib.h>

void vulneravel (char *matriz, size_t x, size_t y, char valor) {
        int i, j;
        matriz = (char *) malloc(x*y);
        for (i = 0; i < x; i++) {
                for (j = 0; j < y; j++) {
                        matriz[i*y+j] = valor;
                }
        }
}

int main() {
	size_t x = 2147483650, y = 1;
	//size_t x = 70000, y = 1;
	char * matriz;
	printf("x = %d\ny = %d\nx * y = %d bytes\n", x, y, x * y);
	vulneravel(matriz, x, y, 'a');
	return 0;
}
