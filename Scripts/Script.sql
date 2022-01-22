CREATE database examenjorge;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `hashed_password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `user` 
VALUES 
(1,'user','$2a$10$AaBOqWFXPGHgpnaSKSmRB.BowydfCcRDEq4mlMaVF9pdkHhO27pyK','User Normal'),
(2,'admin','$2a$10$0nnbO4.gdr.i2Gt4njJNfeurond24/xtmhKJ1dbQieD22Pa1nX6Fu','Admin');

DROP TABLE IF EXISTS `user_roles`;

CREATE TABLE `user_roles` (
  `user_id` int NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  CONSTRAINT `user_roles` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)

INSERT INTO `user_roles` VALUES (1,'user'),(2,'admin'),(2,'user');

DROP TABLE IF EXISTS `cliente`;

CREATE TABLE `cliente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `celular` varchar(255) DEFAULT NULL,
  `correo_electronico` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `cliente` 
VALUES 
(1,'+52 2222542222','uno@ejemplo.com','1952-06-16','Uno'),
(2,'+52 2222542200','dos@ejemplo.com','2011-06-17','Dos');

UPDATE `cliente`
	SET correo_electronico='actualizado@ejemplo.com'
	WHERE id=2;


DELETE FROM `cliente`
	WHERE id=2;
