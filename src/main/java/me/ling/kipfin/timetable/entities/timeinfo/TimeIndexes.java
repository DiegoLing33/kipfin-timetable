package me.ling.kipfin.timetable.entities.timeinfo;

/**
 * Информационная связь индексов и времени
 */
public class TimeIndexes {

    private final Integer closetIndex;
    private final boolean isEnded;
    private final boolean isStarted;

    public TimeIndexes(Integer closetIndex, boolean isEnded, boolean isStarted) {
        this.closetIndex = closetIndex;
        this.isEnded = isEnded;
        this.isStarted = isStarted;
    }

    /**
     * Возвращает индекс ближайшей пары
     * @return  - индекс ближайшей пары
     */
    public Integer getClosetIndex() {
        return closetIndex;
    }

    /**
     * Возвращает true, если пары закончились
     * @return  - результат
     */
    public boolean isEnded() {
        return isEnded;
    }

    /**
     * Возвращает true, если пары начались
     * @return  - результат
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Возвращает true, если пары НЕ начались
     * @return  - результат
     */
    public boolean isNotStarted() {
        return !isStarted;
    }
}
