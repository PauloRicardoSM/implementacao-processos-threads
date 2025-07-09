import threading                  # Para execução de sensores em paralelo (threads)
import multiprocessing            # Para o processo separado que lida com alertas
import random                     # Para simular valores dos sensores
import time                       # Para controlar intervalo entre leituras
from datetime import datetime     # Para gerar timestamp (horário atual)

# Lock para evitar que múltiplas threads imprimam ao mesmo tempo no terminal
log_lock = threading.Lock()

def criar_fila_alertas():
    """Cria e retorna uma fila de alertas compartilhada entre threads e processos"""
    return multiprocessing.Queue()

def sensor_temperatura(alertas, intervalo=0.5):
    """Simula leituras de temperatura a cada 0.5 segundos"""
    while True:
        # Gera valor de temperatura aleatório entre 15.0°C e 35.0°C
        valor = random.uniform(15.0, 35.0)
        timestamp = datetime.now().strftime('%H:%M:%S')  # Marca o horário da leitura
        
        # Garante exclusividade no print
        with log_lock:
            print(f'[{timestamp}] Temperatura: {valor:.1f}°C')
        
        # Gera alerta se a temperatura ultrapassar 25.0°C
        if valor > 25.0:
            alertas.put(f'ALERTA: Temperatura alta {valor:.1f}°C às {timestamp}')
        
        time.sleep(intervalo)

def sensor_umidade(alertas, intervalo=1.0):
    """Simula leituras de umidade a cada 1 segundo"""
    while True:
        try:
            # Gera valor de umidade aleatório entre 40% e 90%
            valor = random.uniform(40.0, 90.0)

            # Verificação de tipo defensiva (útil em ambientes reais)
            if not isinstance(valor, (int, float)):
                print(f"Valor realmente inválido detectado: {valor} (Tipo: {type(valor)})")
                continue

            timestamp = datetime.now().strftime("%H:%M:%S")
            
            with log_lock:
                print(f"[{timestamp}] Umidade: {valor:.1f}%")
            
            # Gera alerta se a umidade estiver abaixo de 50%
            if valor < 50.0:
                alertas.put(f"ALERTA: Umidade baixa {valor:.1f}% às {timestamp}")
            
            time.sleep(intervalo)
        
        except Exception as e:
            # Trata qualquer erro que ocorra na leitura
            print(f"Erro no sensor de umidade: {str(e)}")
            time.sleep(1)

def sensor_luminosidade(alertas, intervalo=1.5):
    """Simula leituras de luminosidade a cada 1.5 segundos"""
    while True:
        # Gera valor aleatório de luminosidade entre 0% e 100%
        valor = random.uniform(0.0, 100.0)
        timestamp = datetime.now().strftime('%H:%M:%S')

        with log_lock:
            print(f'[{timestamp}] Luminosidade: {valor:.1f}%')
        
        # Gera alerta se a luminosidade for inferior a 30%
        if valor < 30.0:
            alertas.put(f'ALERTA: Luminosidade baixa {valor:.1f}% às {timestamp}')
        
        time.sleep(intervalo)

def processar_alertas(alertas):
    """Processo separado para lidar com os alertas recebidos"""
    while True:
        if not alertas.empty():
            alerta = alertas.get()  # Retira o alerta da fila
            print(f"\n⚠️ {alerta}\n")  # Exibe o alerta com destaque
        time.sleep(0.1)  # Pausa curta para evitar uso excessivo da CPU
