merge into MPA (MPA_ID, MPA_NAME)
    values (1, 'G');
merge into MPA (MPA_ID, MPA_NAME)
    values (2, 'PG');
merge into MPA (MPA_ID, MPA_NAME)
    values (3, 'PG-13');
merge into MPA (MPA_ID, MPA_NAME)
    values (4, 'R');
merge into MPA (MPA_ID, MPA_NAME)
    values (5, 'NC-17');

merge into GENRES (GENRE_ID, GENRE_NAME)
    values (1, 'Комедия');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (2, 'Драма');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (3, 'Мультфильм');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (4, 'Триллер');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (5, 'Документальный');
merge into GENRES (GENRE_ID, GENRE_NAME)
    values (6, 'Боевик');

merge into USERS (USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
    values ( 1, 'Email1@mail.com', 'Login1', 'Name1', DATE '1981-07-11' );
merge into USERS (USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
    values ( 2, 'Email2@mail.com', 'Login2', 'Name2', DATE '1985-10-15' );
merge into USERS (USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
    values ( 3, 'Email3@mail.com', 'Login3', 'Name3', DATE '1980-11-10' );

merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 1, 'name1', 'description1', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 2, 'name2', 'description2', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 3, 'name3', 'description3', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 4, 'name4', 'description4', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 5, 'name5', 'description5', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 6, 'name6', 'description6', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 7, 'name7', 'description7', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 8, 'name8', 'description8', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 9, 'name9', 'description9', DATE '1999-10-11', 123, 1 );
merge into FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_MPA)
    values ( 10, 'name10', 'description10', DATE '1999-10-11', 123, 1 );
