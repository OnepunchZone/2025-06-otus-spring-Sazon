insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3);

insert into users (id, username, password) values
(1, 'user', '$2a$10$IwZHbXEeossI4Twlx7AtyeLfftjA/NVCghNZMv2x9rdcxUdWcH6oe'),
(2, 'admin', '$2a$10$4EjhT4o9juaW9ubn/2C9i.xXp6qX.PelJKDhf2jJpg5ZxfJ01afPG');