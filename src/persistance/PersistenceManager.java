package persistance;

import business.entities.TeamPrint;
import persistance.json.exceptions.PersistanceException;
import java.util.List;

/**
 * Generic persistence manager that supports storing and retrieving objects.
 * Now supports TeamPrint in addition to other types.
 */
public abstract class PersistenceManager<T> {
    public abstract List<T> loadAll() throws PersistanceException;
    public abstract void save(List<T> data) throws PersistanceException;
}
