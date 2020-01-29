package me.ling.kipfin.timetable.entities.timeinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
     * Возвращает время начала в LocalTime
     * @return  - объект LocalTime
     */
    public LocalTime getStartsTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(this.getStarts(), formatter);
    }

    /**
     * Возвращает время окончания в LocalTime
     * @return  - объект LocalTime
     */
    public LocalTime getEndsTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(this.getEnds(), formatter);
    }

    /**
     * Возвращает true, если время в промежутке
     * @param time  - тестируемое время
     * @return  - результат тестирования
     */
    public boolean isTimeInRange(LocalTime time){
        LocalTime starts = this.getStartsTime();
        LocalTime ends = this.getEndsTime();
        return (time.isAfter(starts) || time.equals(starts)) &&
                (time.isBefore(ends));
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
