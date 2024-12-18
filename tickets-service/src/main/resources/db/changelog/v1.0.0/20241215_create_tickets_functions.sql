CREATE OR REPLACE FUNCTION ticket.get_all_by_user_id(ticket_user_id INT)
RETURNS TABLE(
    id INT,
    user_id INT,
    screening_id INT,
    seat_id INT
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                t.id,
                t.user_id,
                t.screening_id,
                t.seat_id
            FROM
                ticket.tickets t
            WHERE
                t.user_id = ticket_user_id;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ticket.save_ticket(
    ticket_user_id INT,
    ticket_screening_id INT,
    ticket_seat_id INT
)
RETURNS ticket.tickets AS '
    DECLARE
        new_ticket ticket.tickets;
    BEGIN
    INSERT INTO ticket.tickets (user_id, screening_id, seat_id)
    VALUES (ticket_user_id, ticket_screening_id, ticket_seat_id)
    RETURNING * INTO new_ticket;
    RETURN (
        SELECT
            t
        FROM
            ticket.tickets t
        WHERE
            t.id = new_ticket.id
    );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ticket.exists_by_user_id_and_screening_id(
    ticket_user_id INT,
    ticket_screening_id INT
)
RETURNS BOOLEAN AS '
    DECLARE
        exists BOOLEAN;
    BEGIN
        SELECT
            (COUNT(*) != 0) INTO exists
        FROM
            ticket.tickets t
        WHERE
            t.user_id = ticket_user_id
            AND
            t.screening_id = ticket_screening_id;
        RETURN exists;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE ticket.delete_ticket_by_id(ticket_id INT)
AS '
    BEGIN
        DELETE FROM
            ticket.tickets
        WHERE
            id = ticket_id;
    END;
'
LANGUAGE plpgsql;