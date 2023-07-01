CREATE TABLE user
(
    id         BIGINT PRIMARY KEY,
    username   VARCHAR(255),
    pwd        VARCHAR(255),
    user_type  VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE TABLE school
(
    id           BIGINT PRIMARY KEY,
    name         VARCHAR(255),
    address      VARCHAR(255),
    minGrade     INTEGER,
    maxGrade     INTEGER,
    school_level VARCHAR(255),
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP
);

CREATE TABLE role
(
    id         BIGINT PRIMARY KEY,
    role       VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE TABLE user_role
(
    id         BIGINT PRIMARY KEY,
    user_id    BIGINT,
    role_id    BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);
CREATE TABLE student
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