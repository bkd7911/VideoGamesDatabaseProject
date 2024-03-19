-- User Table
INSERT INTO users (UID, username, password, first_name, last_name, creation_date, last_access_date)
VALUES
  (1, 'user1', 'pass1', 'John', 'Doe', '2022-01-01', '2022-01-05'),
  (2, 'user2', 'pass2', 'Jane', 'Smith', '2022-02-02', '2022-02-10'),
  (3, 'user3', 'pass3', 'Alice', 'Johnson', '2022-03-03', '2022-03-15'),
  (4, 'user4', 'pass4', 'Bob', 'Brown', '2022-04-04', '2022-04-20'),
  (5, 'user5', 'pass5', 'Charlie', 'White', '2022-05-05', '2022-05-25');

-- Email Table
INSERT INTO "Email" (email, UID)
VALUES
  ('user1@example.com', 1),
  ('user2@example.com', 2),
  ('user3@example.com', 3),
  ('user4@example.com', 4),
  ('user5@example.com', 5);

-- Friend Table
INSERT INTO friends (uid1, uid2)
VALUES
  (1, 2),
  (2, 3),
  (3, 4),
  (4, 5),
  (5, 1);

-- VideoGame Table
INSERT INTO "VideoGame" (VGID, title, esrbRating)
VALUES
  (1, 'The Legend of Zelda: Breath of the Wild', 'E10+'),
  (2, 'Cyberpunk 2077', 'M'),
  (3, 'Stardew Valley', 'E'),
  (4, 'Celeste', 'E'),
  (5, 'Among Us', 'T');

-- PlayVideoGame Table
INSERT INTO "PlayVideoGame" (UID, VGID, sessionStart, sessionEnd)
VALUES
  (1, 1, '2022-01-02 12:00:00', '2022-01-02 12:05:00'),
  (2, 2, '2022-02-03 12:10:00', '2022-02-03 12:15:00'),
  (3, 3, '2022-03-04 12:20:00', '2022-03-04 12:25:00'),
  (4, 4, '2022-04-05 12:30:00', '2022-04-05 12:35:00'),
  (5, 5, '2022-05-06 12:40:00', '2022-05-06 12:45:00');

-- VideoGameRating Table
INSERT INTO "VideoGameRating" (UID, VGID, rating)
VALUES
  (1, 1, 4.5),
  (2, 2, 3.8),
  (3, 3, 5.0),
  (4, 4, 4.2),
  (5, 5, 4.7);

-- Platform Table
INSERT INTO "Platform" (PID, name)
VALUES
  (1, 'PlayStation'),
  (2, 'Xbox'),
  (3, 'PC'),
  (4, 'Nintendo Switch'),
  (5, 'Mobile');

-- UserPlatform Table
INSERT INTO "UserPlatform" (UID, PID)
VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5);

-- Release Table
INSERT INTO "Release" (PID, VGID, currPrice, releaseDate)
VALUES
  (1, 1, 59.99, '2022-01-15'),
  (2, 2, 49.99, '2022-02-20'),
  (3, 3, 29.99, '2022-03-25'),
  (4, 4, 39.99, '2022-04-10'),
  (5, 5, 19.99, '2022-05-05');

-- Collection Table
INSERT INTO "Collection" (CID, name, UID)
VALUES
  (1, 'Favorites', 1),
  (2, 'Must Play', 2),
  (3, 'Retro Games', 3),
  (4, 'Indie Gems', 4),
  (5, 'Completed', 5);

-- VideoGameCollection Table
INSERT INTO "VideoGameCollection" (CID, VGID)
VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5);

-- Genre Table
INSERT INTO "Genre" (GID, name)
VALUES
  (1, 'Action'),
  (2, 'RPG'),
  (3, 'Simulation'),
  (4, 'Indie'),
  (5, 'Party');

-- VideoGameGenre Table
INSERT INTO "VideoGameGenre" (VGID, GID)
VALUES
  (1, 2),
  (2, 1),
  (3, 3),
  (4, 4),
  (5, 5);

-- DevPub Table
INSERT INTO "DevPub" (DPID, name)
VALUES
  (1, 'Nintendo'),
  (2, 'CD Projekt'),
  (3, 'ConcernedApe'),
  (4, 'Maddy Makes Games'),
  (5, 'InnerSloth');

-- Developed Table
INSERT INTO "Developed" (DPID, VGID)
VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5);

-- Published Table
INSERT INTO "Published" (DPID, VGID)
VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5);
