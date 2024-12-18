CREATE OR REPLACE FUNCTION auth.get_role_by_name(role_name VARCHAR)
RETURNS auth.roles AS '
    BEGIN
        RETURN (
            SELECT
                r
            FROM
                auth.roles r
            WHERE
                r.name = role_name
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION auth.check_role_exists_by_name(role_name VARCHAR)
RETURNS BOOLEAN AS '
    DECLARE
        exists BOOLEAN;
    BEGIN
       SELECT
            (COUNT(*) != 0) INTO exists
       FROM
            auth.roles
       WHERE
            name = role_name;

       RETURN exists;
    END;
'
LANGUAGE plpgsql;