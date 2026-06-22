<div align="center">

<img src="https://img.shields.io/badge/Sistema_Bancário-Gerenciamento-1a56db?style=for-the-badge&logoColor=white" alt="Sistema Bancário" />

<br/><br/>

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=flat-square&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![CSS](https://img.shields.io/badge/CSS-1572B6?style=flat-square&logo=css3&logoColor=white)
![JasperReports](https://img.shields.io/badge/JasperReports-Relatórios-darkgreen?style=flat-square)
![Status](https://img.shields.io/badge/status-concluído-brightgreen?style=flat-square)

<br/>

**Sistema desktop de gerenciamento bancário desenvolvido com Java + JavaFX e integração com banco de dados PostgreSQL.**

</div>

---

## 📖 Sobre o Projeto

Sistema de gerenciamento de um banco que permite o controle completo de **clientes**, **contas**, **funcionários** e **transações** por meio de uma interface gráfica moderna desenvolvida com **JavaFX**. Os dados são persistidos em banco **PostgreSQL** e o sistema conta com geração de **relatórios** via JasperReports.

---

## 🎯 Funcionalidades

- 👤 **Gerenciamento de clientes** — cadastro, edição, busca e remoção
- 👔 **Gerenciamento de funcionários** — cadastro e controle de acesso
- 🏦 **Gerenciamento de contas** — abertura, consulta e encerramento
- 💸 **Transações bancárias** — depósitos, saques e transferências
- 📋 **Extratos** — histórico de movimentações por conta
- 📊 **Geração de relatórios** — exportação via JasperReports
- 🔍 **Busca avançada** — views SQL para consulta de clientes e contas
- 🔐 **Login** — autenticação de funcionários

---

## 🛠 Tecnologias

| Tecnologia | Uso |
|---|---|
| Java | Linguagem principal |
| JavaFX + Scene Builder | Interface gráfica (arquivos `.fxml`) |
| CSS | Estilização dos componentes JavaFX |
| PostgreSQL | Banco de dados relacional |
| SQL | Modelagem, queries e views |
| JasperReports / Jasper Studio | Geração de relatórios (`.jasper`) |
| BrModelo | Modelagem conceitual e lógica do banco |
| IntelliJ IDEA | IDE de desenvolvimento |
| Flaticon | Ícones da interface |

---

## 🏗 Estrutura do Projeto

```
src/
│
├── main/
│   ├── java/
│   │   ├── com/projetodb/projetobancojava/
│   │   │   │
│   │   │   ├── control/                      # Lógica auxiliar de controle
│   │   │   ├── dao/                          # Acesso ao banco (JDBC)
│   │   │   ├── database/                     # Conexão com o PostgreSQL
│   │   │   ├── model/                        # Entidades (Cliente, Conta, etc.)
│   │   │   ├── util/                         # Utilitários gerais
│   │   │   ├── view/                         # Componentes visuais auxiliares
│   │   │   │
│   │   │   ├── Principal.java                # Ponto de entrada da aplicação
│   │   │   ├── LoginController.java          # Tela de login
│   │   │   ├── HomeController.java           # Tela principal (home)
│   │   │   ├── PrincipalController.java      # Navegação entre telas
│   │   │   ├── ClientesController.java       # CRUD de clientes
│   │   │   ├── ContasController.java         # CRUD de contas
│   │   │   ├── FuncionariosController.java   # CRUD de funcionários
│   │   │   ├── TransacoesController.java     # Depósitos, saques, transferências
│   │   │   ├── ExtratosController.java       # Histórico de movimentações
│   │   │   ├── BuscarClienteController.java  # View de busca de clientes
│   │   │   └── BuscarContaController.java    # View de busca de contas
│   │   │
│   │   └── module-info.java
│   │
│   └── resources/
│       ├── com/projetodb/projetobancojava/   # Telas FXML (Scene Builder)
│       │   ├── login.fxml
│       │   ├── principal.fxml
│       │   ├── home.fxml
│       │   ├── clientes.fxml
│       │   ├── contas.fxml
│       │   ├── funcionarios.fxml
│       │   ├── transacoes.fxml
│       │   ├── extratos.fxml
│       │   ├── buscarCliente.fxml
│       │   └── buscarConta.fxml
│       │
│       ├── css/
│       │   ├── styles.css                    # Estilo global
│       │   ├── loginStyles.css               # Estilo da tela de login
│       │   └── tablesStyles.css              # Estilo das tabelas
│       │
│       ├── images/                           # Ícones e imagens (Flaticon)
│       │
│       └── relatorios/
│           └── contas.jasper                 # Relatório compilado
│
├── .gitignore
├── dbprojetobancosql.sql                     # Script completo do banco
└── README.md
```

---

## 🗄 Banco de Dados

O banco foi modelado com o **BrModelo** (modelo conceitual e lógico) e implementado no **PostgreSQL**. O script completo está em [`dbprojetobancosql.sql`](./dbprojetobancosql.sql), incluindo tabelas, views de busca e dados iniciais.

---

## 🖼 Capturas de Tela

<img width="701" alt="Tela de login" src="https://github.com/user-attachments/assets/4f2b511e-4123-4b39-856c-4639a8b121b2" />

<img width="1039" alt="Tela de clientes" src="https://github.com/user-attachments/assets/1dfbff94-c900-48ed-ac1f-4ad2d079ab30" />

<img width="1039" alt="Tela de contas" src="https://github.com/user-attachments/assets/38c7b084-00c9-4ce5-a72a-cb3deb6a65b1" />

<img width="1040" alt="Transações" src="https://github.com/user-attachments/assets/00420e08-fea6-457b-9750-e566031d2c67" />

<img width="1038" alt="Busca de clientes" src="https://github.com/user-attachments/assets/8f059e95-9a2c-436c-b762-ef377e304eb0" />

<img width="1037" alt="Busca de contas" src="https://github.com/user-attachments/assets/f9d03862-e5a7-455f-80d1-699915e7b1c1" />

<img width="1037" alt="Relatório" src="https://github.com/user-attachments/assets/db220d8e-a911-4551-ae52-2eafbae06743" />

---

## 🎥 Vídeos de Demonstração

- 📽️ [Apresentação geral — classes e funções](https://drive.google.com/file/d/1shPHOC5YgYTCkasCTDho65PC8F8VFewu/view?usp=sharing)
- 📽️ [Atualização — views de busca e correção de atualização de tabela](https://drive.google.com/file/d/1GJ97VNu3YPoX6Wgi2EQqGJ23tcnibIxL/view?usp=sharing)

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+
- IntelliJ IDEA com suporte a JavaFX
- PostgreSQL instalado e rodando

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd projetobancojava
```

### 2. Criar o banco de dados

```bash
psql -U postgres -f dbprojetobancosql.sql
```

### 3. Configurar a conexão

Ajuste as credenciais no arquivo `database/Conexao.java` (ou equivalente):

```java
String url    = "jdbc:postgresql://localhost:5432/<nome-do-banco>";
String user   = "<usuario>";
String senha  = "<senha>";
```

### 4. Executar

Abra o projeto no IntelliJ IDEA e execute `Principal.java`.

---

## 👥 Equipe

Projeto desenvolvido para a disciplina de **Programação de Computadores III** do curso de **Sistemas de Informação — IFPR Campus Palmas**.
