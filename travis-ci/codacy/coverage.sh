#!/bin/bash
set -e

echo "[ INFO ] KeyServer project branch: $TRAVIS_BRANCH"
# Upload
if [ "$TRAVIS_BRANCH" == "master" ]
then
    echo "[ INFO ] Calculating code coverage for master branch."
    mvn com.gavinmogan:codacy-maven-plugin:coverage -DcoverageReportFile=target/site/cobertura/coverage.xml -DprojectToken=$CODACY_PROJECT_TOKEN -DapiToken=$CODACY_PROJECT_TOKEN
fi
if [ "$TRAVIS_BRANCH" == "develop" ]
then
    echo "[ INFO ] Calculating code coverage for develop branch."
    mvn com.gavinmogan:codacy-maven-plugin:coverage -DcoverageReportFile=target/site/cobertura/coverage.xml -DprojectToken=$CODACY_DEVELOP_TOKEN -DapiToken=$CODACY_DEVELOP_TOKEN
fi
echo "[ INFO ] Done!"
