#!/bin/bash
# Junit
rm junit-4.12.jar lib/junit-4.12.jar
wget http://eva.fit.vutbr.cz/~xfusek08/IJA/junit-4.12.jar
mv junit-4.12.jar lib/junit-4.12.jar

# hamcrest-core
rm hamcrest-core-1.3.jar lib/hamcrest-core-1.3.jar
wget http://eva.fit.vutbr.cz/~xfusek08/IJA/hamcrest-core-1.3.jar
mv hamcrest-core-1.3.jar lib/hamcrest-core-1.3.jar

# hamcrest-core
rm ant-javafx.jar lib/ant-javafx.jar
wget http://eva.fit.vutbr.cz/~xfusek08/IJA/ant-javafx.jar
mv ant-javafx.jar lib/ant-javafx.jar