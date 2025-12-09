
DROP TABLE IF EXISTS tb_cliente CASCADE;
DROP TABLE IF EXISTS tb_transacao CASCADE;
DROP TABLE IF EXISTS tb_conta CASCADE;
DROP TABLE IF EXISTS tb_funcionario CASCADE;
DROP TABLE IF EXISTS tb_conta_logs CASCADE;

-- Criacao de tabelas

CREATE TABLE tb_cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL,
    telefone VARCHAR(25) NOT NULL,
    data_nascimento DATE NOT NULL
);

CREATE TABLE tb_conta(
	id SERIAL PRIMARY KEY,
	numero VARCHAR(50) NOT NULL,
	tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('corrente', 'poupanca')),
	saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
	senha VARCHAR(20) NOT NULL,
	
	fk_cliente_id INTEGER,
	CONSTRAINT fk_conta_cliente
		FOREIGN KEY (fk_cliente_id) REFERENCES tb_cliente(id)
);


CREATE TABLE tb_funcionario(
	id SERIAL PRIMARY KEY,
	nome VARCHAR(150) NOT NULL,
	cpf VARCHAR(11) NOT NULL UNIQUE,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(20) NOT NULL,
	telefone VARCHAR(25) NOT NULL,
	data_nascimento DATE NOT NULL,
	salario DECIMAL(10, 2) NOT NULL,
	cargo VARCHAR(50) NOT NULL DEFAULT 'assistente' CHECK (cargo IN ('assistente', 'gerente'))

);

CREATE TABLE tb_transacao(
	id SERIAL PRIMARY KEY,
	tipo VARCHAR(50) NOT NULL,
	valor DECIMAL(15, 2) NOT NULL,
	data_envio TIMESTAMP NOT NULL DEFAULT NOW(),

	fk_conta_destino_id INTEGER,
	CONSTRAINT fk_transacao_conta_destino
		FOREIGN KEY (fk_conta_destino_id) REFERENCES tb_conta(id),

	fk_conta_origem_id INTEGER,
	CONSTRAINT fk_transacao_conta_origem
		FOREIGN KEY (fk_conta_origem_id) REFERENCES tb_conta(id),
		
	CHECK (tipo IN (
    'deposito',
    'saque',
    'transferencia'
	))
);

-- Criacao de tabela para registrar logs de mudancas em saldo (transferencias, saques, etc)

CREATE TABLE tb_conta_logs (
    id SERIAL PRIMARY KEY,
    saldo_antigo DECIMAL(15, 2) NOT NULL,
    saldo_novo DECIMAL(15, 2)  NOT NULL,
    data_mudanca TIMESTAMP DEFAULT NOW() NOT NULL,
	
	fk_conta_id INTEGER NOT NULL,
    CONSTRAINT fk_conta_log
        FOREIGN KEY (fk_conta_id) REFERENCES tb_conta(id)
);

-- Inserindo dados ficticios

INSERT INTO tb_cliente (nome, cpf, email, telefone, data_nascimento) VALUES
('Maria Silva', '12345678901', 'maria@gmail.com', '11987654321', '1990-05-12'),
('João Pereira', '98765432100', 'joao@gmail.com', '21988776655', '1984-09-27'),
('Ana Souza', '45678912333', 'ana@gmail.com', '92977778888', '1995-03-15'),
('Carlos Alberto', '11223344556', 'carlos@gmail.com', '11999994444', '1978-12-02'),
('Fernanda Dias', '66778899001', 'fernanda@gmail.com', '11933335555', '2001-07-27');

INSERT INTO tb_conta (numero, tipo, saldo, senha, fk_cliente_id) VALUES
('0001-1', 'corrente', 1500.00, 'senha123', 1),
('0004-2', 'poupanca', 1500.00, '12345678', 1),
('0001-2', 'poupanca', 2500.00, 'abc12345', 2),
('0002-1', 'corrente', 320.50, 'minhaSenha1', 3),
('0003-1', 'corrente', 9800.00, 'pass7890', 4),
('0003-2', 'poupanca', 5300.00, 'qwerty12', 5);

