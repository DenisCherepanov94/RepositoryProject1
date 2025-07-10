CREATE TABLE users
(
    id       UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(20)         NOT NULL
);

CREATE TABLE orders
(
    id          UUID PRIMARY KEY,
    user_id     UUID        NOT NULL,
    description TEXT        NOT NULL,
    status      VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);