LOCK TABLES `hackernews_test`.`user` WRITE;
INSERT INTO `hackernews_test`.`user` (USERNAME, PASSWORD, FULLNAME) VALUES ("TestUser", "b0f3dc043a9c5c05f67651a8c9108b4c2b98e7246b2eea14cb204295", "Test User");
INSERT INTO `hackernews_test`.`user` (USERNAME, PASSWORD, FULLNAME) VALUES ("WOOT", "b0f3dc043a9c5c05f67651a8c9108b4c2b98e7246b2eea14cb204295", "WO OT");
UNLOCK TABLES;

LOCK TABLES `hackernews_test`.`post` WRITE;
INSERT INTO `hackernews_test`.`post` (TITLE, BODY, CREATED, UPDATED, AUTHOR_ID) VALUES ("Post1", "My post 1 text :-)", "2017-08-07 17:23:57", "2017-08-07 17:23:57", 1);
INSERT INTO `hackernews_test`.`post` (TITLE, BODY, CREATED, UPDATED, AUTHOR_ID) VALUES ("Post2", "My post 2 text :P", "2017-08-08 22:11:00", "2017-08-08 22:11:00", 1);
UNLOCK TABLES;

LOCK TABLES `hackernews_test`.`comment` WRITE;
INSERT INTO `hackernews_test`.`comment` (COMMENT_TEXT, CREATED, PARENTCOMMENTID, POST_ID, AUTHOR_ID) VALUES ("hej", now(), null, 2, 2);
INSERT INTO `hackernews_test`.`comment` (COMMENT_TEXT, CREATED, PARENTCOMMENTID, POST_ID, AUTHOR_ID) VALUES ("hej2", now(), null, 2, 2);
INSERT INTO `hackernews_test`.`comment` (COMMENT_TEXT, CREATED, PARENTCOMMENTID, POST_ID, AUTHOR_ID) VALUES ("hej3", now(), 1, 2, 2);
INSERT INTO `hackernews_test`.`comment` (COMMENT_TEXT, CREATED, PARENTCOMMENTID, POST_ID, AUTHOR_ID) VALUES ("hej4", now(), 1, 2, 2);
UNLOCK TABLES;