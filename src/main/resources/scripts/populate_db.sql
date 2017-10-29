LOCK TABLES `hackernews_test`.`user` WRITE;
INSERT INTO `hackernews_test`.`user` (ID, USERNAME, PASSWORD, FULLNAME) VALUES (1, "TestUser", "b0f3dc043a9c5c05f67651a8c9108b4c2b98e7246b2eea14cb204295", "Test User");
UNLOCK TABLES;