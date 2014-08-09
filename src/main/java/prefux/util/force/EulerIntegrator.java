package prefux.util.force;

import java.util.Iterator;

/**
 * Updates velocity and position data using Euler's Method. This is the
 * simplest and fastest method, but is somewhat inaccurate and less smooth
 * than more costly approaches.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see RungeKuttaIntegrator
 */
public class EulerIntegrator implements Integrator {
    
    /**
     * @see prefux.util.force.Integrator#integrate(prefux.util.force.ForceSimulator, long)
     */
    public long integrate(ForceSimulator sim, long timestep) {
        double speedLimit = sim.getSpeedLimit();
        Iterator iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem item = (ForceItem)iter.next();
            item.location[0] += timestep * item.velocity[0];
            item.location[1] += timestep * item.velocity[1];
            double coeff = timestep / item.mass;
            item.velocity[0] += coeff * item.force[0];
            item.velocity[1] += coeff * item.force[1];
            double vx = item.velocity[0];
            double vy = item.velocity[1];
            double v = (double)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                item.velocity[0] = speedLimit * vx / v;
                item.velocity[1] = speedLimit * vy / v;
            }
        }
        return timestep;
    }

} // end of class EulerIntegrator
