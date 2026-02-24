<div align="center">
  <h1>Task Manager API - Agendador de Tarefas</h1>
  <p><i>Microsserviço responsável pelo núcleo das regras de negócio e gerenciamento de tarefas.</i></p>
</div>

<hr>

<h2>📝 Descrição do Projeto</h2>
<p>Esta API é o motor do ecossistema, gerenciando a criação, agendamento e o ciclo de vida de todas as tarefas. Ela processa as requisições enviadas pelo BFF e garante a persistência segura das informações.</p>

<p><b>Este serviço é parte integrante de uma arquitetura de microsserviços.</b> Para entender o fluxo completo de dados, acesse o repositório do orquestrador:</p>
<p>🔗 <b>BFF Orquestrador:</b> <a href="https://github.com/Ja0Santana/BFF-Agendador">github.com/Ja0Santana/BFF-Agendador</a></p>

<hr>

<h2>🐋 Docker Hub - Imagem Oficial</h2>
<p>A imagem isolada deste serviço pode ser obtida via:</p>
<pre><code>docker pull joaopaul0/api-tarefas:latest</code></pre>

<hr>

<h2>🛠️ Tecnologias e Ferramentas</h2>
<ul>
  <li><b>Java 17+ & Spring Boot 3</b></li>
  <li><b>PostgreSQL</b> (Armazenamento de tarefas e status)</li>
  <li><b>Docker</b> (Ambiente isolado e conteinerizado)</li>
  <li><b>SonarQube</b> (Análise estática de código e segurança)</li>
  <li><b>Swagger/OpenAPI</b> (Interface de teste para os endpoints de tarefas)</li>
</ul>

<hr>

<h2>🛡️ Qualidade e Engenharia</h2>
<ul>
  <li><b>SOLID:</b> Estrutura modular para fácil manutenção do fluxo de tarefas.</li>
  <li><b>Clean Code:</b> Código escrito com foco em legibilidade e testabilidade.</li>
  <li><b>Inspeção Contínua:</b> Monitoramento constante de débitos técnicos e vulnerabilidades.</li>
</ul>

<hr>

<h2>🚦 Como Rodar Localmente</h2>
<ol>
  <li>Clone o repositório: <code>git clone https://github.com/Ja0Santana/agendador-tarefas.git</code></li>
  <li>Certifique-se de possuir uma instância do <b>PostgreSQL</b> ativa.</li>
  <li>Execute: <code>./gradlew bootRun</code></li>
</ol>

<hr>
