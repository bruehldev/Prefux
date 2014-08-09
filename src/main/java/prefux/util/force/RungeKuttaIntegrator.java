package prefux.util.force;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Updates velocity and position data using the 4th-Order Runge-Kutta method.
 * It is slower but more accurate than other techniques such as Euler's Method.
 * The technique requires re-evaluating forces 4 times for a given timestep.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class RungeKuttaIntegrator implements Integrator {
	
	Logger log = LoggerFactory.getLogger(RungeKuttaIntegrator.class);
	
	// These are upper and lower bounds for the q-Factor, that is
	// calculated by the Runge-Kutta coefficients.
	// They are used to ensure numerical stability.
	private static final double Q_MAX = 5.0;
	private static final double Q_MIN = 0.2;
    
    /**
     * @see prefux.util.force.Integrator#integrate(prefux.util.force.ForceSimulator, long)
     */
    public long integrate(ForceSimulator sim, long timestep) {
        double speedLimit = sim.getSpeedLimit();
        double vx, vy, v, coeff;
        double[][] k, l;
        
        Iterator<ForceItem> iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem item = iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            item.plocation[0] = item.location[0];
            item.plocation[1] = item.location[1];
            k[0][0] = timestep*item.velocity[0];
            k[0][1] = timestep*item.velocity[1];
            l[0][0] = coeff*item.force[0];
            l[0][1] = coeff*item.force[1];
        
            // Set the position to the new predicted position
            item.location[0] += 0.5*k[0][0];
            item.location[1] += 0.5*k[0][1];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem item = (ForceItem)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            vx = item.velocity[0] + .5*l[0][0];
            vy = item.velocity[1] + .5*l[0][1];
            v = (double)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[1][0] = timestep*vx;
            k[1][1] = timestep*vy;
            l[1][0] = coeff*item.force[0];
            l[1][1] = coeff*item.force[1];
        
            // Set the position to the new predicted position
            item.location[0] = item.plocation[0] + 0.5*k[1][0];
            item.location[1] = item.plocation[1] + 0.5*k[1][1];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem item = (ForceItem)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            vx = item.velocity[0] + .5*l[1][0];
            vy = item.velocity[1] + .5*l[1][1];
            v = (double)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[2][0] = timestep*vx;
            k[2][1] = timestep*vy;
            l[2][0] = coeff*item.force[0];
            l[2][1] = coeff*item.force[1];
        
            // Set the position to the new predicted position
            item.location[0] = item.plocation[0] + 0.5*k[2][0];
            item.location[1] = item.plocation[1] + 0.5*k[2][1];
        }
        
        // recalculate forces
        sim.accumulate();
        // q-Factor is calculated by the coefficients of the Runge-Kutta calculation
        // SQRT(SUM over all [(k3-k2)/(k2-k1)]^2)/nodes
        
        double q=0.0;
        int items=0;
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem item = (ForceItem)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            double[] p = item.plocation;
            vx = item.velocity[0] + l[2][0];
            vy = item.velocity[1] + l[2][1];
            v = (double)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[3][0] = timestep*vx;
            k[3][1] = timestep*vy;
            l[3][0] = coeff*item.force[0];
            l[3][1] = coeff*item.force[1];
            item.location[0] = p[0] + (k[0][0]+k[3][0])/6.0 + (k[1][0]+k[2][0])/3.0;
            item.location[1] = p[1] + (k[0][1]+k[3][1])/6.0 + (k[1][1]+k[2][1])/3.0;
            double qx = (k[2][0]- k[1][0])/(k[1][0]-k[0][0]);
            double qy = (k[2][1]- k[1][1])/(k[1][1]-k[0][1]);
            q+=qx*qx+qy*qx;
            items++;
            vx = (l[0][0]+l[3][0])/6.0 + (l[1][0]+l[2][0])/3.0;
            vy = (l[0][1]+l[3][1])/6.0 + (l[1][1]+l[2][1])/3.0;
            v = (double)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            item.velocity[0] += vx;
            item.velocity[1] += vy;
        }
        q=Math.sqrt(q)/items;
        if (timestep==0 || q==Double.NaN) {
        	return 20;
        } else if (q>5.0) {
        	return (long)(timestep*.8);
        } else if (q<0.2) {
        	return (long)(timestep*1.2);
        }
        //log.debug("Q-Factor: "+q+" "+timestep);
        return timestep;
    }

} // end of class RungeKuttaIntegrator
