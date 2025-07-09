package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

// Estrutura que representa um pedido
type Pedido struct {
	ID         int
	Produto    string
	Valido     bool
	Pago       bool
	Processado bool
}

func main() {
	// Criação dos canais para cada etapa do pipeline
	novosPedidos := make(chan Pedido)
	pedidosValidados := make(chan Pedido)
	pedidosPagos := make(chan Pedido)
	pedidosProcessados := make(chan Pedido)

	// WaitGroup para esperar o término de todas as goroutines
	var wg sync.WaitGroup

	// Inicia os serviços (goroutines) para cada etapa
	wg.Add(4)
	go servicoValidacao(novosPedidos, pedidosValidados, &wg)
	go servicoPagamento(pedidosValidados, pedidosPagos, &wg)
	go servicoEstoque(pedidosPagos, pedidosProcessados, &wg)
	go servicoNotificacao(pedidosProcessados, &wg)

	// Gerador de novos pedidos (simulação)
	go func() {
		for i := 1; i <= 10; i++ {
			novoPedido := Pedido{
				ID:      i,
				Produto: fmt.Sprintf("Produto-%d", i),
			}
			fmt.Printf("✅ Novo pedido recebido: ID %d\n", novoPedido.ID)
			novosPedidos <- novoPedido
			time.Sleep(time.Duration(rand.Intn(500)) * time.Millisecond) // Simula intervalo entre pedidos
		}
		close(novosPedidos) // Fecha o canal quando todos os pedidos forem enviados
	}()

	// Aguarda o término de todos os serviços
	wg.Wait()
	fmt.Println("🎉 Todos os pedidos foram processados!")
}

// Serviço de Validação
func servicoValidacao(entrada <-chan Pedido, saida chan<- Pedido, wg *sync.WaitGroup) {
	defer wg.Done()
	defer close(saida) // Fecha o canal de saída quando terminar

	for pedido := range entrada {
		// Simula tempo de processamento
		time.Sleep(time.Duration(rand.Intn(300)) * time.Millisecond)

		// Validação do pedido (exemplo simples)
		pedido.Valido = pedido.ID%2 == 0 // Apenas IDs pares são válidos

		if pedido.Valido {
			fmt.Printf("🟢 Pedido %d validado com sucesso\n", pedido.ID)
			saida <- pedido
		} else {
			fmt.Printf("🔴 Pedido %d rejeitado na validação\n", pedido.ID)
		}
	}
}

// Serviço de Pagamento
func servicoPagamento(entrada <-chan Pedido, saida chan<- Pedido, wg *sync.WaitGroup) {
	defer wg.Done()
	defer close(saida)

	for pedido := range entrada {
		time.Sleep(time.Duration(rand.Intn(400)) * time.Millisecond)

		// Simulação de pagamento (sempre bem-sucedido neste exemplo)
		pedido.Pago = true
		fmt.Printf("💳 Pagamento processado para pedido %d\n", pedido.ID)
		saida <- pedido
	}
}

// Serviço de Estoque
func servicoEstoque(entrada <-chan Pedido, saida chan<- Pedido, wg *sync.WaitGroup) {
	defer wg.Done()
	defer close(saida)

	for pedido := range entrada {
		time.Sleep(time.Duration(rand.Intn(500)) * time.Millisecond)

		// Atualiza estoque e marca como processado
		pedido.Processado = true
		fmt.Printf("📦 Estoque atualizado para pedido %d\n", pedido.ID)
		saida <- pedido
	}
}

// Serviço de Notificação
func servicoNotificacao(entrada <-chan Pedido, wg *sync.WaitGroup) {
	defer wg.Done()

	for pedido := range entrada {
		time.Sleep(time.Duration(rand.Intn(200)) * time.Millisecond)
		fmt.Printf("✉️ Cliente notificado sobre pedido %d: %s\n", pedido.ID, pedido.Produto)
	}
}
