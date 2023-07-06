CREATE TABLE documents (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255),
    content VARCHAR(255),
    parent_id BIGINT REFERENCES documents(id)
);