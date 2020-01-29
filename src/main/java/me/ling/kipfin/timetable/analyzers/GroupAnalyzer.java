package me.ling.kipfin.timetable.analyzers;

import me.ling.kipfin.timetable.entities.ExtendedSubject;
import me.ling.kipfin.timetable.entities.TimetableMaster;
import me.ling.kipfin.timetable.entities.timeinfo.TimeIndexes;
import me.ling.kipfin.timetable.entities.timeinfo.TimeInfo;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.List;

public class GroupAnalyzer {

    private final TimeInfo timeInfo;
    private final String group;
    private final SubjectsAnalyzer<ExtendedSubject> subjectsAnalyzer;
    private final List<ExtendedSubject> subjects;

    /**
     * Конструктор
     * @param group     - группа
     * @param master    - масте
     */
    public GroupAnalyzer(String group, @NotNull TimetableMaster master) {
        this.group = group;
        this.subjects = master.getGroupSubjects(group);
        this.timeInfo = master.getTimeInfo();
        this.subjectsAnalyzer = new SubjectsAnalyzer<>(this.subjects);
    }

    /**
     * Возвращает информацию о ближайшей паре
     * @param time  - время
     * @return  - результат TimeIndexes
     */
    public TimeIndexes getClosetInfo(LocalTime time){
        ExtendedSubject resultSubject = this.subjects.get(0);
        for (var subject : subjects)
            if (time.isBefore(this.timeInfo.get(subject.getIndex()).getEndsTime())){
                resultSubject = subject;
                break;
            }
        return new TimeIndexes(resultSubject.getIndex(), this.isEnded(time), this.isStarted(time));
    }

    /**
     * Возвращает true, если пары закончились
     * @param time  - временной промежуток
     * @return  - результат
     */
    public boolean isEnded(@NotNull LocalTime time){
        var item = this.timeInfo.get(this.subjectsAnalyzer.getLastIndex()).getEndsTime();
        return time.isAfter(item);
    }

    /**
     * Возвращает true, если пары начались
     * @param time  - временной промежуток
     * @return  - результат
     */
    public boolean isStarted(@NotNull LocalTime time){
        var item = this.timeInfo.get(this.subjectsAnalyzer.getFirstIndex()).getStartsTime();
        return time.isAfter(item);
    }

    /**
     * Возвращает группу
     * @return  - группа
     */
    public String getGroup() {
        return group;
    }

    /**
     * Возвращает анализатор предметов
     * @return  - анализатор предметов
     */
    public SubjectsAnalyzer<ExtendedSubject> getSubjectsAnalyzer() {
        return subjectsAnalyzer;
    }
}
