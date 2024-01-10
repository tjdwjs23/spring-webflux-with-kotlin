CREATE TABLE template_table (
                                id serial PRIMARY KEY,
                                title varchar(100) NOT NULL,
                                content varchar(100) NOT NULL,
                                author varchar(100) NOT NULL,
                                create_date date DEFAULT CURRENT_DATE,
                                modified_date date DEFAULT NULL
);

CREATE INDEX AUTHOR_IDX ON template_table (author);