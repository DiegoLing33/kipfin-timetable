package me.ling.kipfin.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Расширенная сущность дисциплины
 */
public class ExtendedSubject extends Subject {

    @JsonProperty("subject_who")
    protected TeacherClassroomObjects who = new TeacherClassroomObjects();


    public ExtendedSubject() {
    }

    public ExtendedSubject(String title, Integer index, TeacherClassroomObjects who) {
        super(title, index);
        this.who = who;
    }

    /**
     * Возвращает связку Преподаватель->аудитория
     *
     * @return - связка
     */
    public TeacherClassroomObjects getWho() {
        return who;
    }
}
