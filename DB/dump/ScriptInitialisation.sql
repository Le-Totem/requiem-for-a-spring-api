CREATE TYPE mediatype AS ENUM ('PDF', 'image', 'musescore', 'TuxGuitar');

CREATE TYPE "role" AS ENUM ('Admin', 'Moderateur', 'Utilisateur');

CREATE TABLE user(
   id_user UUID,
   firstname VARCHAR(50)  NOT NULL,
   lastname VARCHAR(50)  NOT NULL,
   email VARCHAR(50)  NOT NULL,
   "password" VARCHAR(50)  NOT NULL,
   is_validated BOOLEAN NOT NULL,
   picture VARCHAR(50) ,
   PRIMARY KEY(id_user)
);

CREATE TABLE group(
   id_group INTEGER,
   "name" VARCHAR(50)  NOT NULL,
   creation_date DATE NOT NULL,
   is_everyone_admin BOOLEAN NOT NULL,
   PRIMARY KEY(id_group)
);

CREATE TABLE genre(
   id_genre INTEGER,
   "name" VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_genre)
);

CREATE TABLE instrument(
   id_instrument INTEGER,
   "name" VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_instrument)
);

CREATE TABLE music_piece(
   id_track INTEGER,
   title VARCHAR(50)  NOT NULL,
   author VARCHAR(50)  NOT NULL,
   "description" VARCHAR(250) ,
   id_genre INTEGER NOT NULL,
   id_group INTEGER NOT NULL,
   PRIMARY KEY(id_track),
   FOREIGN KEY(id_genre) REFERENCES genre(id_genre),
   FOREIGN KEY(id_group) REFERENCES group_(id_group)
);

CREATE TABLE media(
   id_media INTEGER,
   "type" mediatype NOT NULL,
   "url" VARCHAR(100) ,
   date_added DATE NOT NULL,
   date_modified DATE,
   id_track INTEGER NOT NULL,
   id_user UUID NOT NULL,
   PRIMARY KEY(id_media),
   FOREIGN KEY(id_track) REFERENCES music_piece(id_track),
   FOREIGN KEY(id_user) REFERENCES user_(id_user)
);

CREATE TABLE user_group(
   id_user UUID,
   id_group INTEGER,
   "role" "role" NOT NULL,
   PRIMARY KEY(id_user, id_group),
   FOREIGN KEY(id_user) REFERENCES user_(id_user),
   FOREIGN KEY(id_group) REFERENCES group_(id_group)
);

CREATE TABLE media_instrument(
   id_media INTEGER,
   id_instrument INTEGER,
   PRIMARY KEY(id_media, id_instrument),
   FOREIGN KEY(id_media) REFERENCES media(id_media),
   FOREIGN KEY(id_instrument) REFERENCES instrument(id_instrument)
);


