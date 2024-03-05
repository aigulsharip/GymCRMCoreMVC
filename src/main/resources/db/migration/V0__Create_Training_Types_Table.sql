CREATE TABLE training_types (
                                id SERIAL PRIMARY KEY,
                                training_type_name VARCHAR(255) NOT NULL
);

CREATE TABLE customers (
                           customer_id SERIAL PRIMARY KEY,
                           name VARCHAR(64) NOT NULL,
                           contact_name VARCHAR(128) NOT NULL,
                           email VARCHAR(128) NOT NULL,
                           phone VARCHAR(24) NOT NULL
);


-- CREATE TABLE users
-- (
--     username VARCHAR(50) PRIMARY KEY,
--     password VARCHAR(500) NOT NULL,
--     enabled BOOLEAN NOT NULL
-- );
--
-- CREATE TABLE authorities
-- (
--     username VARCHAR(50) NOT NULL,
--     authority VARCHAR(50) NOT NULL,
--     CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
-- );
--
-- CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);
