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
package prefux.util.io;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * A basic username/password authenticator for use with HTTP-Auth.
 * The username or password can be reset for subsequent use as a different
 * user or on a different website.
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class PasswordAuthenticator extends Authenticator {

    private String m_username;
    private String m_password;
    private PasswordAuthentication m_auth;
    
    /**
     * Create a new password authenticator.
     * @param username the user name
     * @param password the password
     */
    PasswordAuthenticator(String username, String password) {
        this.m_password = password;
        this.m_username = username;
    }
    
    /**
     * Get the password.
     * @return the password
     */
    String getPassword() {
        return m_password;
    }

    /**
     * Set the password.
     * @return the password to use
     */
    void setPassword(String password) {
        this.m_password = password;
        this.m_auth = null;
    }

    /**
     * Get the user name.
     * @return the user name
     */
    String getUsername() {
        return m_username;
    }
    
    /**
     * Set the user name.
     * @return the user name to use
     */
    void setUsername(String username) {
        this.m_username = username;
        this.m_auth = null;
    }

    /**
     * Get the singleton PasswordAuthentication instance.
     * @return the PasswordAuthentication instance
     */
    protected PasswordAuthentication getPasswordAuthentication() {
        if ( m_auth == null ) {
            m_auth = new PasswordAuthentication(
                        m_username, m_password.toCharArray());
        }
        return m_auth;
    }

    // ------------------------------------------------------------------------
    
    /**
     * Creates a new PasswordAuthenticator for the given name and password and
     * sets it as the default Authenticator for use within Java networking.
     */
    public static void setAuthentication(String username, String password) {
        Authenticator.setDefault(new PasswordAuthenticator(username,password));
    }
    
} // end of class PasswordAuthenticator
