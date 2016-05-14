package by.bsuir.deliveryservice.entity;

import java.io.Serializable;

public class Office extends Entity implements Serializable
{
    private String name;
    private String credentials;

    public Office() {}
    public Office(String name, String credentials) {
        this.name = name;
        this.credentials = credentials;
    }
    // -----------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCredentials()
    {
        return credentials;
    }

    public void setCredentials(String credentials)
    {
        this.credentials = credentials;
    }
}