INSERT INTO tb_funcionario (nome, cpf, email, senha, telefone, data_nascimento, salario, cargo)
VALUES
('Admin', '11122233344', 'admin', 'admin', '11988887777', '1980-02-14', 12100.00, 'gerente'),
('Assistente', '55566677788', 'Assistente', 'assistente', '21955556666', '1992-11-20', 2600.00, 'assistente'),
('Marcos Gerente', '99988877766', 'marcos.gerente@banco.com', 'gerente123', '21922224444', '1975-04-01', 6500.00, 'gerente'),
('Joana Assistente', '33344455522', 'joana.assistente@banco.com', 'assistente123', '92966664444', '1990-10-10', 1800.00, 'assistente');

-- Depositos
INSERT INTO tb_transacao (tipo, valor, fk_conta_destino_id, fk_conta_origem_id) VALUES
('deposito', 500.00, 1, NULL),
('deposito', 300.00, 3, NULL);

-- Saques
INSERT INTO tb_transacao (tipo, valor, fk_conta_destino_id, fk_conta_origem_id) VALUES
('saque', 100.00, NULL, 1),
('saque', 50.00, NULL, 4);

-- Transferencias
INSERT INTO tb_transacao (tipo, valor, fk_conta_destino_id, fk_conta_origem_id) VALUES
('transferencia', 200.00, 2, 1),
('transferencia', 150.00, 5, 3),
('transferencia', 900.00, 4, 5);

-- Materialized view com relatorio resumido de todas as contas

DROP MATERIALIZED VIEW mv_relatorio_contas;

CREATE MATERIALIZED VIEW mv_relatorio_contas AS
    SELECT
        c.id AS conta_id,
        c.numero AS numero_conta,
        c.tipo AS tipo_conta,
        c.saldo AS saldo_conta,
        cl.id AS cliente_id,
        cl.nome AS cliente_nome,
		fn_calcula_idade(cl.data_nascimento) AS idade,
        NOW() AS snapshot_tempo
    FROM tb_conta c
    LEFT JOIN tb_cliente cl ON c.fk_cliente_id = cl.id;

REFRESH MATERIALIZED VIEW mv_relatorio_contas;

SELECT * FROM mv_relatorio_contas;

-- View de seguranca (cpf, senha ocultos)

CREATE OR REPLACE VIEW vw_mostrar_clientes AS
SELECT
    cl.id AS id_cliente,
    cl.nome,
    cl.email,
    cl.telefone,
    cl.data_nascimento,
    c.tipo AS tipo_conta,
    c.numero AS numero_conta,
    c.saldo AS saldo_conta
FROM tb_cliente AS cl INNER JOIN tb_conta AS c ON c.fk_cliente_id = cl.id;

SELECT * FROM vw_mostrar_clientes;

-- Funcao escalar (retorna a idade a partir da data de nascimento)

CREATE OR REPLACE FUNCTION fn_calcula_idade(data_nasc DATE)
RETURNS INTEGER AS $$
DECLARE
    anos INTEGER;
BEGIN
    anos := EXTRACT(YEAR FROM age(data_nasc));
    RETURN anos;
END;
$$ LANGUAGE plpgsql;

-- Selecionando os clientes e mostrando a idade
SELECT id, nome, fn_calcula_idade(data_nascimento) AS idade FROM tb_cliente;


-- Funcao que retorna uma tabela (view com parametros)
-- Retorna transferencias realizadas/recebidas de uma conta em um invervalo de tempo

DROP FUNCTION IF EXISTS fn_extrato_transacoes;

