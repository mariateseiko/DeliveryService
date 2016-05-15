/*
 * Copyright (c) 2016, Andrew Grivachevsky, Anastasiya Kostyukova,
 * Maria Teseiko
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package by.bsuir.deliveryservice.action.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class StringToDateConverterTest
{
    private final StringToDateConverter converter =
            new StringToDateConverter();

    private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Test
    public void testConvertFromString() throws Exception
    {
        Date srcDate = new Date();

        final String srcDateString = dateFormat.format(srcDate);

        Object dateObject = converter.convertFromString(null, new
                String[]{srcDateString}, null);

        Assert.assertNotNull(dateObject);
        Assert.assertTrue(dateObject instanceof Date);

        Date date = (Date) dateObject;

        Calendar srcCalendar = Calendar.getInstance();
        srcCalendar.setTime(srcDate);

        Calendar resCalendar = Calendar.getInstance();
        srcCalendar.setTime(date);

        Assert.assertEquals(srcCalendar.get(Calendar.DAY_OF_MONTH),
                resCalendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(srcCalendar.get(Calendar.MONTH),
                resCalendar.get(Calendar.MONTH));
        Assert.assertEquals(srcCalendar.get(Calendar.YEAR),
                resCalendar.get(Calendar.YEAR));
    }

    @Test
    public void testConvertToString() throws Exception
    {
        final Date srcDate = new Date();
        String result = converter.convertToString(null, srcDate);

        String expResult = dateFormat.format(srcDate);

        Assert.assertEquals(result, expResult);
    }
}
