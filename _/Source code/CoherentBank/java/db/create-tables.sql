DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
    id          BIGINT NOT NULL PRIMARY KEY,
    name        VARCHAR(50),
    username    VARCHAR(30),
    password    VARCHAR(20),
    email       VARCHAR(50),
    street      VARCHAR(50),
    city        VARCHAR(30),
    state       VARCHAR(30),
    postal_code VARCHAR(10),
    country     VARCHAR(30),
    version     INT
    );

DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts (
    id           BIGINT NOT NULL PRIMARY KEY,
    description  VARCHAR(255),
    balance      DECIMAL(20, 2),
    currency     CHAR(3),
    last_tx      BIGINT,
    customer_id  BIGINT
    );

DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
    id           BIGINT NOT NULL,
    account_id   BIGINT NOT NULL,
    type         VARCHAR(20),
    time         TIMESTAMP,
    description  VARCHAR(255),
    amount       DECIMAL(20, 2),
    balance      DECIMAL(20, 2),
    currency     VARCHAR(10),
    PRIMARY KEY (id, account_id)
    );

DROP TABLE IF EXISTS sequences;
CREATE TABLE sequences (
    name       VARCHAR(50) NOT NULL PRIMARY KEY,
    last_seq   BIGINT
    );
