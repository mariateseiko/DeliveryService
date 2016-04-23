package by.bsuir.deliveryservice.entity;

/**
 * Abstract class {@code Entity} is the root for id-specified classes.
 */
public abstract class Entity {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
