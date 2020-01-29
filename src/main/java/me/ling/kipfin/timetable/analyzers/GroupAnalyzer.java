package me.ling.kipfin.timetable.analyzers;

import me.ling.kipfin.timetable.abstracts.AbstractAnalyzer;
import me.ling.kipfin.timetable.entities.ExtendedSubject;
import me.ling.kipfin.timetable.entities.TimetableMaster;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Анализатор расписания группы
 */
public class GroupAnalyzer extends AbstractAnalyzer<ExtendedSubject> {

    private final String group;

    /**
     * Конструктор
     *
     * @param group  - группа
     * @param master - масте
     */
    public GroupAnalyzer(String group, @NotNull TimetableMaster master) {
        super(master.getTimeInfo(), master.getGroupSubjects(group));
        this.group = group;
    }


    /**
     * Возвращает дисциплины
     *
     * @return - дисциплины
     */
    public List<ExtendedSubject> getSubjects() {
        return this.getObjects();
    }



    /**
     * Возвращает группу
     *
     * @return - группа
     */
    public String getGroup() {
        return group;
    }


}
