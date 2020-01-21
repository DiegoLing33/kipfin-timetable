package me.ling.kipfin.exceptions;

public class TimetableException extends RuntimeException {
    public TimetableException(){
        super("Во время работы с расписанием произошла ошибка!");
    }
    public TimetableException(String message) {
        super(message);
    }
}
