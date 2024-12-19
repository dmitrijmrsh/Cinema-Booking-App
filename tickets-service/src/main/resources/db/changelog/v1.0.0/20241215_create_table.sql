CREATE SCHEMA IF NOT EXISTS ticket;

CREATE TABLE IF NOT EXISTS ticket.tickets (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    screening_id INT NOT NULL,
    seat_id INT NOT NULL
);
