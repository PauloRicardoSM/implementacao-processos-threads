import multiprocessing
import os

def tarefa_filho():
    """Função que será executada pelo processo filho"""
    print(f"📌 Processo FILHO (PID: {os.getpid()}) executando tarefa")

if __name__ == "__main__":
    # Mostra o PID do processo principal
    print(f"🟢 Processo PAI (PID: {os.getpid()}) iniciando")
    
    # Cria um novo processo
    processo = multiprocessing.Process(target=tarefa_filho)
    
    # Inicia o processo filho
    processo.start()
    print(f"🔄 Processo PAI criou filho com PID: {processo.pid}")
    
    # Aguarda o processo filho terminar
    processo.join()
    print("🔴 Processo PAI: filho terminou")