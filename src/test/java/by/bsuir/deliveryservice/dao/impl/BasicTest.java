package by.bsuir.deliveryservice.dao.impl;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.BeforeClass;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BasicTest
{
    @BeforeClass
    public static void initContext() throws NamingException
    {
        BasicDataSource bds = new BasicDataSource();

        /*
         * Insert from META-INF/context.xml
         */

        bds.setDriverClassName("com.mysql.jdbc.Driver");
        // ToDo: Should we use another database for testing purposes?
        bds.setUrl("jdbc:mysql://localhost:3306/delivery");
        bds.setUsername("root"); // The developers are insane, haha. >:]
        bds.setPassword("root");

        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache" +
                ".naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

        Context c = new InitialContext();

        c.createSubcontext("java:");
        c.createSubcontext("java:/comp");
        c.createSubcontext("java:/comp/env");
        c.createSubcontext("java:/comp/env/jdbc");

        c.bind("java:/comp/env/jdbc/delivery", bds);
    }
}
