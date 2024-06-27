-- BATCH_JOB_INSTANCE table
CREATE TABLE BATCH_JOB_INSTANCE  (
    JOB_INSTANCE_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
    VERSION NUMBER(19, 0),
    JOB_NAME VARCHAR2(100) NOT NULL,
    JOB_KEY VARCHAR2(32) NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
);

-- BATCH_JOB_EXECUTION table
CREATE TABLE BATCH_JOB_EXECUTION  (
    JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
    VERSION NUMBER(19, 0),
    JOB_INSTANCE_ID NUMBER(19, 0) NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL,
    END_TIME TIMESTAMP DEFAULT NULL,
    STATUS VARCHAR2(10),
    EXIT_CODE VARCHAR2(2500),
    EXIT_MESSAGE VARCHAR2(2500),
    LAST_UPDATED TIMESTAMP,
    JOB_CONFIGURATION_LOCATION VARCHAR2(2500) NULL,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
    references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
);

-- BATCH_JOB_EXECUTION_PARAMS table
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
    JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL,
    TYPE_CD VARCHAR2(6) NOT NULL,
    KEY_NAME VARCHAR2(100) NOT NULL,
    STRING_VAL VARCHAR2(250),
    DATE_VAL TIMESTAMP DEFAULT NULL,
    LONG_VAL NUMBER(19, 0),
    DOUBLE_VAL NUMBER,
    IDENTIFYING CHAR(1) NOT NULL,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- BATCH_STEP_EXECUTION table
CREATE TABLE BATCH_STEP_EXECUTION  (
    STEP_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
    VERSION NUMBER(19, 0) NOT NULL,
    STEP_NAME VARCHAR2(100) NOT NULL,
    JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL,
    START_TIME TIMESTAMP NOT NULL,
    END_TIME TIMESTAMP DEFAULT NULL,
    STATUS VARCHAR2(10),
    COMMIT_COUNT NUMBER(19, 0),
    READ_COUNT NUMBER(19, 0),
    FILTER_COUNT NUMBER(19, 0),
    WRITE_COUNT NUMBER(19, 0),
    READ_SKIP_COUNT NUMBER(19, 0),
    WRITE_SKIP_COUNT NUMBER(19, 0),
    PROCESS_SKIP_COUNT NUMBER(19, 0),
    ROLLBACK_COUNT NUMBER(19, 0),
    EXIT_CODE VARCHAR2(2500) NOT NULL,
    EXIT_MESSAGE VARCHAR2(2500),
    LAST_UPDATED TIMESTAMP,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- BATCH_STEP_EXECUTION_CONTEXT table
CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
    STEP_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
    references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);

-- BATCH_JOB_EXECUTION_CONTEXT table
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
    JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- BATCH_STEP_EXECUTION_SEQ sequence
CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ
    MINVALUE 0
    START WITH 1
    INCREMENT BY 1
    CACHE 1000;

-- BATCH_JOB_EXECUTION_SEQ sequence
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ
    MINVALUE 0
    START WITH 1
    INCREMENT BY 1
    CACHE 1000;

-- BATCH_JOB_SEQ sequence
CREATE SEQUENCE BATCH_JOB_SEQ
    MINVALUE 0
    START WITH 1
    INCREMENT BY 1
    CACHE 1000;


-----------------IF YOU CREATE THIS ENTITIES IN YOUR DATABASE, THESE ARE MESS UP WITH EXISTING BUSSINESS DATABASES-------------------
/*
	Spring Batch requires certain tables to be present in your database to function correctly. 
	These tables are not created automatically, so you need to create them manually. 
	Here is a script for creating the necessary Spring Batch tables in an Oracle database
*/
-----------------SO TRY TO CREATE DIFFERENT DATABASE OR SCHEMA OR PACKAGE IN MY CASE I CREATED THE DIFFERENT PACKAGE-----------------

CREATE OR REPLACE PACKAGE batch_metadata_pkg AS
    PROCEDURE create_batch_tables;
    PROCEDURE drop_batch_tables;
