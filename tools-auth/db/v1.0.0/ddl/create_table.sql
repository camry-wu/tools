/*
    Product: Cooking

    Creator: Camry 2015/07/27

    Copyright: Copyright 2015 by Vitular Corp. Ltd

    Database: Mysql 5.6

    Function: Creating all tables:

    Version: 1.0.0
*/

/* =======================< use cooking >======================= */

use cooking;

/*=============================== User ========================*/

drop table if exists user;
create table user
(
    oid                 bigint          not null,   /* OID */

    Username            varchar(255)    null,       /* Username */
    Password            varchar(255)    null,       /* Password */
    Salt                varchar(255)    null,       /* Password Salt */
    Locked              tinyint         default 0,  /* Is Locked, (0: No; 1: Yes)*/
    VerifyEmail         varchar(255)    null,       /* VerifyEmail */
    VerifyCellPhoneNo   varchar(255)    null,       /* VerifyCellPhoneNo */

    Version             int             null,       /* version */
    LastUpdate          datetime        null,       /* last modify date */
    LastUpdater         varchar(255)    null,       /* last updater user name */
    IsActive            tinyint         default 0,  /* Is active, Normal Code[2](0: No; 1: Yes)*/

    unique index(Username),
    unique index(VerifyEmail),
    unique index(VerifyCellPhoneNo),
    primary key (oid)
);

/*=============================== Serial No ========================*/

drop table if exists serial_number;
create table serial_number
(
    oid         bigint          not null    AUTO_INCREMENT,
    IdKey       varchar(255)    not null,
    IdValue     bigint          not null,
    primary key (oid)
);
