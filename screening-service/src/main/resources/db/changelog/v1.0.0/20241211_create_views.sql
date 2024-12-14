CREATE OR REPLACE VIEW screening.screenings_with_seats AS
    SELECT
        sc.id,
        sc.start_date,
        sc.start_time,
        sc.film_id,
        sc.hall_id,
        JSON_AGG(
            JSON_BUILD_OBJECT(
                'id', se.id,
                'row_number', se.row_number,
                'seat_in_row', se.seat_in_row,
                'status', se.status
            )
        ) AS seats
    FROM
        screening.screenings sc
    LEFT JOIN
            screening.seats se ON sc.id = se.screening_id
    GROUP BY
        sc.id,
        sc.start_date,
        sc.start_time,
        sc.film_id,
        sc.hall_id;
