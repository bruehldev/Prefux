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
package prefux.data.parser;

import java.text.DateFormat;

/**
 * DataParser instance that parses Date values as java.util.Date instances,
 * representing a particular date and time. This class uses a backing
 * {@link java.text.DateFormat} instance to perform parsing. The DateFormat
 * instance to use can be passed in to the constructor, or by default the
 * DateFormat returned by
 * {@link java.text.DateFormat#getDateTimeInstance(int, int)} with both
 * arguments being {@link java.text.DateFormat#SHORT} is used.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DateTimeParser extends DateParser {

    /**
     * Create a new DateTimeParser.
     */
    public DateTimeParser() {
        this(new DateFormat[]{DateFormat.getDateTimeInstance(
            DateFormat.SHORT, DateFormat.SHORT)});
    }

    /**
     * Create a new DateTimeParser.
     *
     * @param dateFormat the DateFormat instance to use for parsing
     */
    public DateTimeParser(DateFormat[] dateFormats) {
        super(dateFormats);
    }
} // end of class DateTimeParser
