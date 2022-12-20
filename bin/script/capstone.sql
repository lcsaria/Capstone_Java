CREATE DATABASE IF NOT EXISTS `capstone`;

USE `capstone`;

DROP TABLE IF EXISTS `capstone`.`customer_account`;
DROP TABLE IF EXISTS `capstone`.`policy`;
DROP TABLE IF EXISTS `capstone`.`policy_holder`;
DROP TABLE IF EXISTS `capstone`.`vehicles`;
DROP TABLE IF EXISTS `capstone`.`claim`;

CREATE TABLE IF NOT EXISTS `capstone`.`customer_account` (
  `accountNumber` INT NULL DEFAULT NULL,
  `firstName` VARCHAR(45) NULL DEFAULT NULL,
  `lastName` VARCHAR(255) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`accountNumber`));

CREATE TABLE IF NOT EXISTS `capstone`.`policy` (
  `policyNumber` INT NOT NULL,
  `effectiveDate` DATE NOT NULL,
  `expirationDate` DATE NOT NULL,
  `policyHolder` VARCHAR(255) NOT NULL,
  `vehicles` INT NOT NULL,
  `premium` DECIMAL(10,2) NOT NULL,
  `status` BOOLEAN NOT NULL,
  `acctNo` INT NOT NULL,
  PRIMARY KEY (`policyNumber`));

CREATE TABLE IF NOT EXISTS `capstone`.`policy_holder` (
  `policyNumber` INT NOT NULL,
  `firstName` VARCHAR(255) NOT NULL,
  `lastName` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `driverLicenseNumber` VARCHAR(255) NOT NULL,
  `dateIssued` VARCHAR(255) NOT NULL,
  `acctNo` INT NOT NULL,
  PRIMARY KEY (`policyNumber`));

CREATE TABLE IF NOT EXISTS `capstone`.`vehicle` (
  `policyNumber` INT NOT NULL,
  `make` VARCHAR(255) NOT NULL,
  `model` VARCHAR(255) NOT NULL,
  `year` INT NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `fuelType` VARCHAR(255) NOT NULL,
  `purchasePrice` DOUBLE NOT NULL,
  `color` VARCHAR(45) NOT NULL,
  `premium` DOUBLE NOT NULL);

CREATE TABLE IF NOT EXISTS `capstone`.`claim` (
  `claimNumber` VARCHAR(255) NOT NULL,
  `dateOfAccident` VARCHAR(255) NOT NULL,
  `accidentAddress` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `damageDescription` VARCHAR(255) NOT NULL,
  `estimatedCost` DOUBLE(15,2) NOT NULL,
  `policyNumber` INT NOT NULL,
  PRIMARY KEY (`claimNumber`));


