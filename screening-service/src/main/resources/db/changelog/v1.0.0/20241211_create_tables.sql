CREATE SCHEMA IF NOT EXISTS screening;

CREATE TABLE IF NOT EXISTS screening.screenings (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    start_time TIME NOT NULL,
    film_id INT NOT NULL,
    hall_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS screening.seats (
    id SERIAL PRIMARY KEY,
    row_number INT NOT NULL,
    seat_in_row INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    screening_id INT NOT NULL REFERENCES screening.screenings(id) ON DELETE CASCADE
);