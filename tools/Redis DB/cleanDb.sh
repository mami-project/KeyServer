#!/bin/bash
set -e

echo "[ INFO ] Getting certificate list from database..."
CERTLIST=$(java -jar pk-provider.jar -l)
#CERTLIST="${CERTLIST#* }"
#echo [ DEBUG ]
#echo $CERTLIST
for SHA1 in $CERTLIST; do
	if [[ $SHA1 != *['!'\)]* ]]; then
		echo [ INFO ] Deleting certificate from DB: $SHA1
		java -jar pk-provider.jar -d $SHA1
	fi
done

exit 0

