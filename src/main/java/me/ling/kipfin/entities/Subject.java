package me.ling.kipfin.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Цисциплина
 */
public class Subject {

    @JsonProperty("subject_name")
    protected String title;

    @JsonProperty("subject_index")
    protected Integer index;

    public Subject() {
    }

    public Subject(String title, Integer index) {
        this.title = title;
        this.index = index;
    }

    /**
     * Возвращает название дисциплины
     *
     * @return - название дисциплины
     */
    public String getTitle() {
        return title;
    }

    /**
     * Возвращает номер дисциплины
     *
     * @return - номер дисциплины
     */
    public Integer getIndex() {
        return index;
    }
}
