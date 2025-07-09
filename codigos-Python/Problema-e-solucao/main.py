# Importa as funções necessárias do módulo `codigo`
# Cada uma dessas funções representa um componente funcional:
# - sensores simulam coleta de dados
# - processar_alertas trata os dados gerando alertas
# - criar_fila_alertas cria a fila de comunicação entre os componentes
from codigo import sensor_temperatura, sensor_umidade, sensor_luminosidade, processar_alertas, criar_fila_alertas

# Importa módulos para uso de threads, processos e controle de tempo
import threading
import multiprocessing
import time

# Função principal que inicia o sistema
def main():
    # Cria a fila de alertas que será compartilhada entre sensores (threads) e processador (processo)
    # Essa fila pode ser uma multiprocessing.Queue() ou algo semelhante
    alertas = criar_fila_alertas()
    
    # Lista de sensores que serão executados como *threads*
    # Cada thread recebe a fila de alertas como argumento e roda como *daemon* (encerram junto com o programa principal)
    sensores = [
        threading.Thread(target=sensor_temperatura, args=(alertas,), daemon=True),
        threading.Thread(target=sensor_umidade, args=(alertas,), daemon=True),
        threading.Thread(target=sensor_luminosidade, args=(alertas,), daemon=True)
    ]
    
    # Processo separado para processar os alertas recebidos na fila
    # Esse processo também recebe a fila de alertas como argumento e roda como daemon
    alertas_process = multiprocessing.Process(target=processar_alertas, args=(alertas,), daemon=True)
    alertas_process.start()  # Inicia o processo de tratamento de alertas
    
    # Inicia todas as threads dos sensores
    for sensor in sensores:
        sensor.start()

    # Mensagem inicial ao usuário
    print("Sistema iniciado. Pressione Ctrl+C para parar.")
    
    # Mantém o programa principal em execução indefinidamente até que seja interrompido manualmente (Ctrl+C)
    try:
        while True:
            time.sleep(1)  # Evita que o loop consuma CPU inutilmente
    except KeyboardInterrupt:
        # Quando o usuário pressiona Ctrl+C, o programa é finalizado com esta mensagem
        print("\nEncerrando o sistema...")

# Verifica se o script está sendo executado diretamente (e não importado como módulo)
if __name__ == "__main__":
    main()
