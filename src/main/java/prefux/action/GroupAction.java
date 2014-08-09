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

import prefux.Visualization;

/**
 * An Action that can be parameterized to process a particular group of items.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class GroupAction extends Action {

    /** A reference to the group to be processed by this Action */
    protected String m_group;
    
    /**
     * Create a new GroupAction that processes all groups.
     * @see prefux.Visualization#ALL_ITEMS
     */
    public GroupAction() {
        this((Visualization)null);
    }

    /**
     * Create a new GroupAction that processes all groups.
     * @param vis the {@link prefux.Visualization} to process
     * @see prefux.Visualization#ALL_ITEMS
     */
    public GroupAction(Visualization vis) {
        this(vis, Visualization.ALL_ITEMS);
    }
    
    /**
     * Create a new GroupAction that processes all groups.
     * @param vis the {@link prefux.Visualization} to process
     * @param duration the duration of this Action
     * @see prefux.Visualization#ALL_ITEMS
     */
    public GroupAction(Visualization vis, long duration) {
        this(vis, Visualization.ALL_ITEMS, duration);
    }
    
    /**
     * Create a new GroupAction that processes all groups.
     * @param vis the {@link prefux.Visualization} to process
     * @param duration the duration of this Action
     * @param stepTime the time to wait between invocations of this Action
     * @see prefux.Visualization#ALL_ITEMS
     */
    public GroupAction(Visualization vis, long duration, long stepTime) {
        this(vis, Visualization.ALL_ITEMS, duration, stepTime);
    }
    
    /**
     * Create a new GroupAction that processes the specified group.
     * @param group the name of the group to process
     */
    public GroupAction(String group) {
        this(null, group);
    }
    
    /**
     * Create a new GroupAction that processes the specified group.
     * @param group the name of the group to process
     * @param duration the duration of this Action
     */
    public GroupAction(String group, long duration) {
        this(null, group, duration);
    }
    
    /**
     * Create a new GroupAction that processes the specified group.
     * @param group the name of the group to process
     * @param duration the duration of this Action
     * @param stepTime the time to wait between invocations of this Action
     */
    public GroupAction(String group, long duration, long stepTime) {
        this(null, group, duration, stepTime);
    }
    
    /**
     * Create a new GroupAction that processes the specified group.
     * @param vis the {@link prefux.Visualization} to process
     * @param group the name of the group to process
     */
    public GroupAction(Visualization vis, String group) {
        super(vis);
        m_group = group;
    }
    
    /**
     * Create a new GroupAction that processes the specified group.
     * @param vis the {@link prefux.Visualization} to process
     * @param group the name of the group to process
     * @param duration the duration of this Action
     */
    public GroupAction(Visualization vis, String group, long duration) {
        super(vis, duration);
        m_group = group;
    }
    
    /**
     * Create a new GroupAction that processes the specified group.
     * @param vis the {@link prefux.Visualization} to process
     * @param group the name of the group to process
     * @param duration the duration of this Action
     * @param stepTime the time to wait between invocations of this Action
     */
    public GroupAction(Visualization vis, String group,
                       long duration, long stepTime)
    {
        super(vis, duration, stepTime);
        m_group = group;
    }

    // ------------------------------------------------------------------------
    
    /**
     * Get the name of the group to be processed by this Action.
     * @return the name of the group to process
     */
    public String getGroup() {
        return m_group;
    }

    /**
     * Sets the name of the group to be processed by this Action.
     * @param group the name of the group to process
     */
    public void setGroup(String group) {
        m_group = group;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public abstract void run(double frac);

} // end of class GroupAction
