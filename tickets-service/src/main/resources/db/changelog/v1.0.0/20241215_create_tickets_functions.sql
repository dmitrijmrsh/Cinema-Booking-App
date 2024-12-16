CREATE OR REPLACE FUNCTION ticket.get_all_by_user_id(ticket_user_id INT)
RETURNS TABLE(
    id INT,
    user_id INT,
    screening_id INT
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                t.id,
                t.user_id,
                t.screening_id
            FROM
                ticket.tickets t
            WHERE
                t.user_id = ticket_user_id;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ticket.save_ticket(
    ticket_user_id INT,
    ticket_screening_id INT
)
RETURNS ticket.tickets AS '
    DECLARE
        new_ticket ticket.tickets;
    BEGIN
    INSERT INTO ticket.tickets (user_id, screening_id)
    VALUES (ticket_user_id, ticket_screening_id)
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