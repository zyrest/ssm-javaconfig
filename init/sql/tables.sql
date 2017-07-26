/*
SQLyog 企业版 - MySQL GUI v7.14 
MySQL - 5.6.16-log : Database - 
*********************************************************************
*/
/*表结构插入*/
DROP TABLE IF EXISTS `shiro_permission`;
CREATE TABLE `shiro_permission` (
  `permission_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(256) DEFAULT NULL COMMENT 'url地址',
  `describe` varchar(64) DEFAULT NULL COMMENT 'url描述',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*Table structure for table `u_role` */
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `type` varchar(10) DEFAULT NULL COMMENT '角色类型',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*Table structure for table `u_role_permission` */
DROP TABLE IF EXISTS `shiro_role_permission`;
CREATE TABLE `shiro_role_permission` (
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*Table structure for table `u_user` */
DROP TABLE IF EXISTS `shiro_user`;
CREATE TABLE `shiro_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱|登录帐号',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` bigint(1) DEFAULT '1' COMMENT '1:有效，0:禁止登录',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*Table structure for table `u_user_role` */
DROP TABLE IF EXISTS `shiro_user_role`;
CREATE TABLE `shiro_user_role` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
