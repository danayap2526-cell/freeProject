CREATE DATABASE IF NOT EXISTS brightfalls_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE brightfalls_db;

CREATE TABLE usuarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(64)  NOT NULL,
    victorias   INT DEFAULT 0,
    derrotas    INT DEFAULT 0,
    empates     INT DEFAULT 0,
    creado_en   DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE partidas (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    jugador1_id     INT NOT NULL,
    jugador2_id     INT NOT NULL,
    ganador_id      INT,
    duracion_seg    INT,
    jugada_en       DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (jugador1_id) REFERENCES usuarios(id),
    FOREIGN KEY (jugador2_id) REFERENCES usuarios(id),
    FOREIGN KEY (ganador_id)  REFERENCES usuarios(id)
);