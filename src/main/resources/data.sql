CREATE DATABASE test_post IF NOT EXIST;

create table if not exists posts
(
    id    bigint NOT NULL,
    title text   NOT NULL,
    body  text   NOT NULL,
    CONSTRAINT test_posts_pkey PRIMARY KEY (id)
);