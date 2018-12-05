
<img src="https://github.com/gavalian/hipo4/blob/master/src/main/resources/hipo-logo-png.png" width="150">

# HIPO-4 (High Performance Output)

The HIPO format is designed to be used with Nuclear Physics data.
It uses structured chunked data storage with indexed file footer for
fast random access. The file are compressed using LZ4 compression
algorithm, which provides very fast data throughput.

# Quickstart

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

There aonly two dependencies that will be pulled in with this library, namely
LZ4 compression library and jnp-utils library. Otherwise, it's very light weight
library.

# Writing First File

First step in writing any file is defining the data structures that will be written
to the file. In HIPO library the data is described in JSON files, and is read
and parsed by the SchemaFactory class that is passed to writer object, so
writer will save the dictionary for reading. Here is an example of on object
JSON file (also in etc/ directory of distribution).

```json
[
  {
    "type"  : "bank",
    "name"  : "data::event",
    "group" : 2000,
    "item"  : 1,
    "entries":[
      {"name":"pid"     , "type":"I", "info":"particle LUND id"},
      {"name":"charge"  , "type":"B", "info":"particle charge"},
      {"name":"px"      , "type":"F", "info":"x-component of momenta"},
      {"name":"py"      , "type":"F", "info":"y-component of momenta"},
      {"name":"pz"      , "type":"F", "info":"z-component of momenta"},
      {"name":"vx"      , "type":"F", "info":"x-component of vertex"},
      {"name":"vy"      , "type":"F", "info":"y-component of vertex"},
      {"name":"vz"      , "type":"F", "info":"z-component of vertex"},
      {"name":"beta"    , "type":"F", "info":"x-component of vertex"},
      {"name":"chi2"    , "type":"F", "info":"pid assignment quality"},
      {"name":"status"  , "type":"I", "info":"status flag of the particle"}
    ]
  }
]
```
Banks are tabular structures, where every column in the bank has same number
of rows. While it is possible to write more complex objects, it's not yet
documented and we suggest users to stick to given examples.
Once we have the object (bank) definition of the file, one can write a file
with instances of this object. Here is a part of the code (also included
  in the distribution) that writes a new file with randomly generated values
  of object described above:

  ```java
  public static void main(String[] args){

        SchemaFactory schemaFactory = new SchemaFactory();
        schemaFactory.readFile("etc/physics.json");

        HipoWriter writer = new HipoWriter(schemaFactory);

        String outputFile = "myfirstData.hipo";

        writer.open(outputFile); // open output file, writes the dictionary
        Event event = new Event(); // create an event to fill with banks

        for(int i = 0; i < 100; i++){
            event.reset(); // clears the content of the event, ready to write banks
            Bank particles = new Bank(schemaFactory.getSchema("data::event"),6);
            WritePhysicsEvents.fillEvent(particles);
            event.write(particles);
            writer.addEvent(event); // write the event to the output
        }
        writer.close(); // close the file, write the file FOOTER
    }
  ```

  The full code is in the repository (including fillEvent routine), and is named
  "WritePhysicsEvents.java".

  # Reading First File
