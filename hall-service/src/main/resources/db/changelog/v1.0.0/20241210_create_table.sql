CREATE SCHEMA IF NOT EXISTS hall;

CREATE TABLE IF NOT EXISTS hall.halls(
    id SERIAL NOT NULL PRIMARY KEY,
    is_active BOOLEAN NOT NULL,
    row_count INT NOT NULL,
    seat_in_row_count INT NOT NULL
);