END batch_metadata_pkg;
/
CREATE OR REPLACE PACKAGE BODY batch_metadata_pkg AS

    PROCEDURE create_batch_tables IS
    BEGIN
        EXECUTE IMMEDIATE 'CREATE TABLE BATCH_JOB_INSTANCE (
            JOB_INSTANCE_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
            VERSION NUMBER(19, 0),
            JOB_NAME VARCHAR2(100) NOT NULL,
            JOB_KEY VARCHAR2(32) NOT NULL,
            CONSTRAINT JOB_INST_UN UNIQUE (JOB_NAME, JOB_KEY)
        )';

        EXECUTE IMMEDIATE 'CREATE TABLE BATCH_JOB_EXECUTION (
            JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
            VERSION NUMBER(19, 0),
            JOB_INSTANCE_ID NUMBER(19, 0) NOT NULL,
            CREATE_TIME TIMESTAMP NOT NULL,
            START_TIME TIMESTAMP DEFAULT NULL,
            END_TIME TIMESTAMP DEFAULT NULL,
            STATUS VARCHAR2(10),
            EXIT_CODE VARCHAR2(2500),
            EXIT_MESSAGE VARCHAR2(2500),
            LAST_UPDATED TIMESTAMP,
            JOB_CONFIGURATION_LOCATION VARCHAR2(2500) NULL,
            CONSTRAINT JOB_INST_EXEC_FK FOREIGN KEY (JOB_INSTANCE_ID)
            REFERENCES BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
        )';

        EXECUTE IMMEDIATE 'CREATE TABLE BATCH_JOB_EXECUTION_PARAMS (
            JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL,
            TYPE_CD VARCHAR2(6) NOT NULL,
            KEY_NAME VARCHAR2(100) NOT NULL,
            STRING_VAL VARCHAR2(250),
            DATE_VAL TIMESTAMP DEFAULT NULL,
            LONG_VAL NUMBER(19, 0),
            DOUBLE_VAL NUMBER,
            IDENTIFYING CHAR(1) NOT NULL,
            CONSTRAINT JOB_EXEC_PARAMS_FK FOREIGN KEY (JOB_EXECUTION_ID)
            REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
        )';

        EXECUTE IMMEDIATE 'CREATE TABLE BATCH_STEP_EXECUTION (
            STEP_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
            VERSION NUMBER(19, 0) NOT NULL,
            STEP_NAME VARCHAR2(100) NOT NULL,
            JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL,
            START_TIME TIMESTAMP NOT NULL,
            END_TIME TIMESTAMP DEFAULT NULL,
            STATUS VARCHAR2(10),
            COMMIT_COUNT NUMBER(19, 0),
            READ_COUNT NUMBER(19, 0),
            FILTER_COUNT NUMBER(19, 0),
            WRITE_COUNT NUMBER(19, 0),
            READ_SKIP_COUNT NUMBER(19, 0),
            WRITE_SKIP_COUNT NUMBER(19, 0),
            PROCESS_SKIP_COUNT NUMBER(19, 0),
            ROLLBACK_COUNT NUMBER(19, 0),
            EXIT_CODE VARCHAR2(2500) NOT NULL,
            EXIT_MESSAGE VARCHAR2(2500),
            LAST_UPDATED TIMESTAMP,
            CONSTRAINT JOB_EXEC_STEP_FK FOREIGN KEY (JOB_EXECUTION_ID)
            REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
        )';

        EXECUTE IMMEDIATE 'CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT (
            STEP_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
            SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
            SERIALIZED_CONTEXT CLOB,
            CONSTRAINT STEP_EXEC_CTX_FK FOREIGN KEY (STEP_EXECUTION_ID)
            REFERENCES BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
        )';

        EXECUTE IMMEDIATE 'CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT (
            JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL PRIMARY KEY,
            SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
            SERIALIZED_CONTEXT CLOB,
            CONSTRAINT JOB_EXEC_CTX_FK FOREIGN KEY (JOB_EXECUTION_ID)
            REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
        )';

        EXECUTE IMMEDIATE 'CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ
            MINVALUE 0
            START WITH 1
            INCREMENT BY 1
            CACHE 1000';

        EXECUTE IMMEDIATE 'CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ
            MINVALUE 0
            START WITH 1
            INCREMENT BY 1
            CACHE 1000';

        EXECUTE IMMEDIATE 'CREATE SEQUENCE BATCH_JOB_SEQ
            MINVALUE 0
            START WITH 1
            INCREMENT BY 1
            CACHE 1000';
    END create_batch_tables;

    PROCEDURE drop_batch_tables IS
    BEGIN
        EXECUTE IMMEDIATE 'DROP TABLE BATCH_JOB_EXECUTION_CONTEXT';
        EXECUTE IMMEDIATE 'DROP TABLE BATCH_STEP_EXECUTION_CONTEXT';
        EXECUTE IMMEDIATE 'DROP TABLE BATCH_STEP_EXECUTION';
        EXECUTE IMMEDIATE 'DROP TABLE BATCH_JOB_EXECUTION_PARAMS';
        EXECUTE IMMEDIATE 'DROP TABLE BATCH_JOB_EXECUTION';
        EXECUTE IMMEDIATE 'DROP TABLE BATCH_JOB_INSTANCE';

        EXECUTE IMMEDIATE 'DROP SEQUENCE BATCH_STEP_EXECUTION_SEQ';
        EXECUTE IMMEDIATE 'DROP SEQUENCE BATCH_JOB_EXECUTION_SEQ';
        EXECUTE IMMEDIATE 'DROP SEQUENCE BATCH_JOB_SEQ';
    END drop_batch_tables;

END batch_metadata_pkg;
/
-- To create the tables
BEGIN
    batch_metadata_pkg.create_batch_tables;
END;
/

-- To drop the tables
BEGIN
    batch_metadata_pkg.drop_batch_tables;
END;
/
--------------------
-- Connect as a user with DBA privileges
sqlplus sys as sysdba

-- Grant the necessary privileges to your user
GRANT CREATE TABLE TO your_username;
GRANT CREATE SEQUENCE TO your_username;
GRANT CREATE PROCEDURE TO your_username;
GRANT EXECUTE ANY PROCEDURE TO your_username;



DROP TABLE BATCH_JOB_EXECUTION_PARAMS CASCADE CONSTRAINTS;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS (
    JOB_EXECUTION_ID NUMBER(19, 0) NOT NULL,
    PARAMETER_NAME VARCHAR2(100) NOT NULL,
    PARAMETER_TYPE VARCHAR2(100) NOT NULL,
    PARAMETER_VALUE VARCHAR2(2500),
    IDENTIFYING CHAR(1) NOT NULL,
    CONSTRAINT JOB_EXEC_PARAMS_FK FOREIGN KEY (JOB_EXECUTION_ID)
    REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);


ALTER TABLE BATCH_STEP_EXECUTION
ADD CREATE_TIME TIMESTAMP;






