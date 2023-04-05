# Webserver Setup API

A simple API to work with Java Servlets and Hibernate. This stub can be used to extend it into full webserver.
Note this is work in progress, and some functions may not work as expected.

Features:
 - MVC architecture
 - action & page mapping - call particular actions by URL parameter *action*
 - high-level object query from database (ORM) - classes DataStorage and Query
 - database CSV loader on startup
 - autoscheduled tasks in separate threads

Deployment (Tomcat 9.0+):
 - build .WAR file (Eclipse is recommended)
 - copy the .WAR in tomcat/webapp directory
 - launch Tomcat
 - access localhost via browser