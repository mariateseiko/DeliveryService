package by.bsuir.deliveryservice.dao.pool;

import manager.DatabaseManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A pool of {@link ProxyConnection} connections for database access. A thread-safe singleton.
 */
public class ConnectionPool {
    final static Logger LOG = Logger.getLogger(ConnectionPool.class);
    private BlockingQueue<ProxyConnection> pool;
    private static AtomicBoolean isNull = new AtomicBoolean(true);
    private AtomicBoolean isClosed = new AtomicBoolean(false);
    private static ConnectionPool instance;
    private static ReentrantLock lock = new ReentrantLock();

    private ConnectionPool() {
        int poolSize;
        Properties prop;
        try {
            poolSize = Integer.parseInt(DatabaseManager.getProperty("poolsize"));
            pool = new ArrayBlockingQueue<>(poolSize);
            prop = new Properties();
            prop.put("user", DatabaseManager.getProperty("user"));
            prop.put("password", DatabaseManager.getProperty("password"));
            prop.put("autoReconnect", DatabaseManager.getProperty("reconnect"));
            prop.put("characterEncoding", DatabaseManager.getProperty("encoding"));
            prop.put("useUnicode", DatabaseManager.getProperty("unicode"));
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (MissingResourceException e) {
            LOG.fatal("Connection pool can't be created due to configuration problems");
            throw new RuntimeException();
        } catch (SQLException e) {
            LOG.fatal("Connection pool can't be created due to SQL problems");
            throw new RuntimeException();
        }
        int count = 30;
        while(pool.size() != poolSize && count > 0) {
            try {
                pool.offer(new ProxyConnection(DriverManager.getConnection(DatabaseManager.getProperty("url"), prop)));
            } catch(SQLException e) {
                LOG.warn("Database connection couldn't be created");
            }
            count--;
        }
        if (pool.size() != poolSize) {
            throw new RuntimeException("Failed to create connection pool");
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    /**
     * Retrieves a connection from the pool's queue
     * @return a connection to the db, {@code null} if the pool is closed
     */
    public Connection takeConnection() {
        Connection connection = null;
        try {
            if (!isClosed.get()) {
                connection = pool.take();
            }
        } catch (InterruptedException e) {
            LOG.error("Error occurred while returning connection to the pool");
        }
        return connection;
    }

    /**
     * Returns connection back to the pool
     * @param connection connection to return
     * @return {@code true} if succeeded, {@code false} if the connection is null
     */
    public boolean returnConnection(ProxyConnection connection) {
        LOG.debug("Returning connection to the pool");
        if (connection != null) {
            pool.offer(connection);
            return true;
        }
        return false;
    }

    /**
     * Shuts down the pool. Marks the pool as closed, waits for a second for all connections to return and then tries
     * to close them all.
     */
    public void closePool()  {
        LOG.debug("Closing pool, pool size is " + pool.size());
        isClosed.set(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.error("Error occurred while closing the connection pool");
        }
        while (pool.size() > 0) {
            try {
                pool.take().destroy();
            } catch (InterruptedException | SQLException e) {
                LOG.error("Error occurred while destroying pool connection");
            }
        }
        LOG.debug("Pool closed");
    }
}
