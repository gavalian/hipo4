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
