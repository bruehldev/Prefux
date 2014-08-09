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

/**
 * The ActionSwitch selects between a set of Actions, allowing only one
 * of a group of Actions to be executed at a time.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ActionSwitch extends CompositeAction {

    private int m_switchVal;
    
    /**
     * Creates an empty action switch.
     */
    public ActionSwitch() {
        m_switchVal = 0;
    }
    
    /**
     * Creates a new ActionSwitch with the given actions and switch value.
     * @param acts the Actions to include in this switch
     * @param switchVal the switch value indicating which Action to run
     */
    public ActionSwitch(Action[] acts, int switchVal) {
        for ( int i=0; i<acts.length; i++ )
            m_actions.add(acts[i]);
        setSwitchValue(switchVal);
    }
    
    /**
     * @see prefux.action.Action#run(double)
     */
    public void run(double frac) {
        if ( m_actions.size() > 0 ) {
            get(getSwitchValue()).run(frac);
        }
    }
    
    /**
     * Returns the current switch value, indicating the index of the Action
     * that will be executed in reponse to run() invocations.
     * @return the switch value
     */
    public int getSwitchValue() {
        return m_switchVal;
    }
    
    /**
     * Set the switch value. This is the index of the Action that will be
     * executed in response to run() invocations.
     * @param s the new switch value
     */
    public void setSwitchValue(int s) {
        if ( s < 0 || s >= size() )
            throw new IllegalArgumentException(
                    "Switch value out of legal range");
        m_switchVal = s;
    }

} // end of class ActionSwitch
