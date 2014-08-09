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
