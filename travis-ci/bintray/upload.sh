#!/bin/bash
set -e

# Creates the JAR file to be uploaded.
 mvn clean package
# Move the file inside Upload directory.
mkdir -p ./target/bintray
mv ./target/KeyServer-*-jar-with-dependencies.jar ./target/bintray
mv ./target/bintray/KeyServer-*-jar-with-dependencies.jar `echo ./target/bintray/KeyServer-*-jar-with-dependencies.jar | sed s/-jar-with-dependencies//g`;
KS_VER=`echo ./target/bintray/KeyServer-*.jar | sed s/.jar//g | awk -F'-' '{print $2}'`;
echo "[ INFO ] KeyServer current version: $KS_VER"
# Upload
if [ "$TRAVIS_BRANCH" == "master" ]
then
    echo "[ INFO ] Starting upload to main package."
    curl -v -T ./target/bintray/KeyServer-*.jar -ujgm1986:$BINTRAY_API_KEY -H "X-Bintray-Package:KeyServer" -H "X-Bintray-Version:$KS_VER" -H "X-Bintray-Publish:1" -H "X-Bintray-Override:1" https://api.bintray.com/content/jgm1986/KeyServer/KeyServer/$KS_VER/
fi
if [ "$TRAVIS_BRANCH" == "develop" ]
then
    echo "[ INFO ] Starting upload to SNAPSHOT package."
    curl -v -T ./target/bintray/KeyServer-*.jar -ujgm1986:$BINTRAY_API_KEY -H "X-Bintray-Package:KeyServer-DEV" -H "X-Bintray-Version:SNAPSHOT" -H "X-Bintray-Publish:1" -H "X-Bintray-Override:1" https://api.bintray.com/content/jgm1986/KeyServer/KeyServer-DEV/SNAPSHOT/
fi
echo "[ INFO ] Done!"
