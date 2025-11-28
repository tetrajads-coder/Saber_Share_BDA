-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema TetraJADS
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `TetraJADS`; -- Opcional: Borra la anterior para empezar limpio
CREATE SCHEMA IF NOT EXISTS `TetraJADS` DEFAULT CHARACTER SET utf8mb3 ;
USE `TetraJADS` ;

-- -----------------------------------------------------
-- 1. Tabla Roles (Debe ir PRIMERO)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Rol` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(200) NULL,
  PRIMARY KEY (`idRol`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- 2. Tabla Usuario (Con relación a Roles)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `Usu_usu` VARCHAR(45) NOT NULL,
  `Nom_usu` VARCHAR(45) NOT NULL,
  `Ape_usu` VARCHAR(45) NOT NULL,
  `Correo_usu` VARCHAR(45) NOT NULL,
  `Contra_usu` VARCHAR(45) NOT NULL,
  `Fot_usu` LONGBLOB NULL DEFAULT NULL,
  `Inte_usu` VARCHAR(400) NULL DEFAULT NULL,
  `Tel_usu` VARCHAR(45) NOT NULL,
  `Rol_idRol` INT NOT NULL DEFAULT 1, -- Por defecto es Rol 1 (Usuario)
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `fk_Usuario_Rol`
    FOREIGN KEY (`Rol_idRol`)
    REFERENCES `TetraJADS`.`Rol` (`idRol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_Usuario_Rol_idx` ON `TetraJADS`.`Usuario` (`Rol_idRol` ASC) VISIBLE;

-- -----------------------------------------------------
-- 3. Tabla Agenda
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Agenda` (
  `idAgenda` INT NOT NULL AUTO_INCREMENT,
  `fechaserv` DATE NOT NULL,
  `pago` DOUBLE NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idAgenda`),
  CONSTRAINT `fk_Agenda_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TetraJADS`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_Agenda_Usuario1_idx` ON `TetraJADS`.`Agenda` (`Usuario_idUsuario` ASC) VISIBLE;

-- -----------------------------------------------------
-- 4. Tabla Curso
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Curso` (
  `idCurso` INT NOT NULL AUTO_INCREMENT,
  `tit_cur` VARCHAR(45) NOT NULL,
  `desc_cur` VARCHAR(350) NOT NULL,
  `pre_cur` DOUBLE NOT NULL,
  `calf_cur` VARCHAR(45) NOT NULL,
  `Foto` VARCHAR(500) NULL DEFAULT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idCurso`),
  CONSTRAINT `fk_Curso_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TetraJADS`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_Curso_Usuario1_idx` ON `TetraJADS`.`Curso` (`Usuario_idUsuario` ASC) VISIBLE;

-- -----------------------------------------------------
-- 5. Tabla Servicio
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Servicio` (
  `idServicios` INT NOT NULL AUTO_INCREMENT,
  `tit_ser` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(350) NOT NULL,
  `img_ser` LONGBLOB NULL DEFAULT NULL,
  `precio_ser` DOUBLE NOT NULL,
  `fecha_ser` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `req_ser` VARCHAR(500) NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idServicios`),
  CONSTRAINT `fk_Servicio_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TetraJADS`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_Servicio_Usuario1_idx` ON `TetraJADS`.`Servicio` (`Usuario_idUsuario` ASC) VISIBLE;

-- -----------------------------------------------------
-- 6. Tabla Historial
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Historial` (
  `idHistorial` INT NOT NULL AUTO_INCREMENT,
  `fechapago` DATE NOT NULL,
  `pago` DOUBLE NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  `Servicio_idServicios` INT NULL,
  `Curso_idCurso` INT NULL,
  PRIMARY KEY (`idHistorial`),
  CONSTRAINT `fk_Historial_Usuario`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TetraJADS`.`Usuario` (`idUsuario`),
  CONSTRAINT `fk_Historial_Servicio1`
    FOREIGN KEY (`Servicio_idServicios`)
    REFERENCES `TetraJADS`.`Servicio` (`idServicios`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Historial_Curso1`
    FOREIGN KEY (`Curso_idCurso`)
    REFERENCES `TetraJADS`.`Curso` (`idCurso`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_Historial_Usuario_idx` ON `TetraJADS`.`Historial` (`Usuario_idUsuario` ASC) VISIBLE;
CREATE INDEX `fk_Historial_Servicio1_idx` ON `TetraJADS`.`Historial` (`Servicio_idServicios` ASC) VISIBLE;
CREATE INDEX `fk_Historial_Curso1_idx` ON `TetraJADS`.`Historial` (`Curso_idCurso` ASC) VISIBLE;

-- -----------------------------------------------------
-- 7. Tabla Metodo_de_Pago
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Metodo_de_Pago` (
  `idMetodo_de_Pago` INT NOT NULL AUTO_INCREMENT,
  `compañia` VARCHAR(45) NOT NULL,
  `numtar` VARCHAR(25) NOT NULL,
  `CVV` VARCHAR(15) NOT NULL,
  `venci` DATE NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idMetodo_de_Pago`),
  CONSTRAINT `fk_Metodo_de_Pago_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TetraJADS`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_Metodo_de_Pago_Usuario1_idx` ON `TetraJADS`.`Metodo_de_Pago` (`Usuario_idUsuario` ASC) VISIBLE;

-- -----------------------------------------------------
-- 8. Tabla OpinionServicio
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`OpinionServicio` (
  `idOpiniones` INT NOT NULL AUTO_INCREMENT,
  `coment_ops` VARCHAR(45) NOT NULL,
  `cal_ops` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  `Servicio_Servicios` INT NOT NULL,
  PRIMARY KEY (`idOpiniones`),
  CONSTRAINT `fk_OpinioneServicio_Servicio1`
    FOREIGN KEY (`Servicio_Servicios`)
    REFERENCES `TetraJADS`.`Servicio` (`idServicios`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_OpinioneServicio_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TetraJADS`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_OpinioneServicio_Usuario1_idx` ON `TetraJADS`.`OpinionServicio` (`Usuario_idUsuario` ASC) VISIBLE;
CREATE INDEX `fk_OpinioneServicio_Servicio1_idx` ON `TetraJADS`.`OpinionServicio` (`Servicio_Servicios` ASC) VISIBLE;

-- -----------------------------------------------------
-- 9. Tabla OpinionesCurso
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`OpinionesCurso` (
  `idOpiniones` INT NOT NULL AUTO_INCREMENT,
  `coment_ops` VARCHAR(45) NOT NULL,
  `cal_ops` INT NOT NULL,
  `Curso_idCurso` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idOpiniones`),
  CONSTRAINT `fk_OpinionesCurso_Curso1`
    FOREIGN KEY (`Curso_idCurso`)
    REFERENCES `TetraJADS`.`Curso` (`idCurso`),
  CONSTRAINT `fk_OpinionesCurso_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TetraJADS`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

