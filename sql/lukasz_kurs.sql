-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: lukasz
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `kurs`
--

DROP TABLE IF EXISTS `kurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kurs` (
  `id` int(11) NOT NULL,
  `id_linia` int(11) NOT NULL,
  `id_przystanek_poczatkowy` int(11) NOT NULL,
  `id_przystanek_koncowy` int(11) NOT NULL,
  `id_dni_tygodnia` int(11) NOT NULL,
  `uwagi` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `linia_fk_idx` (`id_linia`),
  KEY `przystanek_poczatkowy_fk_idx` (`id_przystanek_poczatkowy`),
  KEY `przystanek_koncowy_fk_idx` (`id_przystanek_koncowy`),
  KEY `dni_tygodnia_fk_idx` (`id_dni_tygodnia`),
  CONSTRAINT `dni_tygodnia_fk` FOREIGN KEY (`id_dni_tygodnia`) REFERENCES `dni_tygodnia` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `linia_fk` FOREIGN KEY (`id_linia`) REFERENCES `linia` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `przystanek_koncowy_fk` FOREIGN KEY (`id_przystanek_koncowy`) REFERENCES `przystanek` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `przystanek_poczatkowy_fk` FOREIGN KEY (`id_przystanek_poczatkowy`) REFERENCES `przystanek` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Opisuje kurs - laczy: \nlinie, przystanek poczatkowy, przystanek poczatkowy oraz okresla w jakie dni kurs sie odbywa';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kurs`
--

LOCK TABLES `kurs` WRITE;
/*!40000 ALTER TABLE `kurs` DISABLE KEYS */;
/*!40000 ALTER TABLE `kurs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-12 12:10:00
