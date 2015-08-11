#!/bin/sh

native2ascii -encoding GBK src/Resources.properties.gbk src/Resources.properties
#native2ascii -encoding GBK src/Resources.properties.en src/Resources.properties

native2ascii -encoding UTF-8 src/centralmgr.ncode.properties.utf_8 src/centralmgr.ncode.properties
#native2ascii -encoding UTF-8 src/centralmgr.ncode.properties.en src/centralmgr.ncode.properties

native2ascii -encoding UTF-8 src/centralmgr.scode.properties.utf_8 src/centralmgr.scode.properties
#native2ascii -encoding UTF-8 src/centralmgr.scode.properties.en src/centralmgr.scode.properties

native2ascii -encoding GBK src/centralmgr.access.properties.gbk src/centralmgr.access.properties
#native2ascii -encoding GBK src/centralmgr.access.properties.en src/centralmgr.access.properties

native2ascii -encoding UTF-8 src/consts.properties.utf_8 src/consts.properties

#src/menu.xml.en
