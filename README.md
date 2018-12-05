# HIPO-4 (High Performance Output)

The HIPO format is designed to be used with Nuclear Physics data.
It uses structured chunked data storage with indexed file footer for
fast random access. The file are compressed using LZ4 compression
algorithm, which provides very fast data throughput.

#Quickstart

To start using HIPO-4 library, include the following in your pom.xml file:

```html
<dependencies>
   <dependency>
     <groupId>org.jlab.jnp</groupId>
     <artifactId>jnp-hipo4</artifactId>
     <version>4.0-SNAPSHOT</version>
   </dependency>
 </dependencies>

 <repositories>
   <repository>
     <id>jhep-maven</id>
     <url>https://clasweb.jlab.org/jhep/maven</url>
   </repository>
 </repositories>
```
