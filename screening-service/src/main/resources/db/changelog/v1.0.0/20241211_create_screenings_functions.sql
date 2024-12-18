CREATE OR REPLACE FUNCTION screening.get_all_screenings()
RETURNS TABLE (
    id INT,
    start_date DATE,
    start_time TIME,
    film_id INT,
    hall_id INT,
    seats JSON
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.start_date,
                v.start_time,
                v.film_id,
                v.hall_id,
                v.seats
            FROM
                screening.screenings_with_seats v;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.get_screenings_by_date(screening_date DATE)
RETURNS TABLE (
    id INT,
    start_date DATE,
    start_time TIME,
    film_id INT,
    hall_id INT,
    seats JSON
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.start_date,
                v.start_time,
                v.film_id,
                v.hall_id,
                v.seats
            FROM
                screening.screenings_with_seats v
            WHERE
                v.start_date = screening_date;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.get_all_screenings_by_hall_id_and_date(
    screening_hall_id INT,
    screening_date DATE
)
RETURNS TABLE (
    id INT,
    start_date DATE,
    start_time TIME,
    film_id INT,
    hall_id INT,
    seats JSON
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.start_date,
                v.start_time,
                v.film_id,
                v.hall_id,
                v.seats
            FROM
                screening.screenings_with_seats v
            WHERE
                v.hall_id = screening_hall_id
                AND
                v.start_date = screening_date;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.get_screenings_by_film_id(screening_film_id INT)
RETURNS TABLE (
    id INT,
    start_date DATE,
    start_time TIME,
    film_id INT,
    hall_id INT,
    seats JSON
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.start_date,
                v.start_time,
                v.film_id,
                v.hall_id,
                v.seats
            FROM
                screening.screenings_with_seats v
            WHERE
                v.film_id = screening_film_id;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.get_screening_by_id(screening_id INT)
RETURNS TABLE (
    id INT,
    start_date DATE,
    start_time TIME,
    film_id INT,
    hall_id INT,
    seats JSON
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.start_date,
                v.start_time,
                v.film_id,
                v.hall_id,
                v.seats
            FROM
                screening.screenings_with_seats v
            WHERE
                v.id = screening_id;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.save_screening(
    screening_date DATE,
    screening_time TIME,
    screening_film_id INT,
    screening_hall_id INT
)
RETURNS screening.screenings_with_seats AS '
    DECLARE
        new_screening screening.screenings;
    BEGIN
    INSERT INTO screening.screenings (start_date, start_time, film_id, hall_id)
    VALUES (screening_date, screening_time, screening_film_id, screening_hall_id)
    RETURNING * INTO new_screening;
    RETURN (
        SELECT
            v
        FROM
            screening.screenings_with_seats v
        WHERE
            v.id = new_screening.id
    );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE screening.delete_screening_by_id(screening_id INT)
AS '
    BEGIN
        DELETE FROM
            screening.screenings t
        WHERE
            t.id = screening_id;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE screening.delete_all_passed_screenings()
AS '
    BEGIN
        DELETE FROM
            screening.screenings t
        WHERE
            t.start_date < CURRENT_DATE
            OR
            (t.start_date = CURRENT_DATE AND t.start_time < CURRENT_TIME);
    END;
'
LANGUAGE plpgsql;