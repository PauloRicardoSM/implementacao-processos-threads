package main

import (
	"fmt"
	"time"
)

func tarefaGoRoutine(nome string) {
	for i := 0; i < 5; i++ {
		fmt.Printf("Goroutine %s: %d\n", nome, i)
		time.Sleep(100 * time.Millisecond) // Pausa para simular trabalho
	}
}

func main() {
	// Cria e executa goroutines em segundo plano
	go tarefaGoRoutine("A")
	go tarefaGoRoutine("B")

	// Espera um pouco para as goroutines terminarem
	time.Sleep(1 * time.Second)
	fmt.Println("Função principal finalizada.")
}
