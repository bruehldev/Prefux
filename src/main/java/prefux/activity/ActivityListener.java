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
package prefux.activity;

import java.util.EventListener;

/**
 * Callback interface by which interested classes can be notified of
 * the progress of a scheduled activity.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface ActivityListener extends EventListener {

    /**
     * Called when an activity has been scheduled with an ActivityManager
     * @param a the scheduled Activity
     */
    public void activityScheduled(Activity a);
    
    /**
     * Called when an activity is first started.
     * @param a the started Activity
     */
    public void activityStarted(Activity a);
    
    /**
     * Called when an activity is stepped.
     * @param a the stepped Activity
     */
    public void activityStepped(Activity a);
    
    /**
     * Called when an activity finishes.
     * @param a the finished Activity
     */
    public void activityFinished(Activity a);
    
    /**
     * Called when an activity is cancelled.
     * @param a the cancelled Activity
     */
    public void activityCancelled(Activity a);
    
} // end of interface ActivityListener
