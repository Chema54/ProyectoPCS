/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.14-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 100.119.92.93    Database: practicas_profesionales
-- ------------------------------------------------------
-- Server version	11.8.6-MariaDB-0+deb13u1 from Debian

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Asignacion`
--

DROP TABLE IF EXISTS `Asignacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Asignacion` (
  `id_asignacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_practicante` int(11) DEFAULT NULL,
  `id_proyecto` int(11) DEFAULT NULL,
  `id_experiencia` int(11) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'Activa',
  PRIMARY KEY (`id_asignacion`),
  KEY `id_proyecto` (`id_proyecto`),
  KEY `id_experiencia` (`id_experiencia`),
  KEY `Asignacion_ibfk_1` (`id_practicante`),
  CONSTRAINT `Asignacion_ibfk_1` FOREIGN KEY (`id_practicante`) REFERENCES `Practicante` (`id_practicante`),
  CONSTRAINT `Asignacion_ibfk_2` FOREIGN KEY (`id_proyecto`) REFERENCES `Proyecto` (`id_proyecto`),
  CONSTRAINT `Asignacion_ibfk_3` FOREIGN KEY (`id_experiencia`) REFERENCES `ExperienciaEducativa` (`id_experiencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Asignacion`
--

LOCK TABLES `Asignacion` WRITE;
/*!40000 ALTER TABLE `Asignacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `Asignacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Autoevaluacion`
--

