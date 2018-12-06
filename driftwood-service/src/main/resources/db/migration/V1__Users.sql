CREATE TABLE users(
                    user_id UUID NOT NULL PRIMARY KEY,
                    version UUID NOT NULL,
                    created TIMESTAMP NOT NULL,
                    updated TIMESTAMP NOT NULL,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(260) NULL UNIQUE,
                    authentication JSONB NOT NULL
);
