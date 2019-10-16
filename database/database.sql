------------------------------------------------DATABASE---------------------------------------------

CREATE DATABASE certificates
   WITH
   OWNER = postgres
ENCODING = 'UTF8'
CONNECTION LIMIT = 50;

-------------------------------------------------TABLES----------------------------------------------

CREATE TABLE gift_certificate
(
   id bigserial NOT NULL,
   name varchar(255) NOT NULL,
   description varchar(255),
   price double PRECISION,
   creation_date date,
   last_modified date,
   duration_in_days integer,
   deleted boolean,
   CONSTRAINT gift_certificate_pkey PRIMARY KEY(id)
);

CREATE TABLE tag
(
   id bigserial NOT NULL,
   name varchar(255) NOT NULL,
   UNIQUE (name),
   CONSTRAINT tag_pkey PRIMARY KEY(id)
);

CREATE TABLE gift_certificate_to_tag
(
    id bigserial PRIMARY KEY,
    gift_certificate_id bigint NOT NULL,
    tag_id bigint NOT NULL,
    CONSTRAINT gift_certificate_to_tag_gift_certificate_id_fkey
    FOREIGN KEY(gift_certificate_id)REFERENCES gift_certificate(id),
    CONSTRAINT gift_certificate_to_tag_tag_id_fkey
    FOREIGN KEY(tag_id)REFERENCES tag(id)
);

/**user password - user
   admin password - admin*/
CREATE TABLE users
(
    id bigserial NOT NULL,
    name varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL,
    users_balance double PRECISION DEFAULT 0,
    UNIQUE (name),
    CONSTRAINT users_pkey PRIMARY KEY(id)
);

CREATE TABLE orders (
    id bigserial PRIMARY KEY,
    users_id bigint NOT NULL,
    to_which_users_id bigint NOT NULL,
    gift_certificate_id  bigint NOT NULL,
    cost double PRECISION,
    timestamp date,
    CONSTRAINT orders_users_id_fkey
    FOREIGN KEY(users_id)REFERENCES users(id),
    CONSTRAINT orders_gift_certificate_id_fkey
    FOREIGN KEY(gift_certificate_id)REFERENCES gift_certificate(id)
);

CREATE TABLE auditing_history (
  id bigserial NOT NULL,
  entity_content varchar(255),
  modified_by varchar(255),
  modified_date date,
  action varchar(255),
  CONSTRAINT auditing_history_pkey PRIMARY KEY(id)
);
-----------------------------------------------FUNCTION--------------------------------------------

CREATE OR REPLACE FUNCTION get_gift_certificate_by_parts (part_name varchar(255)DEFAULT '',
                                                          part_desc varchar(255)DEFAULT '', sort varchar(255)DEFAULT 'id')
    RETURNS refcursor AS $$
DECLARE
    needresult refcursor;
BEGIN
    IF ((part_name NOT ILIKE '') AND (part_name IS NOT NULL) AND (part_desc NOT ILIKE '') AND (part_desc IS NOT NULL))  THEN
        OPEN needresult FOR
            SELECT gc.id, gc.name, gc.description, gc.price, gc.creation_date, gc.last_modified, gc.duration_in_days, gc.deleted
            FROM gift_certificate gc
            WHERE (gc.name LIKE concat('%', part_name, '%')) AND (gc.description LIKE concat('%', part_desc, '%') AND gc.deleted = false)
            ORDER BY
                CASE WHEN sort='name' THEN gc.name END ASC,
                CASE WHEN sort='description' THEN gc.description END ASC,
                CASE WHEN sort='last_modified' THEN TO_CHAR(gc.last_modified, 'YYYY-MM-DD') END DESC,
                CASE WHEN sort='creation_date' THEN TO_CHAR(gc.creation_date, 'YYYY-MM-DD') END DESC,
                gc.id ASC;
        RETURN needresult;
    ELSIF ((part_name NOT ILIKE '') AND (part_name IS NOT NULL)) THEN
        OPEN needresult FOR
            SELECT gc.id, gc.name, gc.description, gc.price, gc.creation_date, gc.last_modified, gc.duration_in_days, gc.deleted
            FROM gift_certificate gc
            WHERE ((gc.name LIKE concat('%', part_name, '%')) AND gc.deleted = false)
            ORDER BY
                CASE WHEN sort='name' THEN gc.name END ASC,
                CASE WHEN sort='description' THEN gc.description END ASC,
                CASE WHEN sort='last_modified' THEN TO_CHAR(gc.last_modified, 'YYYY-MM-DD') END DESC,
                CASE WHEN sort='creation_date' THEN TO_CHAR(gc.creation_date, 'YYYY-MM-DD') END DESC,
                gc.id ASC;
        RETURN needresult;
    ELSIF ((part_desc NOT ILIKE '') AND (part_desc IS NOT NULL)) THEN
        OPEN needresult FOR
            SELECT gc.id, gc.name, gc.description, gc.price, gc.creation_date, gc.last_modified, gc.duration_in_days, gc.deleted
            FROM gift_certificate gc
            WHERE ((gc.description LIKE concat('%', part_desc, '%')) AND gc.deleted = false)
            ORDER BY
                CASE WHEN sort='name' THEN gc.name END ASC,
                CASE WHEN sort='description' THEN gc.description END ASC,
                CASE WHEN sort='last_modified' THEN TO_CHAR(gc.last_modified, 'YYYY-MM-DD') END DESC,
                CASE WHEN sort='creation_date' THEN TO_CHAR(gc.creation_date, 'YYYY-MM-DD') END DESC,
                gc.id ASC;
        RETURN needresult;
    ELSE RAISE EXCEPTION 'Do not found any certificate.';

    END IF;
END
$$ language 'plpgsql';