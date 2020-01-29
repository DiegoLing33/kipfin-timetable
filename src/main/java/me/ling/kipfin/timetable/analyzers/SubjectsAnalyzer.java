package me.ling.kipfin.timetable.analyzers;

import me.ling.kipfin.timetable.entities.Subject;
import me.ling.kipfin.timetable.entities.timeinfo.TimeIndexes;
import me.ling.kipfin.timetable.entities.timeinfo.TimeInfo;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Анализатор предметов
 * @param <T>
 */
public class SubjectsAnalyzer<T extends Subject> {

    private final List<T> subjects;
    private final Integer firstIndex;
    private final Integer lastIndex;

    /**
     * Конструктор
     * @param subjects  - цисциплины
     */
    public SubjectsAnalyzer(List<T> subjects) {
        this.subjects = subjects;
        Collections.sort(this.subjects);

        this.firstIndex = this.subjects.get(0).getIndex();
        this.lastIndex = this.subjects.get(this.subjects.size()-1).getIndex();
    }

    /**
     * Возвращает дисциплины
     * @return  - дисциплины
     */
    public List<T> getSubjects() {
        return subjects;
    }

    /**
     * Возвращает индекс первой пары
     * @return  - индекс первой пары
     */
    public Integer getFirstIndex() {
        return firstIndex;
    }

    /**
     * Возвращает индекс последней пары
     * @return  - индекс последней пары
     */
    public Integer getLastIndex() {
        return lastIndex;
    }

    /**
     * Возвращает true, если индекс находится в предметах
     *
     * Например: протестировав индекс 0, можно узнать о наличии традиционной первой пары
     *
     * @param index - индекс
     * @return  - результат выполнения
     */
    public boolean hasIndex(int index){
        return this.subjects.stream().anyMatch(s -> s.getIndex().equals(index));
    }
}
