LOCK TABLES `hackernews_test`.`user` WRITE;
INSERT INTO `hackernews_test`.`user` (USERNAME, PASSWORD, FULLNAME) VALUES ("TestUser", "b0f3dc043a9c5c05f67651a8c9108b4c2b98e7246b2eea14cb204295", "Test User");
UNLOCK TABLES;

LOCK TABLES `hackernews_test`.`post` WRITE;
INSERT INTO `hackernews_test`.`post` (TITLE, BODY, CREATED, UPDATED, AUTHOR_ID) VALUES ("Post1", "My post 1 text :-)", "2017-08-07 17:23:57", "2017-08-07 17:23:57", 1);
INSERT INTO `hackernews_test`.`post` (TITLE, BODY, CREATED, UPDATED, AUTHOR_ID) VALUES ("Post2", "My post 2 text :P", "2017-08-08 22:11:00", "2017-08-08 22:11:00", 1);
UNLOCK TABLES;