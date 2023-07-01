CREATE TABLE if not exists user
(
    id         BIGINT PRIMARY KEY,
    username   VARCHAR(255),
    pwd        VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE TABLE if not exists school
(
    id           BIGINT PRIMARY KEY,
    name         VARCHAR(255),
    address      VARCHAR(255),
    min_grade    INTEGER,
    max_grade    INTEGER,
    school_level VARCHAR(255),
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP
);

CREATE TABLE if not exists student
(
    id         BIGINT PRIMARY KEY,
    user_id    BIGINT,
    school_id  BIGINT,
    name       VARCHAR(255),
    grade      INTEGER,
    birth_day  DATE,
    sex        VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (school_id) REFERENCES school (id)
);
