CREATE SCHEMA IF NOT EXISTS `smoke_test` DEFAULT CHARACTER SET utf8mb4;

use smoke_test;

CREATE TABLE IF NOT EXISTS `smoke_test`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(16) NOT NULL,
  `pass` VARCHAR(64) NOT NULL,
  `fullname` VARCHAR(16) NOT NULL,
  `type` VARCHAR(8) NOT NULL,
  `mtime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `ctime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_name` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = 'user table';

CREATE TABLE IF NOT EXISTS `smoke_test`.`project` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `province` VARCHAR(8) NOT NULL,
  `city` VARCHAR(16) NOT NULL,
  `district` VARCHAR(16) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `room_count` INT UNSIGNED NOT NULL,
  `phone` VARCHAR(16) NOT NULL,
  `mtime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `ctime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_name` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = 'project table';

CREATE TABLE IF NOT EXISTS `smoke_test`.`user_project` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT UNSIGNED NOT NULL,
  `project_id` INT UNSIGNED NOT NULL,
  `mtime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `ctime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_user_project` (`user_id`, `project_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = 'user-project relationship table';

CREATE TABLE IF NOT EXISTS `smoke_test`.`sensor` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `eui` BIGINT UNSIGNED NOT NULL,
  `eui16` VARCHAR(16) NOT NULL,
  `model` VARCHAR(16) NOT NULL,
  `type` VARCHAR(16) NOT NULL,
  `location` VARCHAR(64) NOT NULL,
  `lati` VARCHAR(16) NOT NULL,
  `longi` VARCHAR(16) NOT NULL,
  `install_time` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `guarantee` VARCHAR(16) NOT NULL DEFAULT '在保',
  `status` VARCHAR(8) NOT NULL DEFAULT '正常',
  `project_id` INT UNSIGNED NOT NULL,
  `gateway_id` INT UNSIGNED NOT NULL,
  `phone` VARCHAR(16) NOT NULL,
  `mtime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `ctime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_eui` (`eui` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = 'sensor table';

CREATE TABLE IF NOT EXISTS `smoke_test`.`op_task` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `eui` BIGINT UNSIGNED NOT NULL,
  `post_user` INT UNSIGNED NOT NULL,
  `post_time` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `op_user` INT UNSIGNED NOT NULL,
  `op_time` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `cause` VARCHAR(16) NOT NULL,
  `handler` VARCHAR(16) NOT NULL,
  `worker` VARCHAR(16) NOT NULL,
  `status` VARCHAR(8) NOT NULL,
  `project_id` INT UNSIGNED NOT NULL,
  `mtime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  `ctime` TIMESTAMP NOT NULL DEFAULT '2017-11-16 15:45:50',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = 'operation task table';