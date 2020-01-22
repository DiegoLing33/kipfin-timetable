/*
 *
 *   ██████╗░██╗███████╗░██████╗░░█████╗░  ██╗░░░░░██╗███╗░░██╗░██████╗░
 *   ██╔══██╗██║██╔════╝██╔════╝░██╔══██╗  ██║░░░░░██║████╗░██║██╔════╝░
 *   ██║░░██║██║█████╗░░██║░░██╗░██║░░██║  ██║░░░░░██║██╔██╗██║██║░░██╗░
 *   ██║░░██║██║██╔══╝░░██║░░╚██╗██║░░██║  ██║░░░░░██║██║╚████║██║░░╚██╗
 *   ██████╔╝██║███████╗╚██████╔╝╚█████╔╝  ███████╗██║██║░╚███║╚██████╔╝
 *   ╚═════╝░╚═╝╚══════╝░╚═════╝░░╚════╝░  ╚══════╝╚═╝╚═╝░░╚══╝░╚═════╝░
 *
 *   Это программное обеспечение имеет лицензию, как это сказано в файле
 *   COPYING, который Вы должны были получить в рамках распространения ПО.
 *
 *   Использование, изменение, копирование, распространение, обмен/продажа
 *   могут выполняться исключительно в согласии с условиями файла COPYING.
 *
 *   Mail: diegoling33@gmail.com
 *
 */

package me.ling.kipfin.timetable.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WeekSubjects<T extends Subject> extends ArrayList<DaySubjects<T>> {
    public WeekSubjects(int initialCapacity) {
        super(initialCapacity);
    }

    public WeekSubjects() {
    }

    public WeekSubjects(@NotNull Collection<? extends DaySubjects<T>> c) {
        super(c);
    }

    /**
     * Возвращает предметы по дню недели
     *
     * @param dayIndex - индекс дня недели
     * @return - карта группа->дисциплины
     */
    @Nullable
    public DaySubjects<T> getSubjects(int dayIndex) {
        return this.get(dayIndex);
    }

    /**
     * Возвращает дисциплины по дню недели и группе
     *
     * @param dayIndex - индекс дня недели
     * @param group    - группа
     * @return - дисциплины
     */
    @Nullable
    public List<T> getSubjects(int dayIndex, String group) {
        return this.get(dayIndex).get(group);
    }

    /**
     * Возвращает неделю группы
     *
     * @param group - группа
     * @return - недельное расписание
     */
    public List<List<T>> getGroupWeek(String group) {
        return this.stream().map(tDaySubjects -> tDaySubjects.get(group))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
