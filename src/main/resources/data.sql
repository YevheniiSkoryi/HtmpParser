CREATE DATABASE test_post IF NOT EXIST;

create table if not exists posts
(
    id    text NOT NULL,
    title text NOT NULL,
    body  text NOT NULL,
    CONSTRAINT test_posts_pkey PRIMARY KEY (id)
);


insert into posts(id, title, body)
VALUES ('713221aa-f5c6-4fee-9dcf-f6e191882f83','title','body and i and Body, which is Body but not equals to body.');
