CREATE TABLE emails (
    email 	VARCHAR(50) PRIMARY KEY,
    uid		INT NOT NULL,
    CONSTRAINT fk_emails_uid
FOREIGN KEY(uid)
REFERENCES users(uid)
ON UPDATE CASCADE
ON DELETE CASCADE
);

CREATE TABLE friends (
    uid1		INT NOT NULL,
    uid2		INT NOT NULL,
    CONSTRAINT fk_friend_uid1
FOREIGN KEY(uid1)
REFERENCES users(uid)
ON UPDATE CASCADE
ON DELETE CASCADE,
    CONSTRAINT fk_friend_uid2
FOREIGN KEY(uid2)
REFERENCES users(uid)
ON UPDATE CASCADE
ON DELETE CASCADE
);

CREATE TABLE platforms (
    pid		 SERIAL  PRIMARY KEY,
    name	VARCHAR  NOT NULL
);

CREATE TABLE session(
    sid		SERIAL  PRIMARY KEY,
    uid		INT NOT NULL,
    vgid		INT NOT NULL,
    sessionStart	timestamp NOT NULL,
    sessionEnd		timestamp,
    CONSTRAINT fk_ses_uid
FOREIGN KEY(uid)
REFERENCES users(uid)
ON UPDATE CASCADE
ON DELETE CASCADE,

    CONSTRAINT fk_ses_vgid
FOREIGN KEY(vgid)
REFERENCES videogame (vgid)
ON UPDATE CASCADE
ON DELETE CASCADE
);

CREATE TABLE collections (
    cid		SERIAL PRIMARY KEY,
    uid		INT NOT NULL,
    name	VARCHAR(20) NOT NULL,
    CONSTRAINT fk_colec_uid
FOREIGN KEY(uid)
REFERENCES users(uid)
ON UPDATE CASCADE
ON DELETE CASCADE
);

CREATE TABLE videogame (
    vgid			SERIAL PRIMARY KEY,
    title			VARCHAR(20) NOT NULL
--     esrb_rating		enum(‘E’, ‘E10+’, ‘T’, ‘M’, ‘AO’)
);

CREATE TABLE genre (
    gid		SERIAL PRIMARY KEY,
    name	VARCHAR(20) NOT NULL
);

CREATE TABLE devpub (
    dpid		SERIAL PRIMARY KEY,
    name	VARCHAR(20) NOT NULL
);

CREATE TABLE developed (
    dpid        INT NOT NULL,
    vgid        INT NOT NULL,


    CONSTRAINT FK_dev_dpid
        FOREIGN KEY (dpid)
            REFERENCES devpub(dpid)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
    CONSTRAINT FK_dev_vgid
        FOREIGN KEY (vgid)
            REFERENCES videogame(vgid)
                ON UPDATE CASCADE
                ON DELETE CASCADE
);

CREATE TABLE published (
    dpid        INT NOT NULL,
    vgid        INT NOT NULL,
    CONSTRAINT FK_dev_dpid
        FOREIGN KEY (dpid)
            REFERENCES devpub(dpid)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
    CONSTRAINT FK_dev_vgid
        FOREIGN KEY (vgid)
            REFERENCES videogame(vgid)
                ON UPDATE CASCADE
                ON DELETE CASCADE
);
