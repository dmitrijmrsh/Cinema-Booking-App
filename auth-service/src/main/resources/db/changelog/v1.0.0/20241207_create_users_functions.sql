CREATE OR REPLACE FUNCTION auth.get_all_users()
RETURNS TABLE (
    id INT,
    email VARCHAR,
    first_name VARCHAR,
    last_name  VARCHAR,
    role_id INT,
    role_name VARCHAR
) AS '
    BEGIN
        RETURN QUERY
            SELECT
                v.id,
                v.email,
                v.first_name,
                v.last_name,
                v.role_id,
                v.role_name
            FROM auth.all_users_with_roles v;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION auth.get_user_by_id(user_id INT)
RETURNS auth.all_users_with_roles AS '
    BEGIN
        RETURN (
            SELECT
                v
            FROM
                auth.all_users_with_roles v
            WHERE
                v.id = user_id
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION auth.check_user_exists_by_id(user_id INT)
RETURNS BOOLEAN AS '
    DECLARE
        exists BOOLEAN;
    BEGIN
        SELECT
            COUNT(*) INTO exists
        FROM
            auth.users
        WHERE
            id = user_id;

        RETURN exists;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION auth.check_user_exists_by_email(user_email VARCHAR)
RETURNS BOOLEAN AS '
    DECLARE
        exists BOOLEAN;
    BEGIN
        SELECT
            COUNT(*) INTO exists
        FROM
            auth.users
        WHERE
            email = user_email;

        RETURN exists;
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION auth.save_user(
    user_email VARCHAR,
    user_password VARCHAR,
    user_first_name VARCHAR,
    user_last_name VARCHAR,
    user_role_id INT
)
RETURNS auth.all_users_with_roles AS '
    DECLARE
        new_user auth.users;
    BEGIN
    INSERT INTO auth.users (email, password, first_name, last_name, role_id)
    VALUES (user_email, user_password, user_first_name, user_last_name, user_role_id)
    RETURNING * INTO new_user;
    RETURN (
        SELECT
            v
        FROM
            auth.all_users_with_roles v
        WHERE v.id = new_user.id
    );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION auth.update_user(
    user_id INT,
    user_email VARCHAR,
    user_first_name VARCHAR,
    user_last_name VARCHAR,
    user_role_id INT
)
RETURNS auth.all_users_with_roles AS '
    BEGIN
        UPDATE auth.users
        SET
            email = user_email,
            first_name = user_first_name,
            last_name = user_last_name,
            role_id = user_role_id
        WHERE
            id = user_id;
        RETURN (
            SELECT
                v
            FROM
                auth.all_users_with_roles v
            WHERE
                v.id = user_id
        );
    END;
'
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE auth.delete_user_by_id(user_id INT)
AS '
    BEGIN
        DELETE FROM
            auth.users
        WHERE
            id = user_id;
    END;
'
LANGUAGE plpgsql;
