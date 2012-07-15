/*
Navicat MySQL Data Transfer

Source Server         : palo@localhost
Source Server Version : 50511
Source Host           : localhost:3306
Source Database       : wpalodatabase

Target Server Type    : MYSQL
Target Server Version : 50511
File Encoding         : 65001

Date: 2012-06-06 13:59:36
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(80) DEFAULT NULL,
  `PASSWORD` varchar(80) DEFAULT NULL,
  `CONNECTION_ID` int(10) unsigned NOT NULL,
  `USER_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `CONNECTION_ID` (`CONNECTION_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`CONNECTION_ID`) REFERENCES `connections` (`ID`),
  CONSTRAINT `accounts_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES ('1', 'admin', 'admin', '1', '1');
INSERT INTO `accounts` VALUES ('2', 'editor', 'editor', '1', '2');
INSERT INTO `accounts` VALUES ('3', 'viewer', 'viewer', '1', '3');
INSERT INTO `accounts` VALUES ('4', 'poweruser', 'poweruser', '1', '4');
INSERT INTO `accounts` VALUES ('5', 'editor', 'editor', '1', '5');
INSERT INTO `accounts` VALUES ('6', 'editor', 'editor', '1', '6');
INSERT INTO `accounts` VALUES ('7', 'viewer', 'viewer', '1', '7');
INSERT INTO `accounts` VALUES ('8', 'admin', 'admin', '2', '1');

-- ----------------------------
-- Table structure for `connections`
-- ----------------------------
DROP TABLE IF EXISTS `connections`;
CREATE TABLE `connections` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(80) DEFAULT NULL,
  `HOST` varchar(255) DEFAULT NULL,
  `SERVICE` varchar(255) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of connections
-- ----------------------------
INSERT INTO `connections` VALUES ('1', 'Palo', 'localhost', '7777', '2', '');
INSERT INTO `connections` VALUES ('2', 'Mondrian-bicp', 'localhost:8080', 'mondrian/xmla', '3', 'mondrian test.');
INSERT INTO `connections` VALUES ('3', 'MS AS 2000', 'localhost', 'xmla/msxisapi.dll', '3', '');
INSERT INTO `connections` VALUES ('4', 'MS AS 2005', 'localhost', 'olap/msmdpump.dll', '3', '');
INSERT INTO `connections` VALUES ('5', 'SAP BW', '10.0.0.113:8011', 'sap/bw/xml/soap/xmla', '3', '');

-- ----------------------------
-- Table structure for `folders`
-- ----------------------------
DROP TABLE IF EXISTS `folders`;
CREATE TABLE `folders` (
  `ID` varchar(80) NOT NULL,
  `NAME` varchar(80) DEFAULT NULL,
  `OWNER` int(10) unsigned NOT NULL,
  `TYPE` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `OWNER` (`OWNER`),
  CONSTRAINT `folders_ibfk_1` FOREIGN KEY (`OWNER`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of folders
-- ----------------------------
INSERT INTO `folders` VALUES ('fe_28edb2f0-4a12-4895-b44d-db8b2b10def2', 'Sales', '1', '3');
INSERT INTO `folders` VALUES ('fe_906f133a-b8df-4e9e-ae87-c2fd22dfbc56', 'Store', '1', '3');
INSERT INTO `folders` VALUES ('fe_b89f3336-a1f5-40e9-93c5-7ef8d0a5bf0f', 'Warehouse', '1', '3');
INSERT INTO `folders` VALUES ('fe_c3a3253e-53f2-4715-92b0-956c4acd25d9', 'HR', '1', '3');
INSERT INTO `folders` VALUES ('sf_73be9e9f-76dc-4c67-b99c-7c10458df884', 'admin', '1', '2');

-- ----------------------------
-- Table structure for `folders_roles_association`
-- ----------------------------
DROP TABLE IF EXISTS `folders_roles_association`;
CREATE TABLE `folders_roles_association` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FOLDER_ID` varchar(80) NOT NULL,
  `ROLE_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FOLDER_ID` (`FOLDER_ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `folders_roles_association_ibfk_1` FOREIGN KEY (`FOLDER_ID`) REFERENCES `folders` (`ID`),
  CONSTRAINT `folders_roles_association_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `roles` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of folders_roles_association
-- ----------------------------
INSERT INTO `folders_roles_association` VALUES ('2', 'fe_c3a3253e-53f2-4715-92b0-956c4acd25d9', '3');
INSERT INTO `folders_roles_association` VALUES ('3', 'fe_28edb2f0-4a12-4895-b44d-db8b2b10def2', '3');
INSERT INTO `folders_roles_association` VALUES ('4', 'fe_906f133a-b8df-4e9e-ae87-c2fd22dfbc56', '3');
INSERT INTO `folders_roles_association` VALUES ('6', 'fe_b89f3336-a1f5-40e9-93c5-7ef8d0a5bf0f', '3');

-- ----------------------------
-- Table structure for `groups`
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(80) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of groups
-- ----------------------------
INSERT INTO `groups` VALUES ('1', 'admin', 'Grants the right to view & edit administration area and modify & share all existing views (System)');
INSERT INTO `groups` VALUES ('2', 'editor', 'Grants the right to modify views shared by other users (System)');
INSERT INTO `groups` VALUES ('3', 'viewer', 'Grants the right to see views shared by other users (System)');
INSERT INTO `groups` VALUES ('4', 'poweruser', 'Grants the right to share views created by this user');
INSERT INTO `groups` VALUES ('5', 'creator', 'Grants the right to create and modify own views');
INSERT INTO `groups` VALUES ('6', 'publisher', 'Grants the right to create, modify and publish own views');

-- ----------------------------
-- Table structure for `groups_roles_association`
-- ----------------------------
DROP TABLE IF EXISTS `groups_roles_association`;
CREATE TABLE `groups_roles_association` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `GROUP_ID` int(10) unsigned NOT NULL,
  `ROLE_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `GROUP_ID` (`GROUP_ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `groups_roles_association_ibfk_1` FOREIGN KEY (`GROUP_ID`) REFERENCES `groups` (`ID`),
  CONSTRAINT `groups_roles_association_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `roles` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of groups_roles_association
-- ----------------------------
INSERT INTO `groups_roles_association` VALUES ('1', '1', '1');
INSERT INTO `groups_roles_association` VALUES ('2', '1', '2');
INSERT INTO `groups_roles_association` VALUES ('3', '1', '3');
INSERT INTO `groups_roles_association` VALUES ('4', '2', '2');
INSERT INTO `groups_roles_association` VALUES ('5', '2', '3');
INSERT INTO `groups_roles_association` VALUES ('6', '3', '3');
INSERT INTO `groups_roles_association` VALUES ('7', '4', '5');
INSERT INTO `groups_roles_association` VALUES ('8', '4', '2');
INSERT INTO `groups_roles_association` VALUES ('9', '4', '3');
INSERT INTO `groups_roles_association` VALUES ('10', '5', '6');
INSERT INTO `groups_roles_association` VALUES ('11', '5', '3');
INSERT INTO `groups_roles_association` VALUES ('12', '6', '7');
INSERT INTO `groups_roles_association` VALUES ('13', '6', '3');

-- ----------------------------
-- Table structure for `reports`
-- ----------------------------
DROP TABLE IF EXISTS `reports`;
CREATE TABLE `reports` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(80) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `OWNER` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `OWNER` (`OWNER`),
  CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`OWNER`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reports
-- ----------------------------

-- ----------------------------
-- Table structure for `reports_roles_association`
-- ----------------------------
DROP TABLE IF EXISTS `reports_roles_association`;
CREATE TABLE `reports_roles_association` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(10) unsigned NOT NULL,
  `ROLE_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `reports_roles_association_ibfk_1` FOREIGN KEY (`REPORT_ID`) REFERENCES `reports` (`ID`),
  CONSTRAINT `reports_roles_association_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `roles` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reports_roles_association
-- ----------------------------

-- ----------------------------
-- Table structure for `reports_views_association`
-- ----------------------------
DROP TABLE IF EXISTS `reports_views_association`;
CREATE TABLE `reports_views_association` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `REPORT_ID` int(10) unsigned NOT NULL,
  `VIEW_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `REPORT_ID` (`REPORT_ID`),
  KEY `VIEW_ID` (`VIEW_ID`),
  CONSTRAINT `reports_views_association_ibfk_1` FOREIGN KEY (`REPORT_ID`) REFERENCES `reports` (`ID`),
  CONSTRAINT `reports_views_association_ibfk_2` FOREIGN KEY (`VIEW_ID`) REFERENCES `views` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reports_views_association
-- ----------------------------

-- ----------------------------
-- Table structure for `repositoryfolders`
-- ----------------------------
DROP TABLE IF EXISTS `repositoryfolders`;
CREATE TABLE `repositoryfolders` (
  `user` varchar(255) NOT NULL DEFAULT '',
  `folder` text,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of repositoryfolders
-- ----------------------------
INSERT INTO `repositoryfolders` VALUES ('1', '<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<?palofolder version=\"0.1\"?>\r\n<folder>\r\n<staticFolder id=\"sf_73be9e9f-76dc-4c67-b99c-7c10458df884\" name=\"admin\" >\n<folderElement id=\"fe_c3a3253e-53f2-4715-92b0-956c4acd25d9\" name=\"HR\" source=\"1\" >\n</folderElement>\n<folderElement id=\"fe_28edb2f0-4a12-4895-b44d-db8b2b10def2\" name=\"Sales\" source=\"2\" >\n</folderElement>\n<folderElement id=\"fe_906f133a-b8df-4e9e-ae87-c2fd22dfbc56\" name=\"Store\" source=\"3\" >\n</folderElement>\n<folderElement id=\"fe_b89f3336-a1f5-40e9-93c5-7ef8d0a5bf0f\" name=\"Warehouse\" source=\"4\" >\n</folderElement>\n</staticFolder>\n</folder>\r\n');

-- ----------------------------
-- Table structure for `roles`
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(80) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `RIGHTS` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('1', 'ADMIN', 'Grants the right to view & edit administration area and modify & share all existing views (System)', 'G');
INSERT INTO `roles` VALUES ('2', 'EDITOR', 'Grants the right to modify views shared by other users (System)', 'C');
INSERT INTO `roles` VALUES ('3', 'VIEWER', 'Grants the right to see views shared by other users (System)', 'R');
INSERT INTO `roles` VALUES ('4', 'OWNER', 'Grants the right to create views and edit these views (System)', 'C');
INSERT INTO `roles` VALUES ('5', 'POWERUSER', 'Grants the right to share views created by this user', 'G');
INSERT INTO `roles` VALUES ('6', 'CREATOR', 'Grants the right to create and modify own views', 'C');
INSERT INTO `roles` VALUES ('7', 'PUBLISHER', 'Grants the right to create, modify and publish own views', 'G');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FIRSTNAME` varchar(80) DEFAULT NULL,
  `LASTNAME` varchar(80) DEFAULT NULL,
  `LOGIN` varchar(80) DEFAULT NULL,
  `PASSWORD` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `LOGIN` (`LOGIN`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'admin', null, 'admin', 'ISMvKXpXpadDiUoOSoAfww==');
INSERT INTO `users` VALUES ('2', null, null, 'editor', 'Wu6dvSoYiDkQUHNXG+4bHw==');
INSERT INTO `users` VALUES ('3', null, null, 'viewer', 'SyoVKYZ7jWl2hbFyLM0BSQ==');
INSERT INTO `users` VALUES ('4', null, null, 'poweruser', 'ZlVBELxjqNAltzj5bkWXxA==');
INSERT INTO `users` VALUES ('5', null, null, 'creator', '7iQzJZsP45m0DoHSyYo4tg==');
INSERT INTO `users` VALUES ('6', null, null, 'publisher', 'Uq3tFlNgNSoPWFdXHZbWjw==');
INSERT INTO `users` VALUES ('7', null, null, 'direct-link', 'czSAp17pC66iOFLOqePEmw==');

-- ----------------------------
-- Table structure for `users_groups_association`
-- ----------------------------
DROP TABLE IF EXISTS `users_groups_association`;
CREATE TABLE `users_groups_association` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) unsigned NOT NULL,
  `GROUP_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  KEY `GROUP_ID` (`GROUP_ID`),
  CONSTRAINT `users_groups_association_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`ID`),
  CONSTRAINT `users_groups_association_ibfk_2` FOREIGN KEY (`GROUP_ID`) REFERENCES `groups` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users_groups_association
-- ----------------------------

-- ----------------------------
-- Table structure for `users_roles_association`
-- ----------------------------
DROP TABLE IF EXISTS `users_roles_association`;
CREATE TABLE `users_roles_association` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) unsigned NOT NULL,
  `ROLE_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `users_roles_association_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`ID`),
  CONSTRAINT `users_roles_association_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `roles` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users_roles_association
-- ----------------------------
INSERT INTO `users_roles_association` VALUES ('1', '1', '1');
INSERT INTO `users_roles_association` VALUES ('2', '1', '2');
INSERT INTO `users_roles_association` VALUES ('3', '1', '3');
INSERT INTO `users_roles_association` VALUES ('4', '4', '5');
INSERT INTO `users_roles_association` VALUES ('5', '4', '2');
INSERT INTO `users_roles_association` VALUES ('6', '4', '3');
INSERT INTO `users_roles_association` VALUES ('7', '2', '2');
INSERT INTO `users_roles_association` VALUES ('8', '2', '3');
INSERT INTO `users_roles_association` VALUES ('9', '5', '6');
INSERT INTO `users_roles_association` VALUES ('10', '5', '3');
INSERT INTO `users_roles_association` VALUES ('11', '6', '7');
INSERT INTO `users_roles_association` VALUES ('12', '6', '3');
INSERT INTO `users_roles_association` VALUES ('13', '3', '3');
INSERT INTO `users_roles_association` VALUES ('14', '7', '3');

-- ----------------------------
-- Table structure for `views`
-- ----------------------------
DROP TABLE IF EXISTS `views`;
CREATE TABLE `views` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `NAME` varchar(80) DEFAULT NULL,
  `OWNER` int(10) unsigned NOT NULL,
  `DEFINITION` text,
  `DATABASE_ID` varchar(255) NOT NULL,
  `CUBE_ID` varchar(255) NOT NULL,
  `ACCOUNT_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `OWNER` (`OWNER`),
  KEY `ACCOUNT_ID` (`ACCOUNT_ID`),
  CONSTRAINT `views_ibfk_1` FOREIGN KEY (`OWNER`) REFERENCES `users` (`ID`),
  CONSTRAINT `views_ibfk_2` FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `accounts` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of views
-- ----------------------------
INSERT INTO `views` VALUES ('1', 'HR', '1', '<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<?palocubeview version=\"0.1\"?>\r\n<view id=\"1\" name=\"HR\" cube=\"HR\">\r\n<property id=\"hideEmpty\" value=\"true\">\r\n</property><axis id=\"cols\" name=\"column\" >\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Measures))\" hierarchy_id=\"HR|.#.|((Measures))\" >\r\n<selected_elements>((Measures)).((Org Salary))</selected_elements>\r\n</axis_hierarchy>\r\n</axis>\r\n<axis id=\"rows\" name=\"row\" >\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Department))\" hierarchy_id=\"HR|.#.|((Department))\" >\r\n<selected_elements>((Department)).((All Departments))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Position))\" hierarchy_id=\"HR|.#.|((Position))\" >\r\n<selected_elements>((Position)).((All Position))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Pay Type))\" hierarchy_id=\"HR|.#.|((Pay Type))\" >\r\n<selected_elements>((Pay Type)).((All Pay Types))</selected_elements>\r\n</axis_hierarchy>\r\n<expanded_paths>((Department)).((All Departments))</expanded_paths>\r\n<expanded_paths>((Department)).((All Departments)),((Department)).((3)):((Position)).((All Position))</expanded_paths>\r\n<expanded_paths>((Department)).((All Departments)),((Department)).((3)):((Position)).((All Position)),((Position)).((Middle Management))</expanded_paths>\r\n<expanded_paths>((Department)).((All Departments)),((Department)).((3)):((Position)).((All Position)),((Position)).((Senior Management)):((Pay Type)).((All Pay Types))</expanded_paths>\r\n</axis>\r\n<axis id=\"selected\" name=\"selection\" >\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Employees))\" hierarchy_id=\"HR|.#.|((Employees))\" >\r\n<selected_elements>((Employees)).((All Employees))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Store))\" hierarchy_id=\"HR|.#.|((Store))\" >\r\n<selected_elements>((Store)).((All Stores))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Store Type))\" hierarchy_id=\"HR|.#.|((Store Type))\" >\r\n<selected_elements>((Store Type)).((All Store Types))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"HR|.#.|((Time))\" hierarchy_id=\"HR|.#.|((Time))\" >\r\n<selected_elements>((Time)).((1997))</selected_elements>\r\n</axis_hierarchy>\r\n</axis>\r\n<axis id=\"hierarchy-repository\" name=\"hierarchy-repository\" >\r\n<property id=\"com.tensegrity.palo.cubview.axis.preferredwidth\" value=\"0\" />\r\n</axis>\r\n</view>\r\n', 'FoodMart', 'HR', '8');
INSERT INTO `views` VALUES ('2', 'Sales', '1', '<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<?palocubeview version=\"0.1\"?>\r\n<view id=\"2\" name=\"Sales\" cube=\"Sales\">\r\n<axis id=\"cols\" name=\"column\" >\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Measures))\" hierarchy_id=\"Sales|.#.|((Measures))\" >\r\n<selected_elements>((Measures)).((Unit Sales))</selected_elements>\r\n</axis_hierarchy>\r\n</axis>\r\n<axis id=\"rows\" name=\"row\" >\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Customers))\" hierarchy_id=\"Sales|.#.|((Customers))\" >\r\n<selected_elements>((Customers)).((All Customers))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Gender))\" hierarchy_id=\"Sales|.#.|((Gender))\" >\r\n<selected_elements>((Gender)).((All Gender))</selected_elements>\r\n</axis_hierarchy>\r\n<expanded_paths>((Customers)).((All Customers))</expanded_paths>\r\n<expanded_paths>((Customers)).((All Customers)):((Gender)).((All Gender))</expanded_paths>\r\n</axis>\r\n<axis id=\"selected\" name=\"selection\" >\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Education Level))\" hierarchy_id=\"Sales|.#.|((Education Level))\" >\r\n<selected_elements>((Education Level)).((All Education Levels))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Marital Status))\" hierarchy_id=\"Sales|.#.|((Marital Status))\" >\r\n<selected_elements>((Marital Status)).((All Marital Status))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Product))\" hierarchy_id=\"Sales|.#.|((Product))\" >\r\n<selected_elements>((Product)).((All Products))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Promotion Media))\" hierarchy_id=\"Sales|.#.|((Promotion Media))\" >\r\n<selected_elements>((Promotion Media)).((All Media))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Promotions))\" hierarchy_id=\"Sales|.#.|((Promotions))\" >\r\n<selected_elements>((Promotions)).((All Promotions))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Store))\" hierarchy_id=\"Sales|.#.|((Store))\" >\r\n<selected_elements>((Store)).((All Stores))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Store Size in SQFT))\" hierarchy_id=\"Sales|.#.|((Store Size in SQFT))\" >\r\n<selected_elements>((Store Size in SQFT)).((All Store Size in SQFTs))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Store Type))\" hierarchy_id=\"Sales|.#.|((Store Type))\" >\r\n<selected_elements>((Store Type)).((All Store Types))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Time))\" hierarchy_id=\"Sales|.#.|((Time))\" >\r\n<selected_elements>((Time)).((1997))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Sales|.#.|((Yearly Income))\" hierarchy_id=\"Sales|.#.|((Yearly Income))\" >\r\n<selected_elements>((Yearly Income)).((All Yearly Incomes))</selected_elements>\r\n</axis_hierarchy>\r\n</axis>\r\n<axis id=\"hierarchy-repository\" name=\"hierarchy-repository\" >\r\n<property id=\"com.tensegrity.palo.cubview.axis.preferredwidth\" value=\"0\" />\r\n</axis>\r\n</view>\r\n', 'FoodMart', 'Sales', '8');
INSERT INTO `views` VALUES ('3', 'Store', '1', '<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<?palocubeview version=\"0.1\"?>\r\n<view id=\"3\" name=\"Store\" cube=\"Store\">\r\n<axis id=\"cols\" name=\"column\" >\r\n</axis>\r\n<axis id=\"rows\" name=\"row\" >\r\n</axis>\r\n<axis id=\"selected\" name=\"selection\" >\r\n<axis_hierarchy dimension_id=\"Store|.#.|((Has coffee bar))\" hierarchy_id=\"Store|.#.|((Has coffee bar))\" >\r\n<selected_elements>((Has coffee bar)).((All Has coffee bars))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Store|.#.|((Measures))\" hierarchy_id=\"Store|.#.|((Measures))\" >\r\n<selected_elements>((Measures)).((Store Sqft))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Store|.#.|((Store))\" hierarchy_id=\"Store|.#.|((Store))\" >\r\n<selected_elements>((Store)).((All Stores))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Store|.#.|((Store Type))\" hierarchy_id=\"Store|.#.|((Store Type))\" >\r\n<selected_elements>((Store Type)).((All Store Types))</selected_elements>\r\n</axis_hierarchy>\r\n</axis>\r\n</view>\r\n', 'FoodMart', 'Store', '8');
INSERT INTO `views` VALUES ('4', 'Warehouse', '1', '<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<?palocubeview version=\"0.1\"?>\r\n<view id=\"4\" name=\"Warehouse\" cube=\"Warehouse\">\r\n<axis id=\"cols\" name=\"column\" >\r\n</axis>\r\n<axis id=\"rows\" name=\"row\" >\r\n</axis>\r\n<axis id=\"selected\" name=\"selection\" >\r\n<axis_hierarchy dimension_id=\"Warehouse|.#.|((Measures))\" hierarchy_id=\"Warehouse|.#.|((Measures))\" >\r\n<selected_elements>((Measures)).((Store Invoice))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Warehouse|.#.|((Product))\" hierarchy_id=\"Warehouse|.#.|((Product))\" >\r\n<selected_elements>((Product)).((All Products))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Warehouse|.#.|((Store))\" hierarchy_id=\"Warehouse|.#.|((Store))\" >\r\n<selected_elements>((Store)).((All Stores))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Warehouse|.#.|((Store Size in SQFT))\" hierarchy_id=\"Warehouse|.#.|((Store Size in SQFT))\" >\r\n<selected_elements>((Store Size in SQFT)).((All Store Size in SQFTs))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Warehouse|.#.|((Store Type))\" hierarchy_id=\"Warehouse|.#.|((Store Type))\" >\r\n<selected_elements>((Store Type)).((All Store Types))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Warehouse|.#.|((Time))\" hierarchy_id=\"Warehouse|.#.|((Time))\" >\r\n<selected_elements>((Time)).((1997))</selected_elements>\r\n</axis_hierarchy>\r\n<axis_hierarchy dimension_id=\"Warehouse|.#.|((Warehouse))\" hierarchy_id=\"Warehouse|.#.|((Warehouse))\" >\r\n<selected_elements>((Warehouse)).((All Warehouses))</selected_elements>\r\n</axis_hierarchy>\r\n</axis>\r\n</view>\r\n', 'FoodMart', 'Warehouse', '8');

-- ----------------------------
-- Table structure for `views_roles_association`
-- ----------------------------
DROP TABLE IF EXISTS `views_roles_association`;
CREATE TABLE `views_roles_association` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `VIEW_ID` int(10) unsigned NOT NULL,
  `ROLE_ID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `VIEW_ID` (`VIEW_ID`),
  KEY `ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `views_roles_association_ibfk_1` FOREIGN KEY (`VIEW_ID`) REFERENCES `views` (`ID`),
  CONSTRAINT `views_roles_association_ibfk_2` FOREIGN KEY (`ROLE_ID`) REFERENCES `roles` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of views_roles_association
-- ----------------------------
