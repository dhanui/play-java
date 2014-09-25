--
-- ER/Studio 8.0 SQL Code Generation
-- Company :      dijedodol
-- Project :      db_model.dm1
-- Author :       dijedodol
--
-- Date Created : Tuesday, September 23, 2014 14:53:53
-- Target DBMS : MySQL 5.x
--

-- 
-- TABLE: app 
--

CREATE TABLE app(
    id         BIGINT          AUTO_INCREMENT,
    name       VARCHAR(100)    NOT NULL,
    api_key    VARCHAR(100)    NOT NULL,
    PRIMARY KEY (id)
)ENGINE=INNODB
;



-- 
-- TABLE: app_instance 
--

CREATE TABLE app_instance(
    id                           BIGINT          AUTO_INCREMENT,
    access_token                 VARCHAR(100)    NOT NULL,
    access_token_expired_time    TIMESTAMP       NOT NULL,
    refresh_token                VARCHAR(100)    NOT NULL,
    app_id                       BIGINT          NOT NULL,
    device_instance_id           BIGINT          NOT NULL,
    PRIMARY KEY (id)
)ENGINE=INNODB
;



-- 
-- TABLE: app_instance_user 
--

CREATE TABLE app_instance_user(
    app_instance_id    BIGINT    NOT NULL,
    user_id            BIGINT    NOT NULL,
    PRIMARY KEY (app_instance_id, user_id)
)ENGINE=INNODB
;



-- 
-- TABLE: device_instance 
--

CREATE TABLE device_instance(
    id                   BIGINT          AUTO_INCREMENT,
    device_model_id      BIGINT          NOT NULL,
    device_id            VARCHAR(100)    NOT NULL,
    device_push_token    VARCHAR(100)    NOT NULL,
    os_type              VARCHAR(100)    NOT NULL,
    os_version           VARCHAR(100)    NOT NULL,
    PRIMARY KEY (id)
)ENGINE=INNODB
;



-- 
-- TABLE: device_model 
--

CREATE TABLE device_model(
    id              BIGINT          AUTO_INCREMENT,
    manufacturer    VARCHAR(100)    NOT NULL,
    model           VARCHAR(100)    NOT NULL,
    PRIMARY KEY (id)
)ENGINE=INNODB
;



-- 
-- TABLE: facebook_user 
--

CREATE TABLE facebook_user(
    id                           BIGINT          NOT NULL,
    email                        VARCHAR(100)    NOT NULL,
    date_of_birth                DATE,
    gender                       VARCHAR(100),
    access_token                 VARCHAR(100)    NOT NULL,
    access_token_expired_date    TIMESTAMP       NOT NULL,
    PRIMARY KEY (id)
)ENGINE=INNODB
;



-- 
-- TABLE: global_user 
--

CREATE TABLE global_user(
    id            BIGINT          AUTO_INCREMENT,
    uuid          VARCHAR(100)    NOT NULL,
    first_name    VARCHAR(100),
    last_name     VARCHAR(100),
    PRIMARY KEY (id)
)ENGINE=INNODB
;



-- 
-- TABLE: iceberg_user 
--

CREATE TABLE iceberg_user(
    id               BIGINT          NOT NULL,
    email            VARCHAR(100)    NOT NULL,
    date_of_birth    DATE            NOT NULL,
    gender           VARCHAR(100)    NOT NULL,
    PRIMARY KEY (id)
)ENGINE=INNODB
;



-- 
-- TABLE: app_instance 
--

ALTER TABLE app_instance ADD CONSTRAINT Refapp2 
    FOREIGN KEY (app_id)
    REFERENCES app(id)
;

ALTER TABLE app_instance ADD CONSTRAINT Refdevice_instance3 
    FOREIGN KEY (device_instance_id)
    REFERENCES device_instance(id)
;


-- 
-- TABLE: app_instance_user 
--

ALTER TABLE app_instance_user ADD CONSTRAINT Refapp_instance7 
    FOREIGN KEY (app_instance_id)
    REFERENCES app_instance(id)
;

ALTER TABLE app_instance_user ADD CONSTRAINT Refglobal_user8 
    FOREIGN KEY (user_id)
    REFERENCES global_user(id)
;


-- 
-- TABLE: device_instance 
--

ALTER TABLE device_instance ADD CONSTRAINT Refdevice_model4 
    FOREIGN KEY (device_model_id)
    REFERENCES device_model(id)
;


-- 
-- TABLE: facebook_user 
--

ALTER TABLE facebook_user ADD CONSTRAINT Refglobal_user5 
    FOREIGN KEY (id)
    REFERENCES global_user(id)
;


-- 
-- TABLE: iceberg_user 
--

ALTER TABLE iceberg_user ADD CONSTRAINT Refglobal_user6 
    FOREIGN KEY (id)
    REFERENCES global_user(id)
;


