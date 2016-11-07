
# How to test KeyServer with cURL?

KeyServer can be tested easily using [cURL](https://curl.haxx.se/ "cURL").

Suppose that:
 - You have a KeyServer running with IP `192.168.158.136` and port: `1443`. 
 - There are a certificate inside Redis DB with the following fingerprint: `5F7B426D0A57CE326711E33467BF436707E7169F`

Then you can generate a cURL command like this:

```
 curl -k -w "@curl-format.txt" -H "Content-Type: application/json" -X POST -d '{"protocol":"TLS 1.2", "method":"ECDHE", "spki":"5F7B426D0A57CE326711E33467BF436707E7169F", "hash":"SHA-256", "input":"BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ=="}' https://192.168.158.136:1443
```

**Note:** The curl-format-txt file is used for display the connection time data. 

For multiple request behind the same connection socket you have to put the KeyServer URL twice or more at the end of cURL command. For example, if you sends 3 requests behind the same socket, the cURL command should be:

```
 curl -k -w "@curl-format.txt" -H "Content-Type: application/json" -X POST -d '{"protocol":"TLS 1.2", "method":"ECDHE", "spki":"5F7B426D0A57CE326711E33467BF436707E7169F", "hash":"SHA-256", "input":"BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ=="}' https://192.168.158.136:1443 https://192.168.158.136:1443 https://192.168.158.136:1443
```