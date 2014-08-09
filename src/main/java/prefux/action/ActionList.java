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
package prefux.action;

import java.util.logging.Logger;

import prefux.Visualization;
import prefux.activity.Activity;
import prefux.util.StringLib;


/**
 * <p>The ActionList represents a chain of Actions that process VisualItems.
 * ActionList also implements the Action interface, so ActionLists can be placed
 * within other ActionList or {@link ActionSwitch} instances,
 * allowing recursive composition of different sets of Actions.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see prefux.activity.Activity
 * @see prefux.action.Action
 */
public class ActionList extends CompositeAction {

    private static final Logger s_logger = 
        Logger.getLogger(ActionList.class.getName());
    
    /**
     * Creates a new run-once ActionList.
     */
    public ActionList() {
        super(0);
    }
    
    /**
     * Creates a new run-once ActionList that processes the given
     * Visualization.
     * @param vis the {@link prefux.Visualization} to process.
     */
    public ActionList(Visualization vis) {
        super(vis);
    }
    
    /**
     * Creates a new ActionList of specified duration and default
     * step time of 20 milliseconds.
     * @param duration the duration of this Activity, in milliseconds
     */
    public ActionList(long duration) {
        super(duration, Activity.DEFAULT_STEP_TIME);
    }
    
    /**
     * Creates a new ActionList which processes the given Visualization
     * and has the specified duration and a default step time of 20
     * milliseconds.
     * @param vis the {@link prefux.Visualization} to process.
     * @param duration the duration of this Activity, in milliseconds
     */
    public ActionList(Visualization vis, long duration) {
        super(vis, duration);
    }
    
    /**
     * Creates a new ActionList of specified duration and step time.
     * @param duration the duration of this Activity, in milliseconds
     * @param stepTime the time to wait in milliseconds between executions
     *  of the action list
     */
    public ActionList(long duration, long stepTime) {
        super(duration, stepTime);
    }

    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        Object[] actions = m_actions.getArray();
        for ( int i=0; i<actions.length; ++i ) {
            Action a = (Action)actions[i];
            try {
                if ( a.isEnabled() ) a.run(frac);
            } catch ( Exception e ) {
                s_logger.warning(e.getMessage() + '\n'
                        + StringLib.getStackTrace(e));
            }
        }
    }

} // end of class ActionList
