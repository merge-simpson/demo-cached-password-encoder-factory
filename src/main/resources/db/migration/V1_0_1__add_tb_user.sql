CREATE TABLE "user" (
    id          UUID,
    username    VARCHAR(255),
    password    VARCHAR(255),
    nickname    VARCHAR(255),

    status      VARCHAR(255),
    created_at  TIMESTAMP           DEFAULT NOW(),
    updated_at  TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT uq_user_username  UNIQUE (username),
    CONSTRAINT uq_user_nickname UNIQUE (nickname)
);