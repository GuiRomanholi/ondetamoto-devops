CREATE TABLE moto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(255),
    placa VARCHAR(50) NOT NULL,
    tag VARCHAR(255),
    id_setores BIGINT,
    CONSTRAINT fk_moto_setores FOREIGN KEY (id_setores) REFERENCES setores(id)
);
