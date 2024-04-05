_________________________________________________________________________________
---------------------------------------------------------------------------------
SELECT 
    title,
    array_agg( distinct concat(release.curr_price)) priceS,
    array_agg( distinct concat(genre.name) ) genreS  ,
    array_agg( distinct concat(release.release_date )) dateS,
    array_agg( distinct concat(platforms.name )) platforms,
    array_agg( distinct concat(devpub.name )) devpubs,

    SUM(play_video_game.sessionend - play_video_game.sessionstart) playtime,
    AVG(video_game_rating.rating) rating

INTO tempSortTable<currentUID>

FROM videogame
    LEFT JOIN release ON release.vgid = videogame.vgid
    LEFT JOIN platforms ON platforms.pid = release.pid
    LEFT JOIN published ON videogame.vgid = published.vgid
    LEFT JOIN devpub ON published.dpid = devpub.dpid
    LEFT JOIN play_video_game ON videogame.vgid = play_video_game.vgid
    LEFT JOIN video_game_rating ON videogame.vgid = video_game_rating.vgid
    LEFT JOIN video_game_genre ON videogame.vgid = video_game_genre.vgid
    LEFT JOIN genre ON genre.gid = video_game_genre.gid

WHERE    videogame.title LIKE '%<title>%'
        /videogame.esrb_rating = '<rating>'
        /genre.name LIKE '%<genre>%'
        /platforms.name ='<platform>'
        /release.release_date <=/>=/= <dateVal>
        /devpub.name ='%<dev/pubname>%'
        /release.curr_price <=/>=/= price
GROUP BY title 
ORDER BY title ASC, dateS ASC;
_________________________________________________________________________________
---------------------------------------------------------------------------------
DROP TABLE tempSortTable<currentUID>;
_________________________________________________________________________________
---------------------------------------------------------------------------------

SELECT * FROM tempSortTable<currentUID> 
ORDER BY title/priceS/genreS/dateS;

