CREATE TABLE IF NOT EXISTS `profiles` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) NOT NULL,
  `bind_user` varchar(64) NOT NULL,
  `skin_up_allow` bit(1) NOT NULL DEFAULT 0,
  `cape_up_allow` bit(1) NOT NULL DEFAULT 0,
  `skin_hash` varchar(255) NULL DEFAULT NULL,
  `cape_hash` varchar(255) NULL DEFAULT NULL,
  `skin_slim` bit(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `users` (
  `id` varchar(64) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `preferred_lang` varchar(8) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);