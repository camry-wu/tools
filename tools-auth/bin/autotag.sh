#!/bin/sh

rm tags
ctags -R --language-force=java --exclude=*.svn* --exclude=*.java~ src/*
