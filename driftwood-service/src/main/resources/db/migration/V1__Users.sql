CREATE TABLE users(
                    id BIGSERIAL NOT NULL PRIMARY KEY,
                    version VARCHAR(36) NOT NULL,
                    created TIMESTAMP NOT NULL,
                    updated TIMESTAMP NOT NULL,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(260) NULL UNIQUE,
                    authentication JSONB NOT NULL
);
