package main

import (
	"fmt"
	"time"
)

func produtor(canal chan<- string) {
	for i := 0; i < 3; i++ {
		mensagem := fmt.Sprintf("Mensagem %d", i)
		canal <- mensagem // Envia a mensagem para o canal
		fmt.Printf("Produtor enviou: %s\n", mensagem)
		time.Sleep(200 * time.Millisecond)
	}
	close(canal) // Fecha o canal quando terminar de enviar mensagens
}

func consumidor(canal <-chan string) {
	for mensagem := range canal { // Loop que lê mensagens do canal até ele ser fechado
		fmt.Printf("Consumidor recebeu: %s\n", mensagem)
		time.Sleep(300 * time.Millisecond)
	}
	fmt.Println("Consumidor: Canal fechado, encerrando.")
}

func main() {
	// Cria um canal de strings
	mensagens := make(chan string)

	// Inicia goroutines para produtor e consumidor
	go produtor(mensagens)
	go consumidor(mensagens)

	// A função principal espera que as goroutines de produtor e consumidor finalizem
	// Para um exemplo simples, podemos usar WaitGroup ou um tempo de espera.
	// Neste caso, um Sleep longo para garantir que tudo ocorra.
	time.Sleep(2 * time.Second)
	fmt.Println("Programa principal finalizado.")
}
