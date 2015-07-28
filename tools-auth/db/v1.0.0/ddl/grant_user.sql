/*
   Product: Cooking

   Creator: Camry 2015/07/27

   Copyright: Copyright 2015 by Vitular Corp. Ltd
  
   Database: Mysql 5.6

   Function: Creating all database users
*/

grant all on *.* to 'cooking_db_admin'@'%' identified by 'cooking#' with grant option;
grant all on *.* to 'cooking_db_admin'@'localhost' identified by 'cooking#' with grant option;
