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
public class WritePhysicsEvents {
    
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
}
