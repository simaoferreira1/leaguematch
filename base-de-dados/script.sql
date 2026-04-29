-- BASE DE DADOS LEAGUEMATCH

-- TABELA UTILIZADOR
CREATE TABLE utilizador (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL -- ADMIN, ORGANIZADOR, PARTICIPANTE, ESPECTADOR
);

-- TABELA TORNEIO
CREATE TABLE torneio (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    modalidade VARCHAR(50) NOT NULL,
    regras TEXT,
    formato VARCHAR(50) NOT NULL, -- LIGA, ELIMINATORIAS, GRUPOS
    organizador_id INT NOT NULL REFERENCES utilizador(id)
);

-- TABELA EQUIPA
CREATE TABLE equipa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    torneio_id INT NOT NULL REFERENCES torneio(id)
);

-- TABELA MEMBROS DA EQUIPA
CREATE TABLE team_member (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES utilizador(id),
    team_id INT NOT NULL REFERENCES equipa(id)
);

-- TABELA PARTIDA
CREATE TABLE partida (
    id SERIAL PRIMARY KEY,
    torneio_id INT NOT NULL REFERENCES torneio(id),
    team_a_id INT NOT NULL REFERENCES equipa(id),
    team_b_id INT NOT NULL REFERENCES equipa(id),
    data_hora TIMESTAMP NOT NULL,
    local VARCHAR(100) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'AGENDADO', -- AGENDADO, EM_CURSO, FINALIZADO
    resultado_a INT DEFAULT 0,
    resultado_b INT DEFAULT 0
);

-- TABELA EVENTO DE JOGO
CREATE TABLE evento_jogo (
    id SERIAL PRIMARY KEY,
    match_id INT NOT NULL REFERENCES partida(id),
    tipo VARCHAR(20) NOT NULL, -- GOLO, FALTA, CARTAO
    user_id INT NOT NULL REFERENCES utilizador(id),
    tempo INT NOT NULL
);

-- TABELA CLASSIFICACAO
CREATE TABLE classificacao (
    id SERIAL PRIMARY KEY,
    team_id INT NOT NULL REFERENCES equipa(id),
    torneio_id INT NOT NULL REFERENCES torneio(id),
    pontos INT DEFAULT 0,
    jogos INT DEFAULT 0,
    vitorias INT DEFAULT 0,
    empates INT DEFAULT 0,
    derrotas INT DEFAULT 0,
    golos_marcados INT DEFAULT 0,
    golos_sofridos INT DEFAULT 0
);

-- TABELA NOTIFICACAO
CREATE TABLE notificacao (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES utilizador(id),
    mensagem TEXT NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN DEFAULT FALSE
);