CREATE TABLE IF NOT EXISTS tweets (id BIGINT PRIMARY KEY, hashtag VARCHAR(255), username VARCHAR(255), fullname VARCHAR(255), message VARCHAR(255), urlAvatar VARCHAR(255), localUrlAvatar VARCHAR(255), dateTime DATETIME, isRetweet BOOLEAN);
CREATE TABLE IF NOT EXISTS playlist (id IDENTITY, dateTime DATETIME, title VARCHAR(255), description VARCHAR(255));
CREATE TABLE IF NOT EXISTS playlistDetail (playlistId BIGINT, tweetId BIGINT);
CREATE TABLE IF NOT EXISTS tweetsProfileImage (tweetId BIGINT, profileImageType VARCHAR(255), profileImageUrl VARCHAR(255), localProfileImageUrl VARCHAR(255));

INSERT INTO tweets (id, hashtag, username, fullname, message, urlAvatar, localUrlAvatar, dateTime, isRetweet)
VALUES (731217831090630656, 'EnsayoDeFlorYPedroEnEEES', 'GPameluchis', 'Adicta A Flor Vigna', 'Que HERMOSA ES CANDE RUGGERI, NO DA MAS DE LINDA!!  #EnsayoDeFlorYPedroEnEEES', 'http://pbs.twimg.com/profile_images/726526454549336064/GI8Jonet_mini.jpg', '', '2016-05-13T17:21:30', FALSE);
INSERT INTO tweets (id, hashtag, username, fullname, message, urlAvatar, localUrlAvatar, dateTime, isRetweet)
VALUES (731217817962340352, 'EnsayoDeFlorYPedroEnEEES', 'NAHU_VIGNISTO', '2AñosJuntoAVosVigna', 'Diesiseis\n #EnsayoDeFlorYPedroEnEEES', 'http://pbs.twimg.com/profile_images/728296015669923840/6IC66J5R_mini.jpg', '', '2016-05-13T17:21:27', FALSE);

INSERT INTO playlist (id, dateTime, title, description)
VALUES (1, '2016-05-13T18:00:00', 'playlist default', '');

INSERT INTO playlistDetail (playlistId, tweetId) VALUES (1, 731217831090630656);
INSERT INTO playlistDetail (playlistId, tweetId) VALUES (1, 731217817962340352);


