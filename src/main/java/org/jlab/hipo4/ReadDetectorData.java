/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.hipo4;

import org.jlab.jnp.hipo4.data.Bank;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.data.Schema;
import org.jlab.jnp.hipo4.io.HipoReader;

/**
 *
 * @author gavalian
 */
public class ReadDetectorData {
    public static void main(String[] args){
        
        HipoReader reader = new HipoReader();
        reader.open("detectorData.hipo");
        
        Schema particlesSchema = reader.getSchemaFactory().getSchema("data::event");
        Schema detectorsSchema = reader.getSchemaFactory().getSchema("data::detector");
        
        Bank  particles = new Bank(particlesSchema); // creates a bank for reading in.
        Bank  detectors = new Bank(detectorsSchema); // creates a bank for reading in.
        Event     event = new Event();
        int eventNumber = 0;
        
        while(reader.hasNext()==true){
            
            eventNumber++;
            
            reader.nextEvent(event);
            event.read(particles);
            event.read(detectors);
            
            int prows = particles.getRows();
            int drows = detectors.getRows();
            System.out.println("------- event # " + eventNumber);
            for(int p = 0; p < prows; p++){
                int   pid = particles.getInt(   "pid", p);
                float  px = particles.getFloat(  "px", p);
                float  py = particles.getFloat(  "py", p);
                float  pz = particles.getFloat(  "pz", p);
                
                double  mom = Math.sqrt(px*px+py*py+pz*pz);
                System.out.println("pid = " + pid + " mom = " + mom);
                for(int d = 0; d < drows; d++){
                    int index = (int) detectors.getByte("pindex", d);                    
                    if(index==p){
                        float time   = detectors.getFloat("time",d);
                        float energy = detectors.getFloat("energy",d);
                        float      x = detectors.getFloat("x",d);
                        float      y = detectors.getFloat("y",d);
                        float      z = detectors.getFloat("z",d);
                        System.out.println(String.format("\t (%8.3f,%8.3f,%8.3f) time = %8.3f, energy = %8.3f ", 
                                x,y,z,time,energy));
                    }
                }
            }
            //System.out.println(particles.nodeString());
        }
    }
}
