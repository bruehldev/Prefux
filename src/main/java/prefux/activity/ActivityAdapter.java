package prefux.activity;

/**
 * Adapter class for ActivityListeners. Provides empty implementations of
 * ActivityListener routines.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ActivityAdapter implements ActivityListener {

    /**
     * @see prefux.activity.ActivityListener#activityScheduled(prefux.activity.Activity)
     */
    public void activityScheduled(Activity a) {
    }

    /**
     * @see prefux.activity.ActivityListener#activityStarted(prefux.activity.Activity)
     */
    public void activityStarted(Activity a) {
    }

    /**
     * @see prefux.activity.ActivityListener#activityStepped(prefux.activity.Activity)
     */
    public void activityStepped(Activity a) {
    }

    /**
     * @see prefux.activity.ActivityListener#activityFinished(prefux.activity.Activity)
     */
    public void activityFinished(Activity a) {
    }

    /**
     * @see prefux.activity.ActivityListener#activityCancelled(prefux.activity.Activity)
     */
    public void activityCancelled(Activity a) {
    }

} // end of class ActivityAdapter
