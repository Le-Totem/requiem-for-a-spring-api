CREATE TYPE mediatype AS ENUM ('PDF', 'IMAGE', 'VIDEO', 'MUSESCORE', 'TUXGUITAR');

CREATE TYPE user_role AS ENUM ('ADMIN', 'MODERATEUR', 'UTILISATEUR');

CREATE TYPE invit_status AS ENUM ('PENDING', 'ACCEPTED', 'REFUSED');

CREATE TABLE "user"(
   id UUID,
   firstname VARCHAR(50)  NOT NULL,
   lastname VARCHAR(50)  NOT NULL,
   email VARCHAR(50) UNIQUE NOT NULL,
   "password" VARCHAR(255)  NOT NULL,
   is_validated BOOLEAN NOT NULL,
   picture VARCHAR(150) ,
   PRIMARY KEY(id)
);

CREATE TABLE "group"(
   id SERIAL,
   "name" VARCHAR(50)  NOT NULL,
   creation_date DATE NOT NULL,
   is_everyone_admin BOOLEAN NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE genre(
   id SERIAL,
   "name" VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE instrument(
   id SERIAL,
   "name" VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE invitation(
   id SERIAL,
   email VARCHAR(50) NOT NULL,
   "status" invit_status NOT NULL default 'PENDING',
   created_at DATE NOT NULL,
   id_group INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_group) REFERENCES "group"(id)
);


CREATE TABLE music_piece(
   id SERIAL,
   title VARCHAR(50)  NOT NULL,
   author VARCHAR(50)  NOT NULL,
   "description" VARCHAR(250) ,
   id_group INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_group) REFERENCES "group"(id)
);

CREATE TABLE media(
   id SERIAL,
    title VARCHAR(200) NOT NULL,
   "type" mediatype NOT NULL,
   "url" VARCHAR(100) ,
   date_added DATE NOT NULL,
   date_modified DATE,
   id_track INTEGER NOT NULL,
   id_user UUID NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_track) REFERENCES music_piece(id),
   FOREIGN KEY(id_user) REFERENCES "user"(id)
);

CREATE TABLE user_group(
   id_user UUID,
   id_group INTEGER,
   "role" user_role NOT NULL,
   PRIMARY KEY(id_user, id_group),
   FOREIGN KEY(id_user) REFERENCES "user"(id),
   FOREIGN KEY(id_group) REFERENCES "group"(id)
);

CREATE TABLE music_piece_genre(
   id_track INT,
   id_genre INT,
   PRIMARY KEY(id_track, id_genre),
   FOREIGN KEY(id_track) REFERENCES music_piece(id),
   FOREIGN KEY(id_genre) REFERENCES genre(id)
);

CREATE TABLE media_instrument(
   id_media INTEGER,
   id_instrument INTEGER,
   PRIMARY KEY(id_media, id_instrument),
   FOREIGN KEY(id_media) REFERENCES media(id),
   FOREIGN KEY(id_instrument) REFERENCES instrument(id)
);