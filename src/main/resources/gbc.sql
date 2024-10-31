/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.30-log : Database - gbc
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gbc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `gbc`;

/*Table structure for table `like_song` */

DROP TABLE IF EXISTS `like_song`;

CREATE TABLE `like_song` (
  `sid` int(11) NOT NULL,
  `DATE` date NOT NULL,
  `uid` int(11) NOT NULL,
  PRIMARY KEY (`sid`,`uid`),
  UNIQUE KEY `sid` (`sid`,`uid`),
  KEY `uid` (`uid`),
  CONSTRAINT `like_song_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `song` (`sid`),
  CONSTRAINT `like_song_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `like_song` */

insert  into `like_song`(`sid`,`DATE`,`uid`) values (1,'2024-10-31',9),(1,'2024-10-15',13),(3,'2024-10-15',13),(4,'2024-10-31',9),(4,'2024-10-17',13),(5,'2024-10-31',9),(5,'2024-10-17',13),(6,'2024-10-17',13),(7,'2024-10-31',9),(7,'2024-10-17',13),(8,'2024-10-31',9),(8,'2024-10-17',13),(9,'2024-10-17',13),(10,'2024-10-30',9),(10,'2024-10-17',13),(45,'2024-10-15',13),(46,'2024-10-15',13),(47,'2024-10-17',9);

/*Table structure for table `song` */

DROP TABLE IF EXISTS `song`;

CREATE TABLE `song` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `filepath` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `artist` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `album` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `duration` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cover` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `download` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `song` */

insert  into `song`(`sid`,`filepath`,`title`,`artist`,`album`,`duration`,`cover`,`download`) values (1,'songs/01.mp3','極私的極彩色アンサー （极具自我色彩的答复）','トゲナシトゲアリ','棘アリ','02:35','img/songs/01.webp','https://wwsr.lanzouv.com/iZXbS2bulrcj'),(2,'songs/02.mp3','サヨナラサヨナラサヨナラ （再见 再见 再见）','トゲナシトゲアリ','棘アリ','02:35','img/songs/02.webp','https://wwsr.lanzouv.com/iKo852bum0ef'),(3,'songs/03.mp3','極私的極彩色アンサー （极具自我色彩的答复）','トゲナシトゲアリ','極私的極彩色アンサー','02:35','img/songs/03.webp','https://wwsr.lanzouv.com/iZXbS2bulrcj'),(4,'songs/04.mp3','サヨナラサヨナラサヨナラ （再见 再见 再见）','トゲナシトゲアリ','サヨナラサヨナラサヨナラ','02:35','img/songs/04.webp','https://wwsr.lanzouv.com/iKo852bum0ef'),(5,'songs/05.mp3','闇に溶けてく （溶于黑暗）','トゲナシトゲアリ','棘ナシ','02:41','img/songs/05.webp',''),(6,'songs/06.mp3','心象的フラクタル (beni-shouga) (心象分形)','トゲナシトゲアリ','棘ナシ','02:58','img/songs/06.webp',''),(7,'songs/07.mp3','空の箱 (井芹仁菜、河原木桃香) （空之箱）','トゲナシトゲアリ','空の箱 (井芹仁菜、河原木桃香)','03:04','img/songs/07.webp','https://wwsr.lanzouv.com/ioGZX2bvdg1a'),(8,'songs/08.mp3','心象的フラクタル (beni-shouga) (心象分形)','トゲナシトゲアリ','心象的フラクタル (beni-shouga)','02:58','img/songs/08.webp',''),(9,'songs/09.mp3','空の箱 (井芹仁菜、河原木桃香) （空之箱）','トゲナシトゲアリ','棘ナシ','03:03','img/songs/09.webp','https://wwsr.lanzouv.com/ioGZX2bvdg1a'),(10,'songs/10.mp3','雑踏、僕らの街 (熙熙攘攘、我们的城市)','トゲナシトゲアリ','雑踏、僕らの街','03:04','img/songs/10.webp','https://wwsr.lanzouv.com/ip8l42bvddyf'),(11,'songs/11.mp3','空白とカタルシス (空白与宣泄)','トゲナシトゲアリ','棘ナシ','03:10','img/songs/11.webp',''),(12,'songs/12.mp3','声なき魚 (新川崎（仮）) (无声之鱼)','トゲナシトゲアリ','棘ナシ','03:00','img/songs/12.webp',''),(13,'songs/13.mp3','声なき魚 (新川崎(仮)) (无声之鱼)','トゲナシトゲアリ','声なき魚 (新川崎（仮）)','03:00','img/songs/13.webp',''),(14,'songs/14.mp3','視界の隅 朽ちる音 (新川崎（仮）) (视野一隅 腐烂之声)','トゲナシトゲアリ','棘ナシ','03:08','img/songs/14.webp',''),(15,'songs/15.mp3','名もなき何もかも (皆无其名)','トゲナシトゲアリ','名もなき何もかも','03:08','img/songs/15.webp','https://wwsr.lanzouv.com/ijIvS2bul4ji'),(16,'songs/16.mp3','誰にもなれない私だから (正因为是无法成为他人的我)','トゲナシトゲアリ','誰にもなれない私だから','03:09','img/songs/16.webp','https://wwsr.lanzouv.com/i8Wg22bveacb'),(17,'songs/17.mp3','黎明を穿つ (破晓黎明)','トゲナシトゲアリ','棘アリ','03:09','img/songs/17.webp','https://wwsr.lanzouv.com/igOYM2bulo5e'),(18,'songs/18.mp3','黎明を穿つ (破晓黎明)','トゲナシトゲアリ','黎明を穿つ','03:09','img/songs/18.webp','https://wwsr.lanzouv.com/igOYM2bulo5e'),(19,'songs/19.mp3','偽りの理 (虚伪之理)','トゲナシトゲアリ','偽りの理','03:09','img/songs/19.webp','https://wwsr.lanzouv.com/iVQtL2bul83g'),(20,'songs/20.mp3','誰にもなれない私だから (正因为是无法成为他人的我)','トゲナシトゲアリ','棘ナシ','03:10','img/songs/20.webp','https://wwsr.lanzouv.com/i8Wg22bveacb'),(21,'songs/21.mp3','偽りの理 (虚伪之理)','トゲナシトゲアリ','棘アリ','03:11','img/songs/21.webp','https://wwsr.lanzouv.com/iVQtL2bul83g'),(22,'songs/22.mp3','ETERNAL FLAME (VOID) (空之箱)','ダイヤモンドダスト','棘ナシ','03:24','img/songs/22.webp',''),(23,'songs/23.mp3','空白とカタルシス (空白与宣泄)','トゲナシトゲアリ','空白とカタルシス','03:10','img/songs/23.webp',''),(24,'songs/24.mp3','気鬱、白濁す (忧郁、白浊)','トゲナシトゲアリ','棘アリ','03:05','img/songs/24.webp','https://wwsr.lanzouv.com/iSV0u2bulawh'),(25,'songs/25.mp3','視界の隅 朽ちる音 (新川崎（仮）) (视野一隅 腐烂之声)','トゲナシトゲアリ','視界の隅 朽ちる音 (新川崎（仮）)','03:08','img/songs/25.webp',''),(26,'songs/26.mp3','雑踏、僕らの街 (熙熙攘攘、我们的城市)','トゲナシトゲアリ','棘ナシ','03:04','img/songs/26.webp','https://wwsr.lanzouv.com/ip8l42bvddyf'),(27,'songs/27.mp3','気鬱、白濁す (忧郁、白浊)','トゲナシトゲアリ','気鬱、白濁す','03:05','img/songs/27.webp','https://wwsr.lanzouv.com/iSV0u2bulawh'),(28,'songs/28.mp3','Cycle Of Sorrow (悲伤循环)','ダイヤモンドダスト','Cycle Of Sorrow','03:18','img/songs/28.webp',''),(29,'songs/29.mp3','名もなき何もかも (皆无其名)','トゲナシトゲアリ','棘アリ','03:09','img/songs/29.webp','https://wwsr.lanzouv.com/ijIvS2bul4ji'),(30,'songs/30.mp3','運命に賭けたい論理 (赌上命运的理论)','トゲナシトゲアリ','運命に賭けたい論理','03:15','img/songs/30.webp','https://wwsr.lanzouv.com/i2M1c2buly3c'),(31,'songs/31.mp3','運命の華 (命运之花)','トゲナシトゲアリ','運命の華','03:15','img/songs/31.webp',''),(32,'songs/32.mp3','傷つき傷つけ痛くて辛い (受伤 伤害 疼痛 痛苦)','トゲナシトゲアリ','傷つき傷つけ痛くて辛い','03:16','img/songs/32.webp',''),(33,'songs/33.mp3','Cycle Of Sorrow (悲伤循环)','ダイヤモンドダスト','棘ナシ','03:18','img/songs/33.webp',''),(34,'songs/34.mp3','傷つき傷つけ痛くて辛い (受伤 伤害 疼痛 痛苦)','トゲナシトゲアリ','棘アリ','03:16','img/songs/34.webp',''),(35,'songs/35.mp3','運命の華 (命运之花)','トゲナシトゲアリ','棘ナシ','03:15','img/songs/35.webp',''),(36,'songs/36.mp3','運命に賭けたい論理 (赌上命运的理论)','トゲナシトゲアリ','棘アリ','03:16','img/songs/36.webp','https://wwsr.lanzouv.com/i2M1c2buly3c'),(37,'songs/37.mp3','ETERNAL FLAME ~空の箱 (空之箱)','ダイヤモンドダスト','ETERNAL FLAME ~空の箱','03:24','img/songs/37.webp',''),(38,'songs/38.mp3','蝶に結いた赤い糸 (系在蝴蝶上的红线)','トゲナシトゲアリ','棘ナシ','03:29','img/songs/38.webp',''),(39,'songs/39.mp3','爆ぜて咲く （爆裂绽放）','トゲナシトゲアリ','爆ぜて咲く','03:41','img/songs/39.webp','https://wwsr.lanzouv.com/iulGC2bulk8d'),(40,'songs/40.mp3','爆ぜて咲く （爆裂绽放）','トゲナシトゲアリ','棘アリ','03:43','img/songs/40.webp','https://wwsr.lanzouv.com/iulGC2bulk8d'),(41,'songs/41.mp3','理想的パラドクスとは (所谓理想悖论)','トゲナシトゲアリ','棘アリ','03:48','img/songs/41.webp','https://wwsr.lanzouv.com/i4afd2bulffa'),(42,'songs/42.mp3','理想的パラドクスとは (所谓理想悖论)','トゲナシトゲアリ','理想的パラドクスとは','03:48','img/songs/42.webp','https://wwsr.lanzouv.com/i4afd2bulffa'),(43,'songs/43.mp3','碧いif（如果蔚蓝）','トゲナシトゲアリ','碧いif','02:42','img/songs/43.webp',NULL),(44,'songs/44.mp3','渇く、憂う（渴望、忧郁）','トゲナシトゲアリ','渇く、憂う','02:41','img/songs/44.webp',NULL),(45,'songs/45.mp3','吹き消した灯火（吹灭的灯火）','トゲナシトゲアリ','吹き消した灯火','03:55','img/songs/45.webp',NULL),(46,'songs/46.mp3','無知のち私（知晓无知的我）','トゲナシトゲアリ','無知のち私','03:03','img/songs/46.webp',NULL),(47,'songs/47.mp3','さよならあの日のメロディ (再见那天的旋律)','ミネ','さよならあの日のメロディ','04:30','img/songs/47.webp',NULL),(48,'songs/48.mp3','生きて生きてゆく (活着活下去)','トゲナシトゲアリ','生きて生きてゆく','03:24','img/songs/48.webp',NULL);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `STATUS` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remember_me_token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `AK_nq_code` (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user` */

insert  into `user`(`uid`,`email`,`PASSWORD`,`STATUS`,`CODE`,`remember_me_token`) values (9,'3237289802@qq.com','11111111','Y','f0574bddf4154fc7856af065d4c97365',NULL),(12,'2500216660@qq.com','xx529628','N','322d53431dc14bcf8549d2262427de41',NULL),(13,'teng3237@163.com','12345678','Y','532d18094e184443826e359b5cf717fc',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
