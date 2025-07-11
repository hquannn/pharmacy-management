CREATE TABLE EMPLOYEE (
    E_ID VARCHAR(36) PRIMARY KEY,
    E_Username VARCHAR(50) NOT NULL UNIQUE,
    E_Fname VARCHAR(255) NOT NULL,
    E_Lname VARCHAR(255),
    E_Bdate DATE,
    E_Age INT,
    E_Sex CHAR(1) NOT NULL,
    E_Type VARCHAR(50) NOT NULL,
    E_Jdate DATE NOT NULL,
    E_Add VARCHAR(255),
    E_Mail VARCHAR(255),
    E_Phno VARCHAR(20) NOT NULL,
    E_Sal DECIMAL(10, 2),
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

CREATE TABLE SUPPLIER (
    Sup_ID VARCHAR(36) PRIMARY KEY,
    Sup_Name VARCHAR(255) NOT NULL,
    Sup_Add VARCHAR(255) NOT NULL,
    Sup_Phno VARCHAR(20) NOT NULL,
    Sup_Mail VARCHAR(255),
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

CREATE TABLE CUSTOMER (
    C_ID VARCHAR(36) NOT NULL PRIMARY KEY,
    C_Fname VARCHAR(255) NOT NULL,
    C_Lname VARCHAR(255),
    C_Age INT,
    C_Sex CHAR(1) NOT NULL,
    C_Phno VARCHAR(20) NOT NULL,
    C_Mail VARCHAR(255),
    C_Points NUMERIC,
    created_date TIMESTAMP NOT NULL UNIQUE,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36),
    UNIQUE(C_Phno)
);

CREATE TABLE MED_CATEGORY (
    Cat_ID VARCHAR(36) PRIMARY KEY,
    Cat_Name VARCHAR(255) NOT NULL,
    Cat_Description VARCHAR(1000),
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

CREATE TABLE MED (
    Med_ID VARCHAR(36) PRIMARY KEY,
    Med_Name VARCHAR(255) NOT NULL,
    Med_Price DECIMAL(10, 2) NOT NULL,
    MU_ID VARCHAR(36) NOT NULL,
    Cat_ID VARCHAR(36) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

CREATE TABLE MED_UNIT (
    MU_ID VARCHAR(36) PRIMARY KEY,
    MU_Unit VARCHAR(20) NOT NULL
);

INSERT INTO MED_UNIT VALUES('facfb9d8-cf12-4e2c-8ba8-a51ef5931aa1', 'Tablet');
INSERT INTO MED_UNIT VALUES('f3b501a0-901e-4789-b35a-be8cc3d11b84', 'Blister Pack');
INSERT INTO MED_UNIT VALUES('74fa531c-634f-4aa6-b7ca-e8f7b03a9a99', 'Tube');
INSERT INTO MED_UNIT VALUES('c62491ef-47c6-4dad-b34b-b1442e83f742', 'Bottle');

CREATE TABLE PURCHASE (
    P_ID VARCHAR(36) PRIMARY KEY,
    Med_ID VARCHAR(36) NOT NULL,
    Sup_ID VARCHAR(36) NOT NULL,
    P_Qty INT NOT NULL,
    P_Cost DECIMAL(10, 2) NOT NULL,
    P_Code VARCHAR(9) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

CREATE TABLE SALE (
    Sale_ID VARCHAR(36) NOT NULL PRIMARY KEY,
    Total_Amt DECIMAL(10, 2),
    C_ID VARCHAR(36) -- allow anonymous customer
);

CREATE TABLE SALE_LOG (
    Sale_ID VARCHAR(36) NOT NULL,
    Total_Amt DECIMAL(10, 2), -- for refund case
    type VARCHAR(6) NOT NULL,
    refund_item_id VARCHAR(255), -- for refund case
    use_point BOOLEAN,
    Sale_Code VARCHAR(9) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36),
    PRIMARY KEY (Sale_ID, created_date)
) PARTITION BY RANGE (created_date);

CREATE TABLE sale_log_2024 PARTITION OF SALE_LOG
  FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE sale_log_2025 PARTITION OF SALE_LOG
  FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');

CREATE TABLE sale_log_default PARTITION OF SALE_LOG DEFAULT;

CREATE TABLE SALE_ITEM (
    I_ID VARCHAR(36),
    Sale_ID VARCHAR(36),
    Sale_Qty INT NOT NULL,
    Tot_Price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (I_ID, Sale_ID),
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

CREATE TABLE LOCATION_RACK (
    LR_ID VARCHAR(36) PRIMARY KEY,
    LR_POSITION VARCHAR(50) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

CREATE TABLE INVENTORY (
    I_ID VARCHAR(36) PRIMARY KEY,
    Med_ID VARCHAR(36) NOT NULL,
    LR_ID VARCHAR(36) NOT NULL,
    I_Qty INT NOT NULL,
    Mfg_Date DATE NOT NULL,
    Exp_Date DATE NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(36) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36),
    UNIQUE (Med_ID, Mfg_Date)
);

CREATE TABLE CUSTOMER_POINT_CONFIG (
    CPC_ID VARCHAR(36) PRIMARY KEY,
    CPC_Ratio NUMERIC NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(36)
);

INSERT INTO CUSTOMER_POINT_CONFIG VALUES('358dbac4-8270-4521-9aed-db3260b3db3e', 0.00001);

INSERT INTO EMPLOYEE VALUES('7c4f7e37-82ce-4b5a-bbb6-e57478044481', 'admin', 'admin', 'admin', null, null, 'M', 'Manager', '2024-01-01', null, 'admin1@gmail.com', '0974995189', 1, '2024-01-01', 'admin', null, null);