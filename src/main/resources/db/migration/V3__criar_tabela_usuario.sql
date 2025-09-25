CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    estabelecimento_id BIGINT,
    CONSTRAINT fk_usuario_estabelecimento FOREIGN KEY (estabelecimento_id) REFERENCES estabelecimento(id)
);
