package by.bsuir.deliveryservice.dao.impl;

import by.bsuir.deliveryservice.dao.OfficeDao;
import by.bsuir.deliveryservice.entity.Office;
import org.junit.Assert;
import org.junit.Test;

public class OfficeDaoImplTest extends BasicTest
{
    @Test
    public void testSelectExistingPrimaryOffice() throws Exception
    {
        OfficeDao od = OfficeDaoImpl.getSingleton();

        Office o = od.getPrimary();
        Assert.assertNotNull(o);

        System.out.println("ID: " + o.getId());
        System.out.println("Name: " + o.getName());
        System.out.println("Credentials: " + o.getCredentials());
    }
}
