#!/bin/bash
set -e

DOWNLOAD=$(curl -L https://github.com/mami-project/KeyServer/releases/latest | grep .jar\"  | cut -d "\"" -f2 | cut -d "\"" -f1)
KSVERSION=$(curl -L https://github.com/mami-project/KeyServer/releases/latest | grep css-truncate-target | cut -d ">" -f2 |  cut -d "<" -f1)
echo "URL: $DOWNLOAD"
echo "VERSION: $KSVERSION"
wget -O KeyServer-$KSVERSION.jar $DOWNLOAD
