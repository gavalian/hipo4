/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.hipo4;

import org.jlab.jnp.hipo4.data.Bank;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.data.SchemaFactory;
import org.jlab.jnp.hipo4.io.HipoWriter;

/**
 *
 * @author gavalian
 */
public class WriteDetectorData {
    
    public static void fillEvent(Bank particles){
        int rows = particles.getRows();
        for(int r = 0; r < rows; r++){
            int   pid =  ((int) (Math.random()*Integer.MAX_VALUE))%15;
            float  px = (float) (Math.random()*2.0);
            float  py = (float) (Math.random()*2.0);
            float  pz = (float) (Math.random()*2.0);
            particles.putInt("pid", r, pid);
            particles.putFloat("px", r, px);
            particles.putFloat("py", r, py);
            particles.putFloat("pz", r, pz);
            particles.putFloat("vx", r, (float) (Math.random()*0.1));
            particles.putFloat("vy", r, (float) (Math.random()*0.1));
            particles.putFloat("vz", r, (float) (Math.random()*3.5));
        }
    }
    
    public static void fillDetector(Bank detector, int nparticles){
        int rows = detector.getRows();
        
        for(int r = 0; r < rows; r++){
        
            int         id =  ((int) (Math.random()*Integer.MAX_VALUE))%15;
            int     pindex =  ((int) (Math.random()*Integer.MAX_VALUE))%nparticles;
            float        x = (float) (Math.random()*2.0);
            float        y = (float) (Math.random()*2.0);
            float        z = (float) (Math.random()*2.0);
            float     time = (float) (Math.random()*72.0);
            float   energy = (float) (Math.random()*36.0);
            
            detector.putShort(     "id", r, (short) id);
            detector.putByte(  "pindex", r, (byte) pindex);
            detector.putFloat(      "x", r, x);
            detector.putFloat(      "y", r, y);
            detector.putFloat(      "z", r, z);
            detector.putFloat(   "time", r, time);
            detector.putFloat(   "energy", r, energy);
            
        }
    }
    
    public static void main(String[] args){
        
        SchemaFactory schemaFactory = new SchemaFactory();
        schemaFactory.readFile("etc/detector.json");
        
        HipoWriter writer = new HipoWriter(schemaFactory);
        
        String outputFile = "detectorData.hipo";
        
        writer.open(outputFile); // open output file, writes the dictionary        
        Event event = new Event(); // create an event to fill with banks
        
        for(int i = 0; i < 20; i++){
            
            event.reset(); // clears the content of the event, ready to write banks
            Bank particles = new Bank(schemaFactory.getSchema("data::event"),6);
            Bank detectors = new Bank(schemaFactory.getSchema("data::detector"),25);
            
            WriteDetectorData.fillEvent(    particles);
            WriteDetectorData.fillDetector( detectors,6);
            
            event.write(particles);
            event.write(detectors);
            
            writer.addEvent(event); // write the event to the output
        }
        
        writer.close(); // close the file, write the file FOOTER
    }
    
}
