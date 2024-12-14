CREATE OR REPLACE FUNCTION screening.get_available_by_screening_id(seat_screening_id INT)
RETURNS TABLE (
    id INT,
    row_number INT,
    seat_in_row INT,
    status VARCHAR,
    screening_id INT
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                t.id,
                t.row_number,
                t.seat_in_row,
                t.status,
                t.screening_id
            FROM
                screening.seats t
            WHERE
                t.screening_id = seat_screening_id;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.get_by_screening_id_and_row_number_and_seat_in_row(
    seat_screening_id INT,
    seat_row_number INT,
    seat_in_row_number INT
)
RETURNS screening.seats AS '
    BEGIN
        RETURN (
            SELECT
                t
            FROM
                screening.seats t
            WHERE
                t.screening_id = seat_screening_id
                AND
                t.row_number = seat_row_number
                AND
                t.seat_in_row = seat_in_row_number
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.save_seat(
    seat_row_number INT,
    seat_in_row_number INT,
    seat_status VARCHAR,
    seat_screening_id INT
)
RETURNS screening.seats AS '
    DECLARE
        new_seat screening.seats;
    BEGIN
    INSERT INTO screening.seats(row_number, seat_in_row, status, screening_id)
    VALUES (seat_row_number, seat_in_row_number, seat_status, seat_screening_id)
    RETURNING * INTO new_seat;
    RETURN (
        SELECT
            t
        FROM
            screening.seats t
        WHERE
            t.id = new_seat.id
    );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION screening.update_seat_status(
    seat_id INT,
    seat_status VARCHAR
)
RETURNS screening.seats AS '
    BEGIN
        UPDATE screening.seats
        SET
            status = seat_status
        WHERE
            id = seat_id;
        RETURN (
            SELECT
                t
            FROM
                screening.seats t
            WHERE
                t.id = seat_id
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE screening.delete_seat_by_id(seat_id INT)
AS '
    BEGIN
        DELETE FROM
            screening.seats
        WHERE
            id = seat_id;
    END;
'
LANGUAGE plpgsql;