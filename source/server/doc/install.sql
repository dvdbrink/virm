CREATE DATABASE IF NOT EXISTS `virm`;

USE `virm`;

CREATE TABLE `painter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` char(20) NOT NULL,
  `last_name` char(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `painting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` char(50) NOT NULL,
  `description` text NOT NULL,
  `data` blob NOT NULL,
  `painter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `painter_id` (`painter_id`),
  CONSTRAINT `painter_id` FOREIGN KEY (`painter_id`) REFERENCES `painter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
