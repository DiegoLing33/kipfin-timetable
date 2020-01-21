package me.ling.kipfin.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Classroom {

    @JsonProperty
    protected String who;

    @JsonProperty
    protected String where;

    @JsonProperty
    protected String group;

    @JsonProperty
    protected Integer index;

    public Classroom() {
    }

    public Classroom(String who, String where, String group, Integer index) {
        this.who = who;
        this.where = where;
        this.group = group;
        this.index = index;
    }

    /**
     * Возвращает преподавателя
     *
     * @return - преподаватель
     */
    public String getWho() {
        return who;
    }

    /**
     * Возвращает аудиторию
     *
     * @return - аудитория
     */
    public String getWhere() {
        return where;
    }

    /**
     * Возвращает группу
     *
     * @return - группа
     */
    public String getGroup() {
        return group;
    }

    /**
     * Возвращает индекс
     *
     * @return - индекс
     */
    public Integer getIndex() {
        return index;
    }
}
