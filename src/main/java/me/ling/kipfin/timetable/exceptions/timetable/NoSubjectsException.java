package me.ling.kipfin.timetable.exceptions.timetable;

import me.ling.kipfin.core.utils.DateUtils;

import java.time.LocalDate;

/**
 * Исключение когда нет предметов в расписании
 */
public class NoSubjectsException extends RuntimeException {
    private final String state;
    private final String date;

    public NoSubjectsException(String group, String date) {
        super("Для группы " + group + " на дату " + date + " нет предметов!");
        this.state = group;
        this.date = date;

    }

    public NoSubjectsException(String group) {
        super("Для группы " + group + " нет предметов!");
        this.state = group;
        this.date = DateUtils.toLocalDateString(LocalDate.now());
    }

    public String getState() {
        return state;
    }

    public String getDate() {
        return date;
    }
}
