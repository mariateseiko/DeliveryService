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
