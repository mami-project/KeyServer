#!/bin/bash
set -e

sudo apt-get install curl build-essential libcurl4-openssl-dev openssl autoconf libtool -y
CURLPKG=$(curl -L http://curl.haxx.se/download/ | grep .tar.gz\" | tail -n 1 | cut -d "\"" -f8 | cut -d "\"" -f1)
wget http://curl.haxx.se/download/$CURLPKG
tar -xvf curl-*.tar.gz
rm curl-*.tar.gz
cd $(ls | grep curl)
./configure
make
sudo make install
