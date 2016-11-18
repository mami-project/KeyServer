#!/bin/bash
set -e

COUNTER=0
NUMCERT=1

if [ $# -eq 1 ] ; then
	NUMCERT=$1
fi

if [ -d certs ] ; then
	echo "[ INFO ] Deleting previous certs folder..."
	rm -r certs
	echo "[ OK ]"
fi

echo "[ INFO ] Creating certs folder..."
mkdir certs
if [ -w general.properties ] ; then
	cp general.properties certs/general.properties
fi
cd certs
echo "[ OK ]"

echo [ INFO ] Generating following certificates: $NUMCERT

while [ $COUNTER -lt $NUMCERT ]; do
	openssl req -new -newkey rsa:4096 -days 365 -nodes -x509 -subj "/C=ES/ST=Madrid/L=Madrid/O=Test/CN=www.example.com" -keyout $COUNTER.key -out $COUNTER.cert
	FINGERPRINT="$(openssl x509 -in $COUNTER.cert -fingerprint -noout)"
	FINGERPRINT="${FINGERPRINT//:}"
	FINGERPRINT="${FINGERPRINT#*=}"
	echo [ INFO ] Certificate fingerprint: $FINGERPRINT
	echo $FINGERPRINT >> FINGERPRINTS.list
	openssl pkcs8 -topk8 -inform PEM -outform DER -in $COUNTER.key  -nocrypt > $COUNTER.exp.key
	java -jar ../pk-provider.jar -i $FINGERPRINT $COUNTER.exp.key
	echo [ INFO ] The counter is $COUNTER
	let COUNTER=COUNTER+1
done

if [ -w general.properties ] ; then
        mv general.properties ../general.properties
fi

echo "[ OK ]"


exit 0

