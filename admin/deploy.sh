!#/bin/bash

./gradlew clean bootJar
scp ./build/libs/admin-0.0.1-SNAPSHOT.jar root@47.101.10.94:/root
rm -rf ./build/libs/admin-0.0.1-SNAPSHOT.jar
