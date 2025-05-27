
🔧 Controllers REST implementados
SensorController
Gerencia os sensores IoT (cadastro, atualização, consulta)
Endpoints: /sensores para operações CRUD
LeituraController
Recebe e gerencia as leituras dos sensores
Endpoints: /leituras para registro e consulta de dados
RiscoController
Realiza análise de risco de incêndio usando IA
Endpoints: /risco/analisar e /risco/{sensorId}
📁 Estrutura do projeto
Model: Entidades JPA (Sensor, Leitura, Risco)
DTO: Objetos de transferência de dados
Repository: Interfaces de persistência com Spring Data JPA
Service: Interfaces e implementações da lógica de negócios
Controller: APIs REST para comunicação externa
Config: Configurações da aplicação
⚙️ Características implementadas
Persistência de dados usando JPA/Hibernate
Banco de dados H2 em memória para desenvolvimento
Integração com API de IA externa para análise de risco
Tratamento de erros e fallback em caso de falha na API externa
Consultas geoespaciais para localizar sensores por área
🌐 Integração com IA
O sistema está configurado para chamar uma API externa de IA em http://localhost:8000/api/analisar-risco. Você precisará implementar essa API separadamente, possivelmente usando Python/FastAPI conforme mencionado anteriormente.

