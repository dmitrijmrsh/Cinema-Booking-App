DROP VIEW IF EXISTS film.all_films_with_genres;

CREATE VIEW film.all_films_with_genres AS
    SELECT
        f.id,
        f.title,
        f.description,
        f.duration_in_minutes,
        g.name AS genre_name,
        g.id AS genre_id
    FROM
        film.films f
    LEFT JOIN
        film.genres g ON f.genre_id = g.id;
