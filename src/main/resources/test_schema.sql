drop all objects;

create table if not exists GENRES
(
    GENRE_ID   INTEGER               not null,
    GENRE_NAME CHARACTER VARYING(20) not null,
    constraint GENRE_ID
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID   INTEGER               not null,
    MPA_NAME CHARACTER VARYING(10) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);

create table if not exists FILMS
(
    FILM_ID          INTEGER,
    FILM_NAME        CHARACTER VARYING(200) not null,
    FILM_DESCRIPTION CHARACTER VARYING(200) not null,
    RELEASE_DATE     DATE                   not null,
    FILM_DURATION    INTEGER                not null,
    FILM_MPA         INTEGER                not null,
    constraint FILM_ID
        primary key (FILM_ID),
    constraint "foreign_key_MPA"
        foreign key (FILM_MPA) references MPA
);

create table if not exists FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRES_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FOREIGN_KEY_FG_FILMS
        foreign key (FILM_ID) references FILMS,
    constraint FOREIGN_KEY_FG_GENRES
        foreign key (GENRE_ID) references GENRES
);

create table if not exists USERS
(
    USER_ID    INTEGER,
    USER_NAME  CHARACTER VARYING(50),
    USER_EMAIL CHARACTER VARYING(200) not null,
    USER_LOGIN CHARACTER VARYING(50)  not null,
    BIRTHDAY   DATE                   not null,
    constraint USER_ID
        primary key (USER_ID)
);

create table if not exists FILM_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILM_LIKES_PK
        primary key (USER_ID, FILM_ID),
    constraint FILM_LIKES_FILMS_FK
        foreign key (FILM_ID) references FILMS,
    constraint FOREIGN_KEY_USER_ID
        foreign key (USER_ID) references USERS
);

create unique index "Users_Email_unq"
    on USERS (USER_EMAIL);

create unique index "Users_Login_unq"
    on USERS (USER_LOGIN);

create table IF NOT EXISTS USER_FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint KEY_NAME_USER_ID
        primary key (USER_ID, FRIEND_ID),
    constraint FOREIGN_KEY_FRIENDS
        foreign key (FRIEND_ID) references USERS,
    constraint FOREIGN_KEY_USERS
        foreign key (USER_ID) references USERS
);

