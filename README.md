# Open Source KeyServer

This software is a Key Server that implements the TLS Session Key Interface (SKI) defined in  [draft-cairns-tls-session-key-interface-00](https://tools.ietf.org/html/draft-cairns-tls-session-key-interface-00 "Session Key Interface (SKI) for TLS and DTLS").

The Heartbleed attack illustrated the security problems with storing private keys in the memory of the TLS server. The TLS Session Key Interface (SKI) defined the mentioned document makes it possible to store private keys in a highly trusted key server, physically separated from client facing servers. The TLS server is split into two distinct entities called
 Edge Server and Key Server that communicate over an encrypted and mutually authenticated channel using e.g.  TLS. This software implements the Key Server entity. 

```
  +--------+  Handshake  +-------------+    SKI    +------------+
  | Client | <---------> | Edge Server | <-------> | Key Server |
  +--------+             +-------------+           +------------+
```
It is aimed at being a functional prototype to test the LURK Architecture and the proposed interface. For more reference see [link URL][1] and [this][2].

   [1]: https://tools.ietf.org/html/draft-mglt-lurk-tls-requirements-00
   [2]: https://tools.ietf.org/html/draft-mglt-lurk-tls-abstract-api-00


## System requirements

KeyServer has been developed using Java SE 8 and Redis as database to store the private cetificates. 

 - Java SE Runtime Environment 8: https://java.com/es/download
 - Redis: http://redis.io/download


## How to install?

The KeyServer is distributed with maven. 

 Once the JAR file if generated, you only have to copy it on your desired folder.

Please take attention to the Wiki page (work in progress) about how to configure KeyServer tool and provide all necessary parameters for its correct execution.
