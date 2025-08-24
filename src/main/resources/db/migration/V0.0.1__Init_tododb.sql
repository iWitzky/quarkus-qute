CREATE SEQUENCE todo_id_seq
    INCREMENT BY 50
    START WITH 1;

CREATE TABLE TODO
(
    id        BIGINT PRIMARY KEY DEFAULT nextval('todo_id_seq'),
    title     VARCHAR(200),
    priority  INT,
    completed BOOLEAN,
    tenantid  varchar(255) not null
);
