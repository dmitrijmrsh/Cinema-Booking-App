CREATE OR REPLACE FUNCTION auth.update_modified_at()
RETURNS TRIGGER AS '
    BEGIN
        new.modified_at = CURRENT_TIMESTAMP;
        RETURN new;
    END;
'
LANGUAGE plpgsql;

CREATE TRIGGER trg_update_modified_at
BEFORE UPDATE ON auth.users
FOR EACH ROW
EXECUTE FUNCTION auth.update_modified_at()
