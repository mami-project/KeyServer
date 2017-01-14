# Open Source KeyServer 

* Download last version available:  [ ![Download](https://api.bintray.com/packages/jgm1986/KeyServer/KeyServer/images/download.svg) ](https://bintray.com/jgm1986/KeyServer/KeyServer/_latestVersion)

Repository status:

| | **Master**  | **Develop**   |
|:--- |:---:|:---:|
| **Build status** | [![Build Status](https://travis-ci.org/mami-project/KeyServer.svg?branch=master)](https://travis-ci.org/mami-project/KeyServer) | [![Build Status](https://travis-ci.org/mami-project/KeyServer.svg?branch=develop)](https://travis-ci.org/mami-project/KeyServer) |
| **Dependencies status** | [![Dependency Status](https://www.versioneye.com/user/projects/58457b180356f100336d0341/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58457b180356f100336d0341) | [![Dependency Status](https://www.versioneye.com/user/projects/58457b1603d153004d8ec98b/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58457b1603d153004d8ec98b) |
| **Code quality**|[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0ea08a24d820457fae1a921252965d3f)](https://www.codacy.com/app/jgm1986/KeyServer?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mami-project/KeyServer&amp;utm_campaign=Badge_Grade) [![Codacy Badge](https://api.codacy.com/project/badge/Coverage/0ea08a24d820457fae1a921252965d3f)](https://www.codacy.com/app/jgm1986/KeyServer?utm_source=github.com&utm_medium=referral&utm_content=jgm1986/KeyServer&utm_campaign=Badge_Coverage)| *`Not Available`* |

# Description

This software is a Key Server that implements the TLS Session Key Interface (SKI) defined in  [draft-cairns-tls-session-key-interface-01](https://tools.ietf.org/html/draft-cairns-tls-session-key-interface-01 "Session Key Interface (SKI) for TLS and DTLS").

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

KeyServer has been developed using Java SE 8 and Redis as database to store the certificates private keys. 

 - Java SE Runtime Environment 8: https://java.com/es/download
 - Redis: http://redis.io/download


## How to install?

You can download the latest KeyServer JAR version from [releases](https://github.com/mami-project/KeyServer/releases) section. If you prefer generate your own JAR, remember that this project is distributed with [Apache Maven](https://maven.apache.org/). 

Once the JAR file is downloaded/generated, move it to the desired folder. Configure the KeyServer using the [KeyServer Wiki](https://github.com/mami-project/KeyServer/wiki) steps before first execution. Otherwise, you'll get an error during the execution. 
