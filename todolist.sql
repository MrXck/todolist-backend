/*
Navicat MySQL Data Transfer

Source Server         : me
Source Server Version : 50644
Source Host           : localhost:3306
Source Database       : todolist

Target Server Type    : MYSQL
Target Server Version : 50644
File Encoding         : 65001

Date: 2023-09-29 20:52:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `id` bigint(20) NOT NULL,
  `title` varchar(128) NOT NULL,
  `detail` longtext NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for todo
-- ----------------------------
DROP TABLE IF EXISTS `todo`;
CREATE TABLE `todo` (
  `id` bigint(20) NOT NULL,
  `title` varchar(128) NOT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `is_done` tinyint(4) NOT NULL,
  `priority` smallint(6) NOT NULL,
  `start_time` date NOT NULL,
  `end_time` date NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `last_login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;
