#!/bin/bash -e
java -jar rgLuntan.jar --spring.profiles.active=prod > log.file 2>&1 &
