-- V1__create_user_security_qa_table.sql

CREATE TABLE user_security_qa
(
    id         BIGINT PRIMARY KEY,
    user_id    BIGINT,
    question   VARCHAR(255),
    answer     VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id)
);
create table user_security_qa_seq
(
    next_val bigint null
);
insert into user_security_qa_seq (next_val) values (1);
