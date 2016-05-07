package by.bsuir.deliveryservice.action.util;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class StringToDateConverter extends StrutsTypeConverter
{
    protected static final String DATE_FORMAT = "dd-MM-yyyy";
    protected DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass)
    {
        String val = values[0];

        try {
            return dateFormat.parse(val);
        } catch (ParseException e) {
            throw new TypeConversionException(
                    String.format("unable to convert a string ('%s') to " +
                            "date ('%s')", val, DATE_FORMAT),
                    e);
        }
    }

    @Override
    public String convertToString(Map context, Object date)
    {
        return (date != null && date instanceof Date) ?
                dateFormat.format(date) :
                null;
    }
}
