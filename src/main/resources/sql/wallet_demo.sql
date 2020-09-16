/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.26 : Database - wallet_demo
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wallet_demo` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wallet_demo`;

/*Table structure for table `t_feedback` */

DROP TABLE IF EXISTS `t_feedback`;

CREATE TABLE `t_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(11) DEFAULT NULL COMMENT '用户id',
  `username` varchar(255) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL COMMENT '反馈内容',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `t_feedback` */

insert  into `t_feedback`(`id`,`uid`,`username`,`content`,`gmt_create`,`gmt_modified`) values (4,'10132','admin2','zxczxczxczcx','2018-02-11 13:58:50','2018-02-11 13:58:50'),(5,'1','admin','zxczxcxc','2018-02-11 19:20:32','2018-02-11 19:20:32');

/*Table structure for table `t_users` */

DROP TABLE IF EXISTS `t_users`;

CREATE TABLE `t_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(11) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `status` int(4) NOT NULL COMMENT '状态：0正常 1禁用',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `id` (`id`,`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10134 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

/*Data for the table `t_users` */

insert  into `t_users`(`id`,`username`,`password`,`status`,`gmt_create`,`gmt_modified`) values (1,'admin','admin',1,'2016-04-15 10:59:21','2016-09-05 14:29:53'),(10131,'admin1','admin1',1,'2018-02-10 23:04:53','2018-02-10 23:04:53'),(10132,'admin2','admin2',1,'2018-02-11 13:58:00','2018-02-11 13:58:00'),(10133,'admin3','123456',1,'2019-11-15 13:34:18','2019-11-15 13:34:18');

/*Table structure for table `t_wallet` */

DROP TABLE IF EXISTS `t_wallet`;

CREATE TABLE `t_wallet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` int(11) NOT NULL COMMENT '用户id',
  `name` varchar(150) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '钱包名',
  `address` varchar(50) NOT NULL COMMENT '地址',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `private_key` varchar(100) NOT NULL COMMENT '私钥',
  `key_store` varchar(1000) DEFAULT NULL COMMENT 'key_store',
  `mnemonic` varchar(1000) DEFAULT NULL COMMENT '助记词',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10019 DEFAULT CHARSET=utf8 COMMENT='钱包表';

/*Data for the table `t_wallet` */

insert  into `t_wallet`(`id`,`uid`,`name`,`address`,`password`,`private_key`,`key_store`,`gmt_create`,`gmt_modified`) values (10013,10133,'UTC--2019-11-16T02-44-19.813000000Z--2da65f698f8f8c4db8d4a1806f836cb3b6469d1f.json','0x2da65f698f8f8c4db8d4a1806f836cb3b6469d1f','12345678','f0d81e36f9fb73c886f7595b596b941ae93347c3856e9ba73c29e63a2c6c839b','{\"address\":\"2da65f698f8f8c4db8d4a1806f836cb3b6469d1f\",\"id\":\"bcd99b91-b048-4b63-a3d5-c3f8ed83d5f5\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"56495421559784e8ebf31816d18033e30a64d9b25f08475bb22f0bd3ecf4b8f1\",\"cipherparams\":{\"iv\":\"3bae898c62b428d30aaf1b2c8694f82f\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"39d3b32983e68210a1a17b7209b9d2b8ab5bb6c4b9ac3a2237ffce09a98ccf65\"},\"mac\":\"7e6766651e95537e4107d3ab485ee41443fda266ca5332f37bc92c5654d09e02\"}}','2019-11-16 10:44:22','2019-11-16 10:44:22'),(10014,10133,'UTC--2019-11-16T05-11-25.981000000Z--79ab774fe1edfedc02bf39ba34e657f0b1ba1304.json','0x79ab774fe1edfedc02bf39ba34e657f0b1ba1304','12345678','2fc1038854698903c35f02a71862f15b6df03299bbf2444e554d0c65a89f44d','{\"address\":\"79ab774fe1edfedc02bf39ba34e657f0b1ba1304\",\"id\":\"a8c95a62-8766-4b4e-b3ab-fcba8be3c158\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"f0be83dc51ed6afbca8c0a0dc815866884e305505726f80a0cf234775dab95a8\",\"cipherparams\":{\"iv\":\"7c5559879d4e65f73a5bcd8be6883a0b\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"21323e06f0e0b6a04d84d684d77bd7f0d39608f2679afaec0a0bd0e60c702281\"},\"mac\":\"6810afbcaf0f1df4f4489e32efe470d2ae2d7d8660b3d0b9f64bc9f46aff1c1a\"}}','2019-11-16 13:12:17','2019-11-16 13:12:17'),(10015,10133,'UTC--2019-11-16T06-07-56.818000000Z--03943de1b02f0aa6dfec088c10e52a97eeeb92e4.json','0x03943de1b02f0aa6dfec088c10e52a97eeeb92e4','123456','bf4adf3b8705f3575c27d76e4444a22a2056272b2c83c4373a104fed6a1a68e7','{\"address\":\"03943de1b02f0aa6dfec088c10e52a97eeeb92e4\",\"id\":\"ce266d85-1981-4e76-ab67-debe69bea232\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"5b3582d94922bf3613264e9b7c7c3ac8d068e71e675cc57ec5b9ce3ab55fc09d\",\"cipherparams\":{\"iv\":\"bdedc3eb76c9403fadfa318ebe2b48ac\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"64e902ba450a8168fa59ec8914362832b8d7e7f449939d6a3eabe7c0ca865569\"},\"mac\":\"33325d54408a0a71529200161434a357de76a5ba16c18a02966df29a0971cc3c\"}}','2019-11-16 14:08:40','2019-11-16 14:08:40'),(10016,10133,'UTC--2019-11-16T06-38-29.119000000Z--c0b9d21befc7cac1223fa6062c0dbf7915567b8b.json','0xc0b9d21befc7cac1223fa6062c0dbf7915567b8b','12345678','4017b0f05bf3103c2a8f5f9c8a6893a87fa533fad282631db49122bd070d9be9','{\"address\":\"c0b9d21befc7cac1223fa6062c0dbf7915567b8b\",\"id\":\"6f2d0f01-3a56-4188-b286-364215c64f7e\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"b246f439f985edf1e9184a77cff89080e1482fcda6d6e96eb0d61a856e07b921\",\"cipherparams\":{\"iv\":\"9cd350a682a248d46f4bcb6d18839450\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"420bea45f0aeae9a693e559efdef8cac0d999d1bb40fc11f6a56379da2fbbc25\"},\"mac\":\"2888149659777ceff44863cd59389d0fd2d35eb99dfce0623561aa622c1cb13c\"}}','2019-11-16 14:38:32','2019-11-16 14:38:32'),(10017,10133,'UTC--2019-11-16T06-43-28.681000000Z--e499425ffc010080335108cc1615c1abbf52795c.json','0xe499425ffc010080335108cc1615c1abbf52795c','12345698','d9a00090f1ba70cdf38f56b55cf735f310f0daccba792b96506279873c4ff894','{\"address\":\"e499425ffc010080335108cc1615c1abbf52795c\",\"id\":\"c95d3714-69ed-4b30-ad30-0e8704dd0c07\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"9e95ba63b541a59c47bf8a79629872d9ae95076f8ba77c6c38ef5d52cf483ce0\",\"cipherparams\":{\"iv\":\"794ad846033818b13f8090fa89f0eee2\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"d9c6c1294cf3932442e3886356d490aaa9fac4c1df3ad53fdae13c7ff5cfe957\"},\"mac\":\"b1894c1a269710de0a5d3584340657c00c59ccaf42ab62484545aa05b2b6da59\"}}','2019-11-16 14:43:32','2019-11-16 14:43:32'),(10018,10132,'UTC--2019-11-16T06-46-48.431000000Z--0ce44dbb943fe2c7f86e6749426d8c1f5579cbd3.json','0x0ce44dbb943fe2c7f86e6749426d8c1f5579cbd3','123456','525b721fcbfe02ef3a4c2e48d7bba38bcfc2daaca40d42d4dd5acdba18282dab','{\"address\":\"0ce44dbb943fe2c7f86e6749426d8c1f5579cbd3\",\"id\":\"11de1482-a7e2-486f-ac00-8eb036e7fdd6\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"06b385731998e8348615165f959f1a5dbf9c832e19fb6c077937a0468ff65560\",\"cipherparams\":{\"iv\":\"42300d6435cd89b8a9f58aa69fd1f80b\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"643e601461a88b772b26b2f0d479e9d06ac75b63c90fb8c290e30b3d021a29eb\"},\"mac\":\"7df17130c6981143a1990d6152080dd9e7332e474904e97a1a4dda15dd26ff72\"}}','2019-11-16 14:46:51','2019-11-16 14:46:51');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
