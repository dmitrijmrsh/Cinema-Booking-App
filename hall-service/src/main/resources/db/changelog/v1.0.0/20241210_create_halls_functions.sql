CREATE OR REPLACE FUNCTION hall.get_all_halls()
RETURNS TABLE (
    id INT,
    is_active BOOLEAN,
    row_count INT,
    seat_in_row_count INT
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                t.id,
                t.is_active,
                t.row_count,
                t.seat_in_row_count
            FROM hall.halls t;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hall.get_all_halls_by_activity(hall_is_active BOOLEAN)
RETURNS TABLE (
    id INT,
    is_active BOOLEAN,
    row_count INT,
    seat_in_row_count INT
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                t.id,
                t.is_active,
                t.row_count,
                t.seat_in_row_count
            FROM
                hall.halls t
            WHERE
                t.is_active = hall_is_active;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hall.get_hall_by_id(hall_id INT)
RETURNS hall.halls AS '
    BEGIN
        RETURN (
            SELECT
                t
            FROM
                hall.halls t
            WHERE
                t.id = hall_id
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hall.save_hall(
    hall_is_active BOOLEAN,
    hall_row_count INT,
    hall_seat_in_row_count INT
)
RETURNS hall.halls AS '
    DECLARE
        new_hall hall.halls;
    BEGIN
    INSERT INTO hall.halls (is_active, row_count, seat_in_row_count)
    VALUES (hall_is_active, hall_row_count, hall_seat_in_row_count)
    RETURNING * INTO new_hall;
    RETURN (
        SELECT
            t
        FROM
            hall.halls t
        WHERE
            t.id = new_hall.id
    );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hall.update_hall(
    hall_id INT,
    hall_is_active BOOLEAN,
    hall_row_count INT,
    hall_seat_in_row_count INT
)
RETURNS hall.halls AS '
    BEGIN
        UPDATE hall.halls
        SET
            is_active = hall_is_active,
            row_count = hall_row_count,
            seat_in_row_count = hall_seat_in_row_count
        WHERE
            id = hall_id;
        RETURN (
            SELECT
                t
            FROM
                hall.halls t
            WHERE
                t.id = hall_id
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE hall.delete_hall_by_id(hall_id INT)
AS '
    BEGIN
        DELETE FROM
            hall.halls
        WHERE
            id = hall_id;
    END;
'
LANGUAGE plpgsql;
