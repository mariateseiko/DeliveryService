package by.bsuir.deliveryservice.service.impl;

//import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public abstract class ServiceTest {
    public static final Logger LOG =Logger.getLogger(ServiceTest.class);

   /* @BeforeClass
    public static void initializeConnectionPool() throws NamingException {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/delivery");
        bds.setUsername("root"); // The developers are insane, haha. >:]
        bds.setPassword("root");

        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache" +
                ".naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

        Context c = new InitialContext();
        try {
            c.createSubcontext("java:");
            c.createSubcontext("java:/comp");
            c.createSubcontext("java:/comp/env");
            c.createSubcontext("java:/comp/env/jdbc");
            c.bind("java:/comp/env/jdbc/delivery", bds);
        } catch (NamingException e) {
            LOG.warn("Already bound");
        }
    }*/
}
