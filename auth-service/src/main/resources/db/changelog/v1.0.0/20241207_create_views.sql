DROP VIEW IF EXISTS auth.all_users_with_roles;

CREATE VIEW auth.all_users_with_roles AS
    SELECT
        u.id,
        u.email,
        u.password,
        u.first_name,
        u.last_name,
        r.id as role_id,
        r.name as role_name
    FROM
        auth.users u
    LEFT JOIN
        auth.roles r ON u.role_id = r.id