CREATE INDEX `fk_OpinionesCurso_Curso1_idx` ON `TetraJADS`.`OpinionesCurso` (`Curso_idCurso` ASC) VISIBLE;
CREATE INDEX `fk_OpinionesCurso_Usuario1_idx` ON `TetraJADS`.`OpinionesCurso` (`Usuario_idUsuario` ASC) VISIBLE;

-- -----------------------------------------------------
-- 10. Tabla Bitacora (¡Esta era la que faltaba!)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TetraJADS`.`Bitacora` (
  `idBitacora` INT NOT NULL AUTO_INCREMENT,
  `usuario_afectado` VARCHAR(45) NULL,
  `accion` VARCHAR(45) NOT NULL,
  `tabla` VARCHAR(45) NOT NULL,
  `fecha` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`idBitacora`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- DATOS: Insertar Roles Básicos
-- -----------------------------------------------------
INSERT IGNORE INTO `TetraJADS`.`Rol` (`idRol`, `nombre`, `descripcion`) VALUES 
(1, 'USUARIO', 'Usuario final estándar'),
(2, 'ADMINISTRADOR', 'Acceso total al sistema'),
(3, 'CHALAN', 'Permisos limitados');

-- -----------------------------------------------------
-- TRIGGER: Auditoría Automática
-- -----------------------------------------------------
DELIMITER $$
DROP TRIGGER IF EXISTS `audit_usuario_insert` $$
CREATE TRIGGER `audit_usuario_insert` 
AFTER INSERT ON `Usuario`
FOR EACH ROW
BEGIN
    INSERT INTO `Bitacora` (`usuario_afectado`, `accion`, `tabla`, `fecha`) 
    VALUES (NEW.Correo_usu, 'INSERTO', 'Usuario', NOW());
END$$
DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;