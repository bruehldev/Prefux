/*  
 * Copyright (c) 2004-2013 Regents of the University of California.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of the University nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * Copyright (c) 2014 Martin Stockhammer
 */

package prefux.util.force;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manages a simulation of physical forces acting on bodies. To create a
 * custom ForceSimulator, add the desired {@link Force} functions and choose an
 * appropriate {@link Integrator}.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ForceSimulator {

    private ArrayList<ForceItem> items;
    private ArrayList<Spring> springs;
    private Force[] iforces;
    private Force[] sforces;
    private int iflen, sflen;
    private Integrator integrator;
    private float speedLimit = 1.0f;
    
    /**
     * Create a new, empty ForceSimulator. A RungeKuttaIntegrator is used
     * by default.
     */
    public ForceSimulator() {
        this(new RungeKuttaIntegrator());
    }

    /**
     * Create a new, empty ForceSimulator.
     * @param integr the Integrator to use
     */
    public ForceSimulator(Integrator integr) {
        integrator = integr;
        iforces = new Force[5];
        sforces = new Force[5];
        iflen = 0;
        sflen = 0;
        items = new ArrayList<>();
        springs = new ArrayList<>();
    }

    /**
     * Get the speed limit, or maximum velocity value allowed by this
     * simulator.
     * @return the "speed limit" maximum velocity value
     */
    public float getSpeedLimit() {
        return speedLimit;
    }
    
    /**
     * Set the speed limit, or maximum velocity value allowed by this
     * simulator.
     * @param limit the "speed limit" maximum velocity value to use
     */
    public void setSpeedLimit(float limit) {
        speedLimit = limit;
    }
    
    /**
     * Get the Integrator used by this simulator.
     * @return the Integrator
     */
    public Integrator getIntegrator() {
        return integrator;
    }
    
    /**
     * Set the Integrator used by this simulator.
     * @param intgr the Integrator to use
     */
    public void setIntegrator(Integrator intgr) {
        integrator = intgr;
    }
    
    /**
     * Clear this simulator, removing all ForceItem and Spring instances
     * for the simulator.
     */
    public void clear() {
        items.clear();
        Iterator<Spring> siter = springs.iterator();
        Spring.SpringFactory f = Spring.getFactory();
        while ( siter.hasNext() )
            f.reclaim(siter.next());
        springs.clear();
    }
    
    /**
     * Add a new Force function to the simulator.
     * @param f the Force function to add
     */
    public void addForce(Force f) {
        if ( f.isItemForce() ) {
            if ( iforces.length == iflen ) {
                // resize necessary
                Force[] newf = new Force[iflen+10];
                System.arraycopy(iforces, 0, newf, 0, iforces.length);
                iforces = newf;
            }
            iforces[iflen++] = f;
        }
        if ( f.isSpringForce() ) {
            if ( sforces.length == sflen ) {
                // resize necessary
                Force[] newf = new Force[sflen+10];
                System.arraycopy(sforces, 0, newf, 0, sforces.length);
                sforces = newf;
            }
            sforces[sflen++] = f;
        }
    }
    
    /**
     * Get an array of all the Force functions used in this simulator.
     * @return an array of Force functions
     */
    public Force[] getForces() {
        Force[] rv = new Force[iflen+sflen];
        System.arraycopy(iforces, 0, rv, 0, iflen);
        System.arraycopy(sforces, 0, rv, iflen, sflen);
        return rv;
    }
    
    /**
     * Add a ForceItem to the simulation.
     * @param item the ForceItem to add
     */
    public void addItem(ForceItem item) {
        items.add(item);
    }
    
    /**
     * Remove a ForceItem to the simulation.
     * @param item the ForceItem to remove
     */
    public boolean removeItem(ForceItem item) {
        return items.remove(item);
    }

    /**
     * Get an iterator over all registered ForceItems.
     * @return an iterator over the ForceItems.
     */
    public Iterator<ForceItem> getItems() {
        return items.iterator();
    }
    
    /**
     * Add a Spring to the simulation.
     * @param item1 the first endpoint of the spring
     * @param item2 the second endpoint of the spring
     * @return the Spring added to the simulation
     */
    public Spring addSpring(ForceItem item1, ForceItem item2) {
        return addSpring(item1, item2, -1.f, -1.f);
    }
    
    /**
     * Add a Spring to the simulation.
     * @param item1 the first endpoint of the spring
     * @param item2 the second endpoint of the spring
     * @param length the spring length
     * @return the Spring added to the simulation
     */
    public Spring addSpring(ForceItem item1, ForceItem item2, float length) {
        return addSpring(item1, item2, -1.f, length);
    }
    
    /**
     * Add a Spring to the simulation.
     * @param item1 the first endpoint of the spring
     * @param item2 the second endpoint of the spring
     * @param d the spring coefficient
     * @param e the spring length
     * @return the Spring added to the simulation
     */
    public Spring addSpring(ForceItem item1, ForceItem item2, double d, double e) {
        if ( item1 == null || item2 == null )
            throw new IllegalArgumentException("ForceItems must be non-null");
        Spring s = Spring.getFactory().getSpring(item1, item2, d, e);
        springs.add(s);
        return s;
    }
    
    /**
     * Get an iterator over all registered Springs.
     * @return an iterator over the Springs.
     */
    public Iterator<Spring> getSprings() {
        return springs.iterator();
    }
    
    /**
     * Run the simulator for one timestep.
     * @param timestep the span of the timestep for which to run the simulator
     */
    public long runSimulator(long timestep) {
        accumulate();
        return integrator.integrate(this, timestep);
    }
    
    /**
     * Accumulate all forces acting on the items in this simulation
     */
    public void accumulate() {
        for ( int i = 0; i < iflen; i++ )
            iforces[i].init(this);
        for ( int i = 0; i < sflen; i++ )
            sforces[i].init(this);
        Iterator<ForceItem> itemIter = items.iterator();
        while ( itemIter.hasNext() ) {
            ForceItem item = itemIter.next();
            item.force[0] = 0.0f; item.force[1] = 0.0f;
            for ( int i = 0; i < iflen; i++ )
                iforces[i].getForce(item);
        }
        Iterator<Spring> springIter = springs.iterator();
        while ( springIter.hasNext() ) {
            Spring s = springIter.next();
            for ( int i = 0; i < sflen; i++ ) {
                sforces[i].getForce(s);
            }
        }
    }
    
} // end of class ForceSimulator
