# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20062
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 47.101.10.94 (MySQL 5.7.44)
# 数据库: encore_booking
# 生成时间: 2023-12-23 10:18:36 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# 转储表 agent
# ------------------------------------------------------------

LOCK TABLES `agent` WRITE;
/*!40000 ALTER TABLE `agent` DISABLE KEYS */;

INSERT INTO `agent` (`id`, `create_date`, `is_deleted`, `update_date`, `version`, `mobile`, `name`, `status`)
VALUES
    (1,'2023-12-23 17:46:48.452147',b'0','2023-12-23 17:46:48.452217',0,'13312345678','王一','ACTIVE'),
    (2,'2023-12-23 17:47:05.827265',b'0','2023-12-23 17:47:05.827281',0,'13812345678','王二','ACTIVE');

/*!40000 ALTER TABLE `agent` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 agent_ticket_quota
# ------------------------------------------------------------

LOCK TABLES `agent_ticket_quota` WRITE;
/*!40000 ALTER TABLE `agent_ticket_quota` DISABLE KEYS */;

INSERT INTO `agent_ticket_quota` (`id`, `create_date`, `is_deleted`, `update_date`, `version`, `price`, `remaining_quantity`, `status`, `total_quantity`, `agent_id`, `inventory_id`)
VALUES
    (1,'2023-12-23 17:47:49.337198',b'0','2023-12-23 17:47:49.337216',0,40000,50,'INACTIVE',50,1,1),
    (2,'2023-12-23 18:07:51.590033',b'0','2023-12-23 18:07:51.590053',0,10000,50,'INACTIVE',50,2,1);

/*!40000 ALTER TABLE `agent_ticket_quota` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 concert
# ------------------------------------------------------------

LOCK TABLES `concert` WRITE;
/*!40000 ALTER TABLE `concert` DISABLE KEYS */;

INSERT INTO `concert` (`id`, `create_date`, `is_deleted`, `update_date`, `version`, `name`, `status`)
VALUES
    (1,'2023-12-23 17:43:10.449059',b'0','2023-12-23 17:43:10.449089',0,'【上海】于文文魔方世界巡回演唱会','ACTIVE');

/*!40000 ALTER TABLE `concert` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 inventory
# ------------------------------------------------------------

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;

INSERT INTO `inventory` (`id`, `create_date`, `is_deleted`, `update_date`, `version`, `remaining_quantity`, `status`, `total_quantity`, `unallocated_quantity`, `concert_id`, `session_id`, `ticket_category_id`)
VALUES
    (1,'2023-12-23 17:46:04.348742',b'0','2023-12-23 10:07:51.671640',3,100,'ACTIVE',100,0,1,1,1),
    (2,'2023-12-23 17:46:18.923766',b'0','2023-12-23 09:47:24.972409',1,100,'ACTIVE',100,100,1,1,2),
    (3,'2023-12-23 17:46:31.540928',b'0','2023-12-23 09:47:22.862604',1,100,'ACTIVE',100,100,1,1,3);

/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 session
# ------------------------------------------------------------

LOCK TABLES `session` WRITE;
/*!40000 ALTER TABLE `session` DISABLE KEYS */;

INSERT INTO `session` (`id`, `create_date`, `is_deleted`, `update_date`, `version`, `name`, `status`, `concert_id`)
VALUES
    (1,'2023-12-23 17:44:34.735724',b'0','2023-12-23 17:44:34.735772',0,'2023012023 周六 19:30','ACTIVE',1);

/*!40000 ALTER TABLE `session` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 ticket_category
# ------------------------------------------------------------

LOCK TABLES `ticket_category` WRITE;
/*!40000 ALTER TABLE `ticket_category` DISABLE KEYS */;

INSERT INTO `ticket_category` (`id`, `create_date`, `is_deleted`, `update_date`, `version`, `name`, `status`, `concert_id`)
VALUES
    (1,'2023-12-23 17:45:00.803078',b'0','2023-12-23 17:45:00.803098',0,'看台480元','ACTIVE',1),
    (2,'2023-12-23 17:45:13.714565',b'0','2023-12-23 17:45:13.714628',0,'看台780元','ACTIVE',1),
    (3,'2023-12-23 17:45:23.193514',b'0','2023-12-23 17:45:23.193529',0,'看台680元','ACTIVE',1),
    (4,'2023-12-23 17:45:35.633391',b'0','2023-12-23 17:45:35.633405',0,'内场980元','ACTIVE',1),
    (5,'2023-12-23 17:45:48.194325',b'0','2023-12-23 17:45:48.194354',0,'内场1380元','ACTIVE',1);

/*!40000 ALTER TABLE `ticket_category` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 users
# ------------------------------------------------------------

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`id`, `create_date`, `is_deleted`, `update_date`, `version`, `account_id`, `name`, `password`)
VALUES
    (1,NULL,b'0',NULL,0,'admin','管理员','96e79218965eb72c92a549dd5a330112');

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 users_roles
# ------------------------------------------------------------

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;

INSERT INTO `users_roles` (`users_id`, `roles`)
VALUES
    (1,'admin');

/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
