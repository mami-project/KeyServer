#!/bin/bash
set -e

COUNTER=0
NUMCERT=1

if [ $# -eq 1 ] ; then
        NUMCERT=$1
fi

while [ $COUNTER -lt $NUMCERT ]; do
        redis-cli -a foobared -n 0 RANDOMKEY >> pklist.txt
        let COUNTER=COUNTER+1
done

echo "[ OK ]"

exit 0
