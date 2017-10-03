
# Redis KeyServer Tools

This folder contains the following elements:

* **cleanDb.sh**: This script remove all content from Redis DB.
* **genCerts.sh**: Used to generate your own test certificates and provision them inside Redis DB.
* **genRandomList.sh**: Creates a "pklist.txt" file whose content is a random fingerprints list provisioned inside Redis DB.
**INFO:** All those scripts, has been designed for Linux Bash.


## Test folder content*

The current directory contains two files:
* **dump.rdb**: This file can be loaded by Redis during the start process. The private keys from **certs** folder, has been provisioned inside **dump.rdb** file. 
* **redis.conf**: Tedis configuration file used for testing purpose.

And a folder:
* **certs**: Contains certificates auto generated with their the private keys. 


**\*WARNING**: The *test* folder is used by Travis-CI during unitary and integration tests.
