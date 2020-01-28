package me.ling.kipfin.timetable.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Элемент информации о времени
 */
public class TimeInfoItem {
    @JsonProperty
    protected String starts;
    @JsonProperty
    protected String ends;

    public TimeInfoItem(String starts, String ends) {
        this.starts = starts;
        this.ends = ends;
    }

    public TimeInfoItem() {
    }

    /**
     * Возвращает время начала
     * @return  - начало
     */
    public String getStarts() {
        return starts;
    }

    /**
     * Возвращает время конца
     * @return - конец
     */
    public String getEnds() {
        return ends;
    }

    /**
     * Преобразует объект в строку
     * @return  - Формат строки "starts - ends"
     */
    @Override
    public String toString() {
        return String.format("%s - %s", starts, ends);
    }
}
