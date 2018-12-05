
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

After you have a new file created, you can read objects from it. The object
descriptions come from the file, it keeps dictionary of objects that were written.
Here is the example program reading the file created in the previous section
(for full code with includes look at src/main/java/org/jlab/hipo4/ReadPhysicsEvent.java):

```java
public class ReadPhysicsEvent {

    public static void main(String[] args){

        HipoReader reader = new HipoReader();
        reader.open("myfirstData.hipo");

        Schema particlesSchema = reader.getSchemaFactory().getSchema("data::event");

        Bank particles = new Bank(particlesSchema); // creates a bank for reading in.
        Event    event = new Event();

        while(reader.hasNext()==true){
            reader.nextEvent(event);
            event.read(particles);

            System.out.println(particles.nodeString());
        }
    }
}
```

The printout will look something like this:

```bash
reader:: *****>>>>> openning file : myfirstData.hipo
reader:: ***** dictionary entries :            1
reader:: ***** number of  records :            1
reader:: ***** number of  events  :          100
           pid :          3         0         3        12         7         2
        charge :          0         0         0         0         0         0
            px :     1.5671    0.3957    1.5059    0.1059    0.5070    1.6558
            py :     1.2131    0.2368    0.1146    0.1316    1.1042    1.7051
            pz :     0.2896    0.1184    1.1606    1.2504    0.8243    0.5830
            vx :     0.0168    0.0409    0.0581    0.0615    0.0901    0.0283
            vy :     0.0301    0.0079    0.0740    0.0545    0.0222    0.0789
            vz :     3.4132    1.4249    3.1226    2.3459    1.6301    3.0557
          beta :     0.0000    0.0000    0.0000    0.0000    0.0000    0.0000
          chi2 :     0.0000    0.0000    0.0000    0.0000    0.0000    0.0000
        status :          0         0         0         0         0         0

           pid :         14        12         2         9        13        12
        charge :          0         0         0         0         0         0
            px :     1.1114    0.8347    1.3506    0.5111    1.7776    1.5834
            py :     1.1189    0.0071    1.5518    1.7602    1.0176    0.0376
            pz :     0.2778    1.5869    0.9217    1.6153    0.0597    0.3919
            vx :     0.0707    0.0068    0.0994    0.0572    0.0857    0.0869
            vy :     0.0720    0.0317    0.0561    0.0047    0.0633    0.0996
            vz :     1.4709    1.5315    1.0431    0.5665    3.1120    0.8961
          beta :     0.0000    0.0000    0.0000    0.0000    0.0000    0.0000
          chi2 :     0.0000    0.0000    0.0000    0.0000    0.0000    0.0000
        status :          0         0         0         0         0         0
```

# Accessing Bank Information

For more complex example on how to read the data stored in the bank, and how
to write dependent bank and pase it look at the codes:

```bash
src/main/java/org/jlab/hipo4/WriteDetectorData.java
src/main/java/org/jlab/hipo4/ReadDetectorData.java
```