DROP TABLE IF EXISTS `Autoevaluacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Autoevaluacion` (
  `id_autoevaluacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_asignacion` int(11) NOT NULL,
  `nombre_entregable` varchar(100) DEFAULT NULL,
  `archivo` longblob DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'Inhabilitado',
  `fecha_limite` date DEFAULT NULL,
  `calificacion` decimal(5,2) DEFAULT NULL,
  `comentarios` text DEFAULT NULL,
  PRIMARY KEY (`id_autoevaluacion`),
  KEY `id_asignacion` (`id_asignacion`),
  CONSTRAINT `Autoevaluacion_ibfk_1` FOREIGN KEY (`id_asignacion`) REFERENCES `Asignacion` (`id_asignacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Autoevaluacion`
--

LOCK TABLES `Autoevaluacion` WRITE;
/*!40000 ALTER TABLE `Autoevaluacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `Autoevaluacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Coordinador`
--

DROP TABLE IF EXISTS `Coordinador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Coordinador` (
  `id_coordinador` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `numeroPersonal` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido_paterno` varchar(100) NOT NULL,
  `apellido_materno` varchar(100) NOT NULL,
  `correo` varchar(150) NOT NULL,
  PRIMARY KEY (`id_coordinador`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `Coordinador_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Coordinador`
--

LOCK TABLES `Coordinador` WRITE;
/*!40000 ALTER TABLE `Coordinador` DISABLE KEYS */;
INSERT INTO `Coordinador` VALUES
(1,1,'000001','Lenin Jesus Hernandez Ramirez','','','');
/*!40000 ALTER TABLE `Coordinador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Documento_Aceptacion`
--

DROP TABLE IF EXISTS `Documento_Aceptacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Documento_Aceptacion` (
  `id_doc_aceptacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_asignacion` int(11) NOT NULL,
  `nombre_entregable` varchar(100) DEFAULT NULL,
  `archivo` longblob DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'Inhabilitado',
  `fecha_limite` date DEFAULT NULL,
  PRIMARY KEY (`id_doc_aceptacion`),
  KEY `id_asignacion` (`id_asignacion`),
  CONSTRAINT `Documento_Aceptacion_ibfk_1` FOREIGN KEY (`id_asignacion`) REFERENCES `Asignacion` (`id_asignacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Documento_Aceptacion`
--

LOCK TABLES `Documento_Aceptacion` WRITE;
/*!40000 ALTER TABLE `Documento_Aceptacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `Documento_Aceptacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Evaluacion_OV`
--

DROP TABLE IF EXISTS `Evaluacion_OV`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Evaluacion_OV` (
  `id_evaluacion_ov` int(11) NOT NULL AUTO_INCREMENT,
  `id_asignacion` int(11) NOT NULL,
  `nombre_entregable` varchar(100) DEFAULT NULL,
  `archivo` longblob DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'Inhabilitado',
  `fecha_limite` date DEFAULT NULL,
  `calificacion` decimal(5,2) DEFAULT NULL,
  `comentarios` text DEFAULT NULL,
  PRIMARY KEY (`id_evaluacion_ov`),
  KEY `id_asignacion` (`id_asignacion`),
  CONSTRAINT `Evaluacion_OV_ibfk_1` FOREIGN KEY (`id_asignacion`) REFERENCES `Asignacion` (`id_asignacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Evaluacion_OV`
--

LOCK TABLES `Evaluacion_OV` WRITE;
/*!40000 ALTER TABLE `Evaluacion_OV` DISABLE KEYS */;
/*!40000 ALTER TABLE `Evaluacion_OV` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ExperienciaEducativa`
--

DROP TABLE IF EXISTS `ExperienciaEducativa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ExperienciaEducativa` (
  `id_experiencia` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `id_periodo` int(11) DEFAULT NULL,
  `nrc` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id_experiencia`),
  UNIQUE KEY `nrc` (`nrc`),
  KEY `id_periodo` (`id_periodo`),
  CONSTRAINT `ExperienciaEducativa_ibfk_1` FOREIGN KEY (`id_periodo`) REFERENCES `Periodo` (`id_periodo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ExperienciaEducativa`
--

LOCK TABLES `ExperienciaEducativa` WRITE;
/*!40000 ALTER TABLE `ExperienciaEducativa` DISABLE KEYS */;
/*!40000 ALTER TABLE `ExperienciaEducativa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrganizacionVinculada`
--

DROP TABLE IF EXISTS `OrganizacionVinculada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `OrganizacionVinculada` (
  `id_organizacion` int(11) NOT NULL AUTO_INCREMENT,
  `razon_social` varchar(150) NOT NULL,
  `ubicacion` varchar(255) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo` varchar(150) NOT NULL,
  PRIMARY KEY (`id_organizacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrganizacionVinculada`
--

LOCK TABLES `OrganizacionVinculada` WRITE;
/*!40000 ALTER TABLE `OrganizacionVinculada` DISABLE KEYS */;
/*!40000 ALTER TABLE `OrganizacionVinculada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Periodo`
--

DROP TABLE IF EXISTS `Periodo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Periodo` (
  `id_periodo` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'Cerrado',
  PRIMARY KEY (`id_periodo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Periodo`
--

LOCK TABLES `Periodo` WRITE;
/*!40000 ALTER TABLE `Periodo` DISABLE KEYS */;
/*!40000 ALTER TABLE `Periodo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Practicante`
--

DROP TABLE IF EXISTS `Practicante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Practicante` (
  `id_practicante` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido_paterno` varchar(100) NOT NULL,
  `apellido_materno` varchar(100) NOT NULL,
  `correo` varchar(150) NOT NULL,
  `matricula` varchar(50) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'Activo',
  `id_usuario` int(11) NOT NULL,
  PRIMARY KEY (`id_practicante`),
  KEY `fk_practicante_usuario` (`id_usuario`),
  CONSTRAINT `fk_practicante_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Practicante`
--

LOCK TABLES `Practicante` WRITE;
/*!40000 ALTER TABLE `Practicante` DISABLE KEYS */;
/*!40000 ALTER TABLE `Practicante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PracticanteExperiencia`
--

DROP TABLE IF EXISTS `PracticanteExperiencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `PracticanteExperiencia` (
  `id_practicante` int(11) NOT NULL,
  `id_experiencia` int(11) NOT NULL,
  PRIMARY KEY (`id_practicante`,`id_experiencia`),
  KEY `id_experiencia` (`id_experiencia`),
  CONSTRAINT `PracticanteExperiencia_ibfk_1` FOREIGN KEY (`id_practicante`) REFERENCES `Practicante` (`id_practicante`),
  CONSTRAINT `PracticanteExperiencia_ibfk_2` FOREIGN KEY (`id_experiencia`) REFERENCES `ExperienciaEducativa` (`id_experiencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PracticanteExperiencia`
--

LOCK TABLES `PracticanteExperiencia` WRITE;
/*!40000 ALTER TABLE `PracticanteExperiencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `PracticanteExperiencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Profesor`
--

DROP TABLE IF EXISTS `Profesor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Profesor` (
  `id_profesor` int(11) NOT NULL AUTO_INCREMENT,
  `numero_personal` varchar(50) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido_paterno` varchar(100) NOT NULL,
  `apellido_materno` varchar(100) NOT NULL,
  `correo` varchar(150) NOT NULL,
  `estado` varchar(20) DEFAULT 'Activo',
  `id_usuario` int(11) NOT NULL,
  PRIMARY KEY (`id_profesor`),
  UNIQUE KEY `numero_personal` (`numero_personal`),
  KEY `fk_profesor_usuario` (`id_usuario`),
  CONSTRAINT `fk_profesor_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Profesor`
--

LOCK TABLES `Profesor` WRITE;
/*!40000 ALTER TABLE `Profesor` DISABLE KEYS */;
/*!40000 ALTER TABLE `Profesor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProfesorExperiencia`
--

DROP TABLE IF EXISTS `ProfesorExperiencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProfesorExperiencia` (
  `id_profesor` int(11) NOT NULL,
  `id_experiencia` int(11) NOT NULL,
  PRIMARY KEY (`id_profesor`,`id_experiencia`),
  KEY `id_experiencia` (`id_experiencia`),
  CONSTRAINT `ProfesorExperiencia_ibfk_1` FOREIGN KEY (`id_profesor`) REFERENCES `Profesor` (`id_profesor`),
  CONSTRAINT `ProfesorExperiencia_ibfk_2` FOREIGN KEY (`id_experiencia`) REFERENCES `ExperienciaEducativa` (`id_experiencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProfesorExperiencia`
--

LOCK TABLES `ProfesorExperiencia` WRITE;
/*!40000 ALTER TABLE `ProfesorExperiencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `ProfesorExperiencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Proyecto`
--

DROP TABLE IF EXISTS `Proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Proyecto` (
  `id_proyecto` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) DEFAULT NULL,
  `id_titular` int(11) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'Sin asignar',
  `cupo_total` int(11) NOT NULL DEFAULT 0,
  `espacios_disponibles` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_proyecto`),
  KEY `id_titular` (`id_titular`),
  CONSTRAINT `Proyecto_ibfk_1` FOREIGN KEY (`id_titular`) REFERENCES `TitularProyecto` (`id_titular`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Proyecto`
--

LOCK TABLES `Proyecto` WRITE;
/*!40000 ALTER TABLE `Proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `Proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Reporte`
--

DROP TABLE IF EXISTS `Reporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Reporte` (
  `id_reporte` int(11) NOT NULL AUTO_INCREMENT,
  `id_asignacion` int(11) NOT NULL,
  `nombre_entregable` varchar(100) DEFAULT NULL,
  `archivo` longblob DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'Inhabilitado',
  `fecha_limite` date DEFAULT NULL,
  `horas_reportadas` int(11) DEFAULT 0,
  `calificacion` decimal(5,2) DEFAULT NULL,
  `comentarios` text DEFAULT NULL,
  PRIMARY KEY (`id_reporte`),
  KEY `id_asignacion` (`id_asignacion`),
  CONSTRAINT `Reporte_ibfk_1` FOREIGN KEY (`id_asignacion`) REFERENCES `Asignacion` (`id_asignacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reporte`
--

LOCK TABLES `Reporte` WRITE;
/*!40000 ALTER TABLE `Reporte` DISABLE KEYS */;
/*!40000 ALTER TABLE `Reporte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rol`
--

DROP TABLE IF EXISTS `Rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Rol` (
  `id_rol` int(11) NOT NULL AUTO_INCREMENT,
  `nombreRol` varchar(50) NOT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombreRol` (`nombreRol`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rol`
--

LOCK TABLES `Rol` WRITE;
/*!40000 ALTER TABLE `Rol` DISABLE KEYS */;
INSERT INTO `Rol` VALUES
(2,'COORDINADOR'),
(1,'PRACTICANTE'),
(3,'PROFESOR');
/*!40000 ALTER TABLE `Rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TitularProyecto`
--

DROP TABLE IF EXISTS `TitularProyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `TitularProyecto` (
  `id_titular` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `id_organizacion` int(11) DEFAULT NULL,
  `numero_personal` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_titular`),
  KEY `id_organizacion` (`id_organizacion`),
  CONSTRAINT `TitularProyecto_ibfk_1` FOREIGN KEY (`id_organizacion`) REFERENCES `OrganizacionVinculada` (`id_organizacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TitularProyecto`
--

LOCK TABLES `TitularProyecto` WRITE;
/*!40000 ALTER TABLE `TitularProyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `TitularProyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuario` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL,
  `access` tinyint(1) NOT NULL DEFAULT 1,
  `id_rol` int(11) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_usuario_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `Rol` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES
(1,'lenin','123456','COORDINADOR',1,2);
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-24 10:46:29
