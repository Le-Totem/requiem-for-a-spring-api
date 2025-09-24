-- ⚡ extension pour UUID
create extension if not exists pgcrypto;

-- 👤 Utilisateurs
insert into "user" ("id", "firstname", "lastname", "email", "password", "is_validated", "picture") values
                                                                                                       (gen_random_uuid(), 'Alice', 'Durand', 'alice@example.com', 'hashedpass1', true, 'alice.png'),
                                                                                                       (gen_random_uuid(), 'Bob', 'Martin', 'bob@example.com', 'hashedpass2', true, 'bob.png'),
                                                                                                       (gen_random_uuid(), 'Charlie', 'Petit', 'charlie@example.com', 'hashedpass3', false, null);

-- 👥 Groupes
insert into "group" (name, creation_date, is_everyone_admin) values
                                                                 ('Jazz Lovers', '2023-01-15', false),
                                                                 ('Rock Band', '2024-02-20', true);

-- 🎶 Genres
insert into genre (name) values
                             ('Jazz'),
                             ('Rock'),
                             ('Classical'),
                             ('Pop');

-- 🎸 Instruments
insert into instrument (name) values
                                  ('Guitar'),
                                  ('Piano'),
                                  ('Drums'),
                                  ('Violin');

-- 🎼 Morceaux
insert into music_piece (title, author, description, id_genre, id_group) values
                                                                             ('Autumn Leaves', 'Joseph Kosma', 'Famous jazz standard', 1, 1),
                                                                             ('Bohemian Rhapsody', 'Queen', 'Classic rock masterpiece', 2, 2);

-- 📂 Médias
insert into media (type, url, date_added, date_modified, id_track, id_user) values
                                                                                ('PDF', 'scores/autumn_leaves.pdf', '2024-03-10', null, 1,
                                                                                 (select id from "user" where email='alice@example.com')),
                                                                                ('image', 'covers/bohemian.png', '2024-04-05', '2024-04-10', 2,
                                                                                 (select id from "user" where email='bob@example.com')),
                                                                                ('musescore', 'musescore/bohemian_rhapsody', '2024-05-01', null, 2,
                                                                                 (select id from "user" where email='charlie@example.com'));

-- 🔑 Utilisateurs ↔ Groupes
insert into user_group (id_user, id_group, role)
select id, 1, 'Admin' from "user" where email='alice@example.com';
insert into user_group (id_user, id_group, role)
select id, 2, 'Moderateur' from "user" where email='bob@example.com';
insert into user_group (id_user, id_group, role)
select id, 1, 'Utilisateur' from "user" where email='charlie@example.com';

-- 🎵 Médias ↔ Instruments
insert into media_instrument (id_media, id_instrument) values
                                                           (1, 2),  -- Autumn Leaves PDF → Piano
                                                           (2, 1),  -- Bohemian Rhapsody image → Guitar
                                                           (3, 3);  -- Bohemian Rhapsody musescore → Drums
