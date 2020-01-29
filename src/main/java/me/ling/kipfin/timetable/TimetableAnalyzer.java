package me.ling.kipfin.timetable;

import me.ling.kipfin.timetable.entities.TimeInfo;
import me.ling.kipfin.timetable.entities.TimetableMaster;
import me.ling.kipfin.timetable.exceptions.timetable.NoSubjectsException;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Collections;

/**
 * Анализатор расписания
 */
public class TimetableAnalyzer {

    private final TimetableMaster master;
    private final TimeInfo timeInfo;

    /**
     * Конструктор
     *
     * @param master - мастер расписание
     */
    public TimetableAnalyzer(@NotNull TimetableMaster master) {
        this.master = master;
        this.timeInfo = master.getTimeInfo();
    }

    /**
     * Возвращает текущий предмет
     *
     * @param group - группа
     * @param time  - время
     * @return - индекс предмета или -1, если предметы еще не начались, 100 - если предметы уже закончились
     * @throws NoSubjectsException - выбрасывает исключение, когда у группы нет предметов
     */
    public Integer getCurrentIndexForGroup(String group, LocalTime time) throws NoSubjectsException {
        if (this.isTimeBeforeFirstSubject(group, time)) return -1;
        if (this.isTimeAfterLastSubject(group, time)) return 100;
        return this.getClosetIndexForGroup(group, time);
    }

    /**
     * Возвращает индекс ближайшего по времени предмета
     * @param group - группа
     * @param time  - время
     * @return  - индекс предмета
     * @throws NoSubjectsException  - у группы нет предметов в этот день
     */
    public Integer getClosetIndexForGroup(String group, LocalTime time) throws NoSubjectsException {
        var subjects = this.getMaster().getGroupSubjects(group);
        Collections.sort(subjects);
        for (var subject : subjects)
            if (time.isBefore(this.getMaster().getTimeInfoBySubject(subject).getEndsTime()))
                return subject.getIndex();
        return subjects.get(0).getIndex();
    }

    /**
     * Возвращает true, если время раньше первой пары
     *
     * @param group - группа
     * @param time  - время
     * @return - результат выполнния
     * @throws NoSubjectsException - выбрасывает исключение, когда у группы нет предметов
     */
    public boolean isTimeBeforeFirstSubject(String group, @NotNull LocalTime time) throws NoSubjectsException {
        var firstSubjectIndex = this.getMaster().getFirstSubjectIndex(group);
        return time.isBefore(this.getMaster().getTimeInfoByIndex(firstSubjectIndex).getStartsTime());
    }

    /**
     * Возвращает true, если время позже последней пары
     *
     * @param group - группа
     * @param time  - время
     * @return - результат выполнния
     * @throws NoSubjectsException - выбрасывает исключение, когда у группы нет предметов
     */
    public boolean isTimeAfterLastSubject(String group, @NotNull LocalTime time) throws NoSubjectsException {
        var lastSubjectIndex = this.getMaster().getLastSubjectIndex(group);
        return time.isAfter(this.getMaster().getTimeInfoByIndex(lastSubjectIndex).getEndsTime());
    }

    /**
     * Возвращает true, если время "ДО" дисциплины по индексу
     *
     * @param group - группа
     * @param index - индекс
     * @param time  - время
     * @return - результат
     * @throws NoSubjectsException - исключение, когда у группы нет предметов в этот день
     */
    public boolean isTimeBeforeIndex(String group, int index, @NotNull LocalTime time) throws NoSubjectsException {
        if (this.getMaster().isGroupHasIndex(group, index))
            return time.isBefore(this.timeInfo.get(index).getStartsTime());
        return false;
    }

    /**
     * Возвращает true, если время "ПОСЛЕ" дисциплины по индексу
     *
     * @param group - группа
     * @param index - индекс
     * @param time  - время
     * @return - результат
     * @throws NoSubjectsException - исключение, когда у группы нет предметов в этот день
     */
    public boolean isTimeAfterIndex(String group, int index, @NotNull LocalTime time) throws NoSubjectsException {
        if (this.getMaster().isGroupHasIndex(group, index))
            return time.isAfter(this.timeInfo.get(index).getStartsTime());
        return false;
    }

    /**
     * Возвращает мастер расписание
     *
     * @return - объект MasterTimetable
     */
    public TimetableMaster getMaster() {
        return master;
    }

    /**
     * Возвращает информацию о времени
     *
     * @return - информация о времени
     */
    public TimeInfo getTimeInfo() {
        return timeInfo;
    }
}
