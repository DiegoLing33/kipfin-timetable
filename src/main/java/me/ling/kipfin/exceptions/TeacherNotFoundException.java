package me.ling.kipfin.exceptions;

public class TeacherNotFoundException extends TimetableException {
    public TeacherNotFoundException(String teacherName) {
        super("Преподаватель [ " + teacherName + " ] не найден!");
    }
    public TeacherNotFoundException(Integer teacherInteger) {
        super("Преподаватель [ " + teacherInteger + " ] не найден!");
    }
}
