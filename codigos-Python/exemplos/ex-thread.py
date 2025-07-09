import threading
import time

def tarefa():
    print("Thread iniciou trabalho")
    time.sleep(2)  # Simula trabalho demorado
    print("Thread terminou trabalho")

# Cria a thread
thread = threading.Thread(target=tarefa)

print("Main: Vou iniciar a thread")
thread.start()  # Inicia a thread

print("Main: Thread está rodando, vou aguardar...")
thread.join()  # Espera a thread terminar

print("Main: Thread foi encerrada")