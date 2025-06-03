-- Create users table
CREATE TABLE users
(
    id         BIGINT       NOT NULL,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    full_name  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

-- Create indexes
CREATE INDEX idx_users_username ON users (username);

