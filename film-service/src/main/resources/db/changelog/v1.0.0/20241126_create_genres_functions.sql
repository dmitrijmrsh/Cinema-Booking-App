CREATE OR REPLACE FUNCTION film.get_all_genres()
RETURNS TABLE (id INT, name VARCHAR) AS '
    BEGIN
        RETURN QUERY
            SELECT
                g.id,
                g.name
            FROM
                film.genres g;
    END;'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.get_genre_by_id(genre_id INT)
RETURNS film.genres AS '
    BEGIN
        RETURN (
            SELECT
                g
            FROM
                film.genres g
            WHERE
                g.id = genre_id
        );
    END;'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.get_genre_by_name(genre_name VARCHAR)
    RETURNS film.genres AS '
    BEGIN
        RETURN (
            SELECT
                g
            FROM
                film.genres g
            WHERE g.name = genre_name
        );
    END;
'
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.save_genre(genre_name VARCHAR)
RETURNS film.genres AS '
    DECLARE
        new_genre film.genres;
    BEGIN
        INSERT INTO film.genres (name) VALUES (genre_name)
        RETURNING * INTO new_genre;
        RETURN new_genre;
    END;'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION film.check_genre_exists_by_name(genre_name VARCHAR(255))
RETURNS BOOLEAN AS '
    DECLARE
        exists BOOLEAN;
    BEGIN
        SELECT
            (COUNT(*) != 0) INTO exists
        FROM
            film.genres
        WHERE
            name = genre_name;
        RETURN exists;
    END;
'
LANGUAGE plpgsql;
