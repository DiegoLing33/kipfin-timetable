package me.ling.kipfin.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * День предметов
 *
 * @param <T>
 */
public class DaySubjects<T extends Subject> extends HashMap<String, List<T>> {
    public DaySubjects(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public DaySubjects(int initialCapacity) {
        super(initialCapacity);
    }

    public DaySubjects() {
    }

    public DaySubjects(Map<? extends String, ? extends List<T>> m) {
        super(m);
    }
}
