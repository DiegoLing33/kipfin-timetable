package me.ling.kipfin.timetable.analyzers;

import me.ling.kipfin.core.utils.ListUtils;
import me.ling.kipfin.exceptions.NotFoundEntityException;
import me.ling.kipfin.timetable.entities.ExtendedSubject;
import me.ling.kipfin.timetable.entities.Subject;
import me.ling.kipfin.timetable.entities.TimetableMaster;
import me.ling.kipfin.timetable.entities.timeinfo.TimeIndexes;
import me.ling.kipfin.timetable.entities.timeinfo.TimeInfo;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Анализатор расписания группы
 */
public class GroupAnalyzer {

    private final TimeInfo timeInfo;
    private final String group;
    private final List<ExtendedSubject> subjects;
    private final Integer firstIndex;
    private final Integer lastIndex;

    /**
     * Конструктор
     *
     * @param group  - группа
     * @param master - масте
     */
    public GroupAnalyzer(String group, @NotNull TimetableMaster master) {
        this.group = group;
        this.timeInfo = master.getTimeInfo();
        this.subjects = master.getGroupSubjects(group);
        this.firstIndex = this.subjects.get(0).getIndex();
        this.lastIndex = this.subjects.get(this.subjects.size() - 1).getIndex();

        Collections.sort(this.subjects);
    }


    /**
     * Возвращает дисциплины
     *
     * @return - дисциплины
     */
    public List<ExtendedSubject> getSubjects() {
        return subjects;
    }

    /**
     * Возвращает индекс первой пары
     *
     * @return - индекс первой пары
     */
    public Integer getFirstIndex() {
        return firstIndex;
    }

    /**
     * Возвращает индекс последней пары
     *
     * @return - индекс последней пары
     */
    public Integer getLastIndex() {
        return lastIndex;
    }

    /**
     * Возвращает true, если индекс находится в предметах
     * <p>
     * Например: протестировав индекс 0, можно узнать о наличии традиционной первой пары
     *
     * @param index - индекс
     * @return - результат выполнения
     */
    public boolean hasIndex(int index) {
        return ListUtils.contains(this.getSubjects(), Subject::getIndex, index);
    }

    /**
     * Возвращает информацию о ближайшей паре
     *
     * @param time - время
     * @return - результат TimeIndexes
     */
    public TimeIndexes getClosetInfo(LocalTime time) {
        int closetIndex = ListUtils.get(this.getSubjects(), s -> time.isBefore(this.timeInfo.get(s.getIndex()).getEndsTime())).getIndex();
        return new TimeIndexes(closetIndex, this.isEnded(time), this.isStarted(time));
    }

    /**
     * Возвращает true, если пары закончились
     *
     * @param time - временной промежуток
     * @return - результат
     */
    public boolean isEnded(@NotNull LocalTime time) {
        var item = this.timeInfo.get(this.getLastIndex()).getEndsTime();
        return time.isAfter(item);
    }

    /**
     * Возвращает true, если пары начались
     *
     * @param time - временной промежуток
     * @return - результат
     */
    public boolean isStarted(@NotNull LocalTime time) {
        var item = this.timeInfo.get(this.getFirstIndex()).getStartsTime();
        return time.isAfter(item);
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
     * Возвращает предмет по индексу
     *
     * @param index - индекс
     * @return - предмет
     * @throws NotFoundEntityException - исключение, если предмет не найден
     */
    public ExtendedSubject getSubjectByIndex(Integer index) throws NotFoundEntityException {
        return ListUtils.get(this.getSubjects(), Subject::getIndex, index);
    }
}