CREATE OR REPLACE FUNCTION fn_extrato_transacoes(conta_id INTEGER, data_inicio DATE, data_final DATE)
RETURNS TABLE(
    fk_conta_id INTEGER,
    saldo_antigo DECIMAL(15, 2),
	saldo_novo DECIMAL(15, 2),
	data_mudanca TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY SELECT c.fk_conta_id, c.saldo_antigo, c.saldo_novo, c.data_mudanca FROM tb_conta_logs AS c WHERE c.fk_conta_id = conta_id 
	AND (c.data_mudanca BETWEEN data_inicio AND data_final) ORDER BY c.data_mudanca DESC;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM fn_extrato_transacoes(1, '1970-01-01', '2025-12-31');


-- Procedure de saque de dinheiro

CREATE OR REPLACE PROCEDURE sp_saque(
    conta_id INTEGER,
    valor DOUBLE PRECISION
) AS $$
BEGIN

	IF NOT EXISTS (SELECT id FROM tb_conta WHERE id = conta_id) THEN
	    RAISE EXCEPTION 'Conta de origem não existe.';
	END IF;
	
    IF valor <= 0 THEN
        RAISE EXCEPTION 'O valor para saque deve ser maior que zero.';
    END IF;

    IF (SELECT saldo FROM tb_conta WHERE id = conta_id) < valor THEN
        RAISE EXCEPTION 'Saldo insuficiente para o saque';
    END IF;

    UPDATE tb_conta SET saldo = (saldo - valor) WHERE id = conta_id;

    -- Registra transação na tabela tb_transacao
    INSERT INTO tb_transacao(tipo, valor, fk_conta_origem_id, fk_conta_destino_id, data_envio) VALUES 
		('saque', valor, conta_id, NULL, NOW());

	RAISE NOTICE 'Valor de R$ % foi sacado da conta de % com sucesso!', valor, 
		(SELECT nome FROM tb_cliente WHERE id = (SELECT fk_cliente_id FROM tb_conta WHERE id = conta_id));
END;
$$ LANGUAGE plpgsql;

CALL sp_saque(4, 20.50);


-- Procedure de depositos

CREATE OR REPLACE PROCEDURE sp_depositar(
    conta_id INTEGER,
    valor DOUBLE PRECISION
) AS $$
BEGIN

	IF NOT EXISTS (SELECT id FROM tb_conta WHERE id = conta_id) THEN
	    RAISE EXCEPTION 'Conta de destino não existe.';
	END IF;

    IF valor <= 0 THEN
        RAISE EXCEPTION 'O valor para depósito deve ser maior que zero.';
    END IF;

    -- Atualiza o saldo
    UPDATE tb_conta 
    SET saldo = saldo + valor 
    WHERE id = conta_id;

    -- Registra a transação
    INSERT INTO tb_transacao(tipo, valor, fk_conta_destino_id, fk_conta_origem_id, data_envio) 
    VALUES ('deposito', valor, conta_id, NULL, NOW());

	RAISE NOTICE 'Valor de R$ % foi depositado na conta de % com sucesso!', 
	    valor,
	    (SELECT nome FROM tb_cliente WHERE id = (SELECT fk_cliente_id FROM tb_conta WHERE id = conta_id));

END;
$$ LANGUAGE plpgsql;


-- Procedure para transferencia entre contas

CREATE OR REPLACE PROCEDURE sp_transferir(
	conta_origem_id INTEGER,
	conta_destino_id INTEGER,
	valor DOUBLE PRECISION
) AS $$
BEGIN

	IF NOT EXISTS (SELECT id FROM tb_conta WHERE id = conta_origem_id) THEN
	    RAISE EXCEPTION 'Conta de origem não existe.';
	END IF;
	
	IF NOT EXISTS (SELECT id FROM tb_conta WHERE id = conta_destino_id) THEN
	    RAISE EXCEPTION 'Conta de destino não existe.';
	END IF;

	IF valor <= 0 THEN
        RAISE EXCEPTION 'O valor para transferencia deve ser maior que zero.';
    END IF;
	
	IF (SELECT saldo FROM tb_conta WHERE id = conta_origem_id) < valor THEN
        RAISE EXCEPTION 'Saldo insuficiente.';
    END IF;

	UPDATE tb_conta SET saldo = (saldo - valor) WHERE id = conta_origem_id;
    UPDATE tb_conta SET saldo = (saldo + valor) WHERE id = conta_destino_id;

    INSERT INTO tb_transacao (tipo, valor, fk_conta_origem_id, fk_conta_destino_id, data_envio) VALUES 
		('transferencia', valor, conta_origem_id, conta_destino_id, NOW());

	RAISE NOTICE 'Valor de R$ % foi transferido da conta de % para % com sucesso!', valor, 
		(SELECT nome FROM tb_cliente WHERE id = (SELECT fk_cliente_id FROM tb_conta WHERE id = conta_origem_id)),
        (SELECT nome FROM tb_cliente WHERE id = (SELECT fk_cliente_id FROM tb_conta WHERE id = conta_destino_id));
END;
$$ LANGUAGE plpgsql;

CALL sp_transferir(1, 2, 100.50);


-- Trigger BEFORE para impedir saldo ficar negativo em contas durante transacoes 

CREATE OR REPLACE FUNCTION fn_impedir_saldo_negativo()
RETURNS TRIGGER AS $$
BEGIN

    IF NEW.saldo < 0 THEN
        RAISE EXCEPTION 'Operação bloqueada: O saldo não pode ficar negativo.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_impedir_saldo_negativo
BEFORE UPDATE ON tb_conta FOR EACH ROW EXECUTE FUNCTION fn_impedir_saldo_negativo();

-- Trigger AFTER para registro de logs quando o saldo da conta sofre alteracoes
CREATE OR REPLACE FUNCTION fn_registrar_mudanca_saldo()
RETURNS TRIGGER AS $$
BEGIN

	INSERT INTO tb_conta_logs(saldo_antigo, saldo_novo, data_mudanca, fk_conta_id) VALUES
		(OLD.saldo, NEW.saldo, NOW(), NEW.id);

	RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_logs_saldo
AFTER UPDATE ON tb_conta
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_mudanca_saldo();

CALL sp_saque(2, 10.00);

-- Transferindo valores pra popular tabela de logs
CALL sp_depositar(1, 500.00);
CALL sp_depositar(3, 300.00);
CALL sp_saque(1, 100.00);
CALL sp_saque(4, 50.00);
CALL sp_transferir(1, 2, 200.00);
CALL sp_transferir(3, 5, 150.00);
CALL sp_transferir(5, 4, 900.00);

-- Criacao de roles
CREATE ROLE assistente WITH LOGIN PASSWORD 'assistente123';
CREATE ROLE gerente WITH LOGIN PASSWORD 'gerente123';

CREATE ROLE login_user WITH LOGIN PASSWORD 'login_user'; -- Role usada pra tela de login no java
GRANT SELECT ON tb_funcionario TO login_user;


-- Permissoes para assistente (pode visualizar tabelas/views, usar algumas funcoes/procedures e inserir valores em algumas tabelas)

GRANT SELECT ON tb_cliente, tb_conta, tb_funcionario, tb_transacao, tb_conta_logs TO assistente;
GRANT INSERT ON tb_transacao, tb_conta, tb_cliente, tb_conta_logs TO assistente;
GRANT UPDATE ON tb_conta TO assistente;

GRANT EXECUTE ON FUNCTION fn_calcula_idade, fn_extrato_transacoes TO assistente;
GRANT EXECUTE ON PROCEDURE sp_saque, sp_depositar, sp_transferir TO assistente;
GRANT SELECT ON vw_mostrar_clientes, mv_relatorio_contas TO assistente;

-- Permissoes para gerente (permissoes do assistente + criar/editar/excluir clientes, contas e funcionarios)

GRANT SELECT, INSERT, UPDATE, DELETE ON tb_cliente, tb_conta, tb_funcionario TO gerente;
GRANT SELECT, INSERT, DELETE ON tb_transacao, tb_conta_logs TO gerente;

GRANT EXECUTE ON FUNCTION fn_calcula_idade, fn_extrato_transacoes TO gerente;
GRANT EXECUTE ON PROCEDURE sp_saque, sp_depositar, sp_transferir TO gerente;
GRANT SELECT, UPDATE ON mv_relatorio_contas TO gerente;
GRANT SELECT ON vw_mostrar_clientes TO gerente;

GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_conta_id_seq TO assistente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_conta_logs_id_seq TO assistente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_transacao_id_seq TO assistente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_cliente_id_seq TO assistente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_funcionario_id_seq TO assistente;

GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_conta_id_seq TO gerente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_conta_logs_id_seq TO gerente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_transacao_id_seq TO gerente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_cliente_id_seq TO gerente;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE tb_funcionario_id_seq TO gerente;