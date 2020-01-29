package me.ling.kipfin.timetable.exceptions.timetable;

/**
 * Исключение когда нет предметов врасписании
 */
public class NoSubjectsException extends RuntimeException {
    public NoSubjectsException(String group, String date){
        super("Для группы " + group + " на дату " + date + " нет предметов!");
    }
    public NoSubjectsException(String group){
        super("Для группы " + group + " нет предметов!");
    }
}
