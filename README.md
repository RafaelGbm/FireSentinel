# FireSentinel - Sistema de Monitoramento de Risco de Incêndio

FireSentinel é uma aplicação Spring Boot para monitoramento de risco de incêndio utilizando simulação de cadastro de sensores IoT e suas leituras. A ideia é simular um sistema de monitoramento de risco de incêndio, no registro de sensores, validar suas leituras, e registrar ocorrências de incêndio com base em certas leituras que apresentam riscos.

## 📋 Requisitos do Projeto

1. **Implementação das Classes**
   - Todas as classes foram implementadas conforme o diagrama de classes
   - Utilização de JPA/Hibernate para persistência
   - Banco de dados H2 em memória para desenvolvimento

2. **Métodos Operacionais**
   - Implementação de 3+ métodos operacionais com Javadoc
   - Inclui sobrecarga e sobrescrita de métodos
   - Documentação completa de parâmetros e retornos

3. **Interface de Usuário**
   - Classe principal com menu interativo
   - Entrada de dados via Scanner
   - Validação de entradas

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Maven
- RESTful API (Projeto estruturado para ser utilizado também como API)

## 🏗️ Estrutura do Projeto

```
src/main/java/com/global_solution/
├── config/           # Configurações da aplicação
├── controller/       # Controladores REST
├── dto/              # Objetos de Transferência de Dados
├── model/            # Entidades JPA
├── repository/       # Repositórios de dados
├── service/          # Lógica de negócios
└── FireSentinelMain.java  # Classe principal
```

## 🖥️ Aplicação de Linha de Comando (CLI)

O FireSentinel possui uma interface de linha de comando interativa para gerenciamento local do sistema.

### Funcionalidades Principais

1. **Gerenciamento de Sensores**
   - Cadastro de novos sensores com localização geográfica
   - Listagem de todos os sensores cadastrados
   - Visualização detalhada do status de cada sensor

2. **Registro de Leituras**
   - Cadastro manual de leituras de sensores
   - Detecção automática de situações de risco
   - Histórico de leituras por sensor

3. **Análise de Dados**
   - Cálculo de médias de leituras
   - Análise de variações entre leituras
   - Identificação de tendências de risco

4. **Gerenciamento de Ocorrências**
   - Registro de ocorrências de incêndio
   - Classificação por severidade (1-10)
   - Filtragem por região e criticidade
   - Acompanhamento de ocorrências críticas

### Menu Principal

```
=== Menu Fire Sentinel ===
1. Cadastrar novo sensor
2. Registrar leitura
3. Listar sensores
4. Analisar leituras
5. Registrar ocorrência
6. Listar ocorrências
7. Buscar ocorrências por região
8. Listar ocorrências críticas
9. Sair
```

### Exemplo de Uso

1. **Cadastrando um novo sensor**
   ```
   === Cadastro de Sensor ===
   Nome do sensor: Sensor Sala 101
   Tipo do sensor: Temperatura/Umidade
   Localização: Bloco A - Sala 101
   Latitude: -23.5505
   Longitude: -46.6333
   ```

2. **Registrando uma leitura**
   ```
   === Registro de Leitura ===
   
   Sensores disponíveis:
   1 - Sensor Sala 101
   
   Escolha o ID do sensor: 1
   Temperatura (°C): 32.5
   Umidade (%): 45.2
   Nível de Fumaça (ppm): 15.7
   CO2 (ppm): 520.0
   ```

3. **Analisando leituras**
   ```
   === Análise de Leituras ===
   
   Sensor: Sensor Sala 101
   Temperatura média: 30.2°C
   Umidade média: 48.5%
   Nível médio de fumaça: 12.3 ppm
   CO2 médio: 510.2 ppm
   ```

4. **Registrando uma ocorrência**
   ```
   === Registro de Ocorrência ===
   Região: Bloco A
   Severidade (1-10): 8
   Origem (Sensor/Manual/Denúncia): Sensor
   Descrição: Alta temperatura e fumaça detectadas
   ```

### Executando a Aplicação

1. **Compilação e execução**
   <br>
   Você pode compilar e executar a aplicação pela IDE do IntelliJ IDEA, apenas executando o arquivo <b>FireSentinelMain.java</b>, ou pelo terminal, com os comandos abaixo:
   
   ```bash
   # Navegue até o diretório raiz do projeto
   cd caminho/para/FireSentinel
   
   # Compilar o projeto
   mvn clean package
   
   # Executar a aplicação
   java -jar target/fire-sentinel-App-0.0.1-SNAPSHOT.jar
   ```

### Fluxo de Trabalho Típico

1. Cadastre os sensores que serão monitorados
2. Registre leituras periódicas dos sensores
3. Monitore as análises para identificar situações de risco
4. Registre ocorrências quando necessário
5. Acompanhe as ocorrências críticas nas últimas 24 horas

### Dicas de Uso

- **Cadastro de Sensores**: Certifique-se de registrar a localização exata (latitude/longitude) para melhor rastreamento
- **Leituras**: Registre leituras regularmente para uma análise mais precisa
- **Ocorrências**: Utilize a classificação de severidade para priorizar ações
- **Análise**: Verifique regularmente a análise de leituras para identificar tendências de risco

## 📚 Documentação Adicional

### Métodos Operacionais

1. **calcularRisco** (Sobrecarga)
   ```java
   /**
    * Calcula o risco de incêndio com base nas leituras do sensor
    * @param leitura Dados do sensor (temperatura, umidade, etc.)
    * @return Nível de risco (BAIXO, MEDIO, ALTO, CRITICO)
    */
   public NivelRisco calcularRisco(Leitura leitura)
   
   /**
    * Calcula o risco de incêndio com valores individuais
    * @param temperatura Temperatura em graus Celsius
    * @param umidade Umidade relativa do ar (%)
    * @param fumaca Nível de fumaça detectado
    * @return Nível de risco calculado
    */
   public NivelRisco calcularRisco(double temperatura, double umidade, double fumaca)
   ```

2. **enviarAlerta** (Sobrescrita)
   ```java
   /**
    * Envia alerta quando o nível de risco é alto
    * @param risco Dados da análise de risco
    * @return true se o alerta foi enviado com sucesso
    */
   @Override
   public boolean enviarAlerta(Risco risco)
   ```

3. **buscarSensoresProximos**
   ```java
   /**
    * Busca sensores em um raio específico
    * @param latitude Coordenada de latitude
    * @param longitude Coordenada de longitude
    * @param raioEmKm Raio de busca em quilômetros
    * @return Lista de sensores encontrados no raio
    */
   public List<Sensor> buscarSensoresProximos(double latitude, double longitude, double raioEmKm)
   ```


