CREATE OR REPLACE FUNCTION film.get_all_films()
RETURNS TABLE (
    id INT,
    title VARCHAR,
    description TEXT,
    duration_in_minutes INT,
    genre_name VARCHAR,
    genre_id INT
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.title,
                v.description,
                v.duration_in_minutes,
                v.genre_name,
                v.genre_id
            FROM film.all_films_with_genres v;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.get_films_by_genre_name(film_genre_name VARCHAR)
RETURNS TABLE (
    id INT,
    title VARCHAR,
    description TEXT,
    duration_in_minutes INT,
    genre_name VARCHAR,
    genre_id INT
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.title,
                v.description,
                v.duration_in_minutes,
                v.genre_name,
                v.genre_id
            FROM
                film.all_films_with_genres v
            WHERE
                v.genre_name = film_genre_name;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.get_film_by_id(film_id INT)
RETURNS film.all_films_with_genres AS '
    BEGIN
        RETURN (
            SELECT
                v
            FROM
                film.all_films_with_genres v
            WHERE
                v.id = film_id
         );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.save_film(
    film_title VARCHAR,
    film_description TEXT,
    film_duration_in_minutes INT,
    film_genre_id INT
)
RETURNS film.all_films_with_genres AS '
    DECLARE
        new_film film.films;
    BEGIN
    INSERT INTO film.films (title, description, duration_in_minutes, genre_id)
    VALUES (film_title, film_description, film_duration_in_minutes, film_genre_id)
    RETURNING * INTO new_film;
    RETURN (
        SELECT
            v
        FROM
            film.all_films_with_genres v
        WHERE
            v.id = new_film.id
    );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.update_film(
    film_id INT,
    film_title VARCHAR,
    film_description TEXT,
    film_duration_in_minutes INT,
    film_genre_id INT
)
RETURNS film.all_films_with_genres AS '
    BEGIN
        UPDATE film.films
        SET
            title = film_title,
            description = film_description,
            duration_in_minutes = film_duration_in_minutes,
            genre_id = film_genre_id
        WHERE
            id = film_id;
        RETURN (
            SELECT
                v
            FROM
                film.all_films_with_genres v
            WHERE
                v.id = film_id
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE film.delete_film_by_id(film_id INT)
AS '
    BEGIN
        DELETE FROM
            film.films
        WHERE
            id = film_id;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.check_film_exists_by_id(film_id INT)
RETURNS BOOLEAN AS '
    DECLARE
        exists BOOLEAN;
    BEGIN
        SELECT
            COUNT(*) INTO exists
        FROM
            film.films
        WHERE
            id = film_id;

        RETURN exists;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.check_film_exists_by_title(film_title VARCHAR(255))
RETURNS BOOLEAN AS '
    DECLARE
        exists BOOLEAN;
    BEGIN
        SELECT
            COUNT(*) INTO exists
        FROM
            film.films
        WHERE
            title = film_title;

        RETURN exists;
    END;
'
LANGUAGE plpgsql;