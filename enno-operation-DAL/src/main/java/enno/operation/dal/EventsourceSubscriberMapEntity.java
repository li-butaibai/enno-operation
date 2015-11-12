package enno.operation.dal;

/**
 * Created by sabermai on 2015/11/13.
 */
public class EventsourceSubscriberMapEntity {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsourceSubscriberMapEntity that = (EventsourceSubscriberMapEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
