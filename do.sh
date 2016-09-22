#!/bin/sh
mvn clean package
mvn cargo:redeploy
