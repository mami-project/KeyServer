#!/bin/bash
set -e

sudo apt-get install curl build-essential libcurl4-openssl-dev openssl autoconf libtool -y
KSPKG=$(curl -L https://github.com/mami-project/KeyServer/releases/latest | grep .jar\"  | cut -d "\"" -f2 | cut -d "\"" -f1)
DOWNLOAD=https://github.com$KSPKG
echo "DEBUG: $DOWNLOAD" 
wget $DOWNLOAD
echo "Downloaded KeyServer version: $KSPKG"
