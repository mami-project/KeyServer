# KeyServer Client 

This tool has been designed for evaluate the performance of KeyServer for ECDH signing operation. It loads all fingerprints included inside a list (external text file), and send

## Components

The package contains the following files:

 File Name        | Description
----------------- | ---------------------------------------
 genRandomList.sh | This Bash script has been designed for Ubuntu. It can be used for generate a random list of certificates fingerprints included in Redis DB. You don't need execute this script unless you want generate your own list.
 pklist.txt       | This is an example list with 1000000 certificates fingerprints. Use this file with the Redis dump file with 1000000 certificates.
 ksClient.jar    | KeyServer client for performance tests.


## How to execute the KeyServer client?

**Warning**: This tool requires a file list with the certificates fingerprints. The user can generate a new list with `genRandomList.sh` or use the pregenerated file `pklist.txt`.

The command structure used for execute KeyServer client is:

```
java -jar ksClient.jar KS_IP KS_PORT TIMEOUT CERT_LIST N_CERTS PAUSE
```

Field        | Description
------------ | -------------------------------------------
 KS_IP       | KeyServer server IP address. Example: `127.0.0.1`
 KS_Port     | KeyServer application port. Example: `1443`
 TIMEOUT     | Client timeout value in seconds.
 CERT_LIST   | Certificate fingerprint list file.
 N_CERTS     | *Optional field*. Used to define a number of certificates to use in this test.
 PAUSE       | *Optional field*. Pause between certificate requests.


This is an example:

```
java -jar ksClient.jar 127.0.0.1 1443 30 pklist.txt 3000 10000
```


The output of this tool is similar to:

```
{
        "protocol":"TLS 1.2",
        "method":"ECDHE",
        "spki":"45EEDCCF64B9535A3FBFAC746E8F56E09BC49F0D",
        "hash":"SHA-256",
        "input":"BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/Y
mBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCs
LMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ=="
}
{"output":"p1KNy/aChvZ3n6BgKPpE0ta4+8kGPoFQc17KIbajdw7uuAiAhDTbRFqvV3bBpzoFD7833
15c68yzsiewfRUVRzC2k7xxkR0A2Sdegsp1lNYygr0W5LJqdfn/tM5+o0CfcqYF/CGcjSRlfYnLST6Nc
s2otVsz3FSttjVOdN5DO40VGjbLhbtRpgAmPYm3udU+T40FoAMuq2sRG5XAtHWRjXkYqSc6ZJgfbZQl8
s1fXD+I+AgPG9oeK9tXwg03vBdbqrt3zkmE2sR8/uKp7aGT9D8iJasqs1c5prqjFXIm13CfXuYpfjjov
KQXfP/nXulBdqBwWdBMdeQz9Qa+lBHJGoYbkf3MvwaT+KBjxZPb8qZ6Va/3O8EPp3yqJSn1NuyYNqPzJ
D8EYH1P/PN0mpsJz1nEwy/2tZC9De9JujUKDFzhQZU+ycsKJNa8oZQU6bKVohCWJWP27XGze9ZSdELlA
zKCBy81UEvvnGExVZuv5FKLp7j0oJoENyeKnPQZiCPxFR0outCcVb98d3RqCsdodazaXpAOAVyoq6Oui
RfEqFrG8tktRlU308kPeEpudMQ3nCFi/BwejmYqLgfuMDBdAlCCAQMlU9GpOLeBnoh4KXm00Q1zeSLBJ
kZOKNHFuAvnPtHObnf9+1bJ+DGsDG5A+f8mWj2MVxDeXiQdbBhUxkA="}

Test 47 Request Time: 129 ms
```

The first JSON contains the request from the KeyServer to the KeyServer Server. The second JSON is the response from the KeyServer. Finally, the last line contains the current test number (in this example, it's the number 47) and the time needed for complete this request.


## Create a new Fingerprint List

This tool requires a linux distribution with `redis-tool` installed on your system.

```
sudo apt-get install redis-tools
```

The user must include the number of random certificate fingerprints to include inside output list file. This script only works from the server where Redis is running.

Command structure:

```
genRandomList.sh NUMBER
```

Field        | Description
------------ | -------------------------------------------
 NUMBER      | Number of certificates inside list.

Example:

```
genRandomList.sh 1000
```