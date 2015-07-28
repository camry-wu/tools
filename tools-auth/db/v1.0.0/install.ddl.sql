/*
   Product: Cooking

   Creator: Camry 2015/07/27

   Copyright: Copyright 2015 by Vitular Corp. Ltd
  
   Database: Mysql 5.6

   Function: Running install DDL sql script files
*/

/* Creating Database */
source ddl/create_databases.sql;

/* Creating Database Users */
source ddl/grant_user.sql;

/* Creating tables */
source ddl/create_table.sql;
