-- V1__create_tables.sql

-- Create the user table if it does not exist
CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT PRIMARY KEY,
                                    username VARCHAR(255),
                                    pwd VARCHAR(255),
                                    user_type VARCHAR(255),
                                    created_at TIMESTAMP,
                                    updated_at TIMESTAMP
);

-- Create the role table if it does not exist
CREATE TABLE IF NOT EXISTS role (
                                    id BIGINT PRIMARY KEY,
                                    role VARCHAR(255),
                                    created_at TIMESTAMP,
                                    updated_at TIMESTAMP
);

-- Create the user_role table if it does not exist
CREATE TABLE IF NOT EXISTS user_role (
                                         id BIGINT PRIMARY KEY,
                                         user_id BIGINT,
                                         role_id BIGINT,
                                         created_at TIMESTAMP,
                                         updated_at TIMESTAMP,
                                         FOREIGN KEY (user_id) REFERENCES user(id),
                                         FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Create the school table if it does not exist
CREATE TABLE IF NOT EXISTS school (
                                      id BIGINT PRIMARY KEY,
                                      name VARCHAR(255),
                                      address VARCHAR(255),
                                      min_grade INTEGER,
                                      max_grade INTEGER,
                                      school_level VARCHAR(255),
                                      created_at TIMESTAMP,
                                      updated_at TIMESTAMP
);

-- Create the student table if it does not exist
CREATE TABLE IF NOT EXISTS student (
                                       id BIGINT PRIMARY KEY,
                                       user_id BIGINT,
                                       school_id BIGINT,
                                       name VARCHAR(255),
                                       grade INTEGER,
                                       birth_day TIMESTAMP,
                                       sex VARCHAR(255),
                                       created_at TIMESTAMP,
                                       updated_at TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES user(id),
                                       FOREIGN KEY (school_id) REFERENCES school(id)
);