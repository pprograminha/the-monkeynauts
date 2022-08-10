REVOKE ALL ON schema public FROM public;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE SCHEMA IF NOT EXISTS themonkeynauts;
CREATE SCHEMA IF NOT EXISTS extensions;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA extensions;



SET default_table_access_method = heap;

-- Tables

CREATE TABLE themonkeynauts."account"
(
    id              uuid DEFAULT extensions.uuid_generate_v4()  NOT NULL
);

ALTER TABLE ONLY themonkeynauts."account"
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);

CREATE TABLE themonkeynauts."user"
(
    id                  uuid DEFAULT extensions.uuid_generate_v4()  NOT NULL,
    email               text                                        NOT NULL,
    nickname            text                                        NOT NULL,
    password            text                                        NOT NULL,
    wallet_id           uuid                                                ,
    last_bounty_hunting timestamp
);

ALTER TABLE ONLY themonkeynauts."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

CREATE TABLE themonkeynauts."user_account"
(
    user_id         uuid                                        NOT NULL,
    account_id      uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."user_account"
    ADD CONSTRAINT user_account_pkey PRIMARY KEY (user_id);

CREATE TABLE themonkeynauts."role"
(
    role_id         text                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."role"
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);

INSERT INTO themonkeynauts."role" (role_id)
VALUES  ('ROOT'), ('ADMIN'), ('PLAYER');

CREATE TABLE themonkeynauts."user_role"
(
    role_id         text                                        NOT NULL,
    user_id         uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."user_role"
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (role_id, user_id);

CREATE TABLE themonkeynauts."monkeynaut_number"
(
    number          integer                                     NOT NULL
);

INSERT INTO themonkeynauts."monkeynaut_number" (number)
VALUES (0);

CREATE TABLE themonkeynauts."monkeynaut"
(
    id              uuid DEFAULT extensions.uuid_generate_v4()  NOT NULL,
    number          integer                                     NOT NULL,
    firstname       text                                        NOT NULL,
    lastname        text                                        NOT NULL,
    class           text                                        NOT NULL,
    rank            text                                        NOT NULL,
    user_id         uuid                                        NOT NULL,
    operator_id     uuid                                        NOT NULL,
    skill           integer                                     NOT NULL,
    speed           integer                                     NOT NULL,
    resistance      integer                                     NOT NULL,
    life            integer                                     NOT NULL,
    current_energy  integer                                     NOT NULL,
    max_energy      integer                                     NOT NULL,
    ship_id         uuid                                                ,
    account_id      uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."monkeynaut"
    ADD CONSTRAINT monkeynaut_pkey PRIMARY KEY (id);

CREATE TABLE themonkeynauts."equipment"
(
    id              uuid DEFAULT extensions.uuid_generate_v4()  NOT NULL,
    name            text                                        NOT NULL,
    type            text                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."equipment"
    ADD CONSTRAINT equipment_pkey PRIMARY KEY (id);

CREATE TABLE themonkeynauts."user_equipment"
(
    user_id         uuid                                        NOT NULL,
    equipment_id    uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."user_equipment"
    ADD CONSTRAINT user_equipment_pkey PRIMARY KEY (user_id, equipment_id);

CREATE TABLE themonkeynauts."monkeynaut_equipment"
(
    monkeynaut_id   uuid                                        NOT NULL,
    equipment_id    uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."monkeynaut_equipment"
    ADD CONSTRAINT monkeynaut_equipment_pkey PRIMARY KEY (monkeynaut_id, equipment_id);

CREATE TABLE themonkeynauts."ship"
(
    id              uuid DEFAULT extensions.uuid_generate_v4()  NOT NULL,
    name            text                                        NOT NULL,
    class           text                                        NOT NULL,
    rank            text                                        NOT NULL,
    user_id         uuid                                        NOT NULL,
    operator_id     uuid                                        NOT NULL,
    durability            real                                        NOT NULL,
    account_id      uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."ship"
    ADD CONSTRAINT ship_pkey PRIMARY KEY (id);

CREATE TABLE themonkeynauts."ship_monkeynaut"
(
    ship_id          uuid                                        NOT NULL,
    monkeynaut_id    uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."ship_monkeynaut"
    ADD CONSTRAINT ship_monkeynaut_pkey PRIMARY KEY (ship_id, monkeynaut_id);

CREATE TABLE themonkeynauts."wallet"
(
    id              uuid DEFAULT extensions.uuid_generate_v4()  NOT NULL,
    address         text                                        NOT NULL,
    name            text                                        NOT NULL,
    balance         real DEFAULT 0.0                            NOT NULL,
    user_id         uuid                                        NOT NULL,
    account_id      uuid                                        NOT NULL
);

ALTER TABLE ONLY themonkeynauts."wallet"
    ADD CONSTRAINT wallet_pkey PRIMARY KEY (id);

-- Constraints

ALTER TABLE ONLY themonkeynauts."user_account"
    ADD CONSTRAINT user_account_account_id_fkey FOREIGN KEY (account_id) REFERENCES themonkeynauts."account" (id);

ALTER TABLE ONLY themonkeynauts."user_account"
    ADD CONSTRAINT user_account_user_id_fkey FOREIGN KEY (user_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."user"
    ADD CONSTRAINT user_email_unique_fk UNIQUE (email);

ALTER TABLE ONLY themonkeynauts."user"
    ADD CONSTRAINT user_nickname_unique_fk UNIQUE (nickname);

ALTER TABLE ONLY themonkeynauts."user_role"
    ADD CONSTRAINT role_user_id_fkey FOREIGN KEY (user_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."monkeynaut"
    ADD CONSTRAINT monkeynaut_account_id_fkey FOREIGN KEY (account_id) REFERENCES themonkeynauts."account" (id);

ALTER TABLE ONLY themonkeynauts."monkeynaut"
    ADD CONSTRAINT monkeynaut_user_id_fkey FOREIGN KEY (user_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."monkeynaut"
    ADD CONSTRAINT monkeynaut_operator_id_fkey FOREIGN KEY (operator_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."monkeynaut"
    ADD CONSTRAINT monkeynaut_ship_id_fkey FOREIGN KEY (ship_id) REFERENCES themonkeynauts."ship" (id);

ALTER TABLE ONLY themonkeynauts."equipment"
    ADD CONSTRAINT equipment_name_unique_fk UNIQUE (name);

ALTER TABLE ONLY themonkeynauts."ship"
    ADD CONSTRAINT ship_user_id_fkey FOREIGN KEY (user_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."ship"
    ADD CONSTRAINT ship_operator_id_fkey FOREIGN KEY (operator_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."user_equipment"
    ADD CONSTRAINT user_equipment_user_id_fkey FOREIGN KEY (user_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."user_equipment"
    ADD CONSTRAINT user_equipment_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES themonkeynauts."equipment" (id);

ALTER TABLE ONLY themonkeynauts."monkeynaut_equipment"
    ADD CONSTRAINT monkeynaut_equipment_monkeynaut_id_fkey FOREIGN KEY (monkeynaut_id) REFERENCES themonkeynauts."monkeynaut" (id);

ALTER TABLE ONLY themonkeynauts."monkeynaut_equipment"
    ADD CONSTRAINT monkeynaut_equipment_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES themonkeynauts."equipment" (id);

ALTER TABLE ONLY themonkeynauts."ship_monkeynaut"
    ADD CONSTRAINT ship_monkeynaut_ship_id_fkey FOREIGN KEY (ship_id) REFERENCES themonkeynauts."ship" (id);

ALTER TABLE ONLY themonkeynauts."ship_monkeynaut"
    ADD CONSTRAINT ship_monkeynaut_monkeynaut_id_fkey FOREIGN KEY (monkeynaut_id) REFERENCES themonkeynauts."monkeynaut" (id);

ALTER TABLE ONLY themonkeynauts."wallet"
    ADD CONSTRAINT wallet_account_id_fkey FOREIGN KEY (account_id) REFERENCES themonkeynauts."account" (id);

ALTER TABLE ONLY themonkeynauts."wallet"
    ADD CONSTRAINT wallet_user_id_fkey FOREIGN KEY (user_id) REFERENCES themonkeynauts."user" (id);

ALTER TABLE ONLY themonkeynauts."wallet"
    ADD CONSTRAINT wallet_user_id_unique_fk UNIQUE (user_id);

-- Indexes

CREATE INDEX user_email_idx ON themonkeynauts."user" (email);

CREATE INDEX monkeynaut_user_id_idx ON themonkeynauts."monkeynaut" (user_id);

CREATE INDEX monkeynaut_operator_id_idx ON themonkeynauts."monkeynaut" (operator_id);

-- Row Level Security

CREATE POLICY tenant_isolation_policy ON themonkeynauts."monkeynaut"
    USING (account_id = case when (current_setting('app.current_tenant', 't') = '') IS NOT FALSE then extensions.uuid_nil() else current_setting('app.current_tenant')::uuid end);
ALTER TABLE themonkeynauts."monkeynaut" ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation_policy ON themonkeynauts."ship"
    USING (account_id = case when (current_setting('app.current_tenant', 't') = '') IS NOT FALSE then extensions.uuid_nil() else current_setting('app.current_tenant')::uuid end);
ALTER TABLE themonkeynauts."ship" ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation_policy ON themonkeynauts."wallet"
    USING (account_id = case when (current_setting('app.current_tenant', 't') = '') IS NOT FALSE then extensions.uuid_nil() else current_setting('app.current_tenant')::uuid end);
ALTER TABLE themonkeynauts."wallet" ENABLE ROW LEVEL SECURITY;

-- Trigger

CREATE FUNCTION themonkeynauts.update_wallet_of_user() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
UPDATE themonkeynauts."user" u
SET wallet_id = NEW.id
WHERE u.id = NEW.user_id;
RETURN NEW;
END;
$$;

CREATE FUNCTION themonkeynauts.update_account_of_ship() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
AS
$$
DECLARE
new_tenant uuid;
BEGIN
    new_tenant := (SELECT account_id FROM themonkeynauts."user_account" WHERE user_id = NEW.user_id)::uuid;
UPDATE themonkeynauts."ship" s
SET account_id = new_tenant
WHERE s.id = NEW.id;
RETURN NEW;
END;
$$;

CREATE FUNCTION themonkeynauts.update_account_of_monkeynaut() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
AS
$$
DECLARE
new_tenant uuid;
BEGIN
    new_tenant := (SELECT account_id FROM themonkeynauts."user_account" WHERE user_id = NEW.user_id)::uuid;
UPDATE themonkeynauts."monkeynaut" m
SET account_id = new_tenant
WHERE m.id = NEW.id;
RETURN NEW;
END;
$$;

CREATE TRIGGER trig_update_wallet_of_user
    AFTER INSERT
    ON themonkeynauts."wallet"
    FOR EACH ROW
    EXECUTE FUNCTION themonkeynauts.update_wallet_of_user();

CREATE TRIGGER trig_update_account_of_ship
    AFTER INSERT
    ON themonkeynauts."ship"
    FOR EACH ROW
    EXECUTE FUNCTION themonkeynauts.update_account_of_ship();

CREATE TRIGGER trig_update_account_of_monkeynaut
    AFTER INSERT
    ON themonkeynauts."monkeynaut"
    FOR EACH ROW
    EXECUTE FUNCTION themonkeynauts.update_account_of_monkeynaut();

-- Grant

