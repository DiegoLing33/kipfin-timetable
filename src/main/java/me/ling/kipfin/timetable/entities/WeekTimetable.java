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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Расписание на неделю
 */
public class WeekTimetable {

    @JsonProperty("group")
    private String groupTitle;

    @JsonProperty("week_day_name")
    private String weekDayName;

    @JsonProperty("week_day_index")
    private Integer weekDayIndex;

    @JsonProperty
    private List<Subject> subjects;

    public WeekTimetable() {
    }

    public WeekTimetable(String groupTitle, String weekDayName, Integer weekDayIndex, List<Subject> subjects) {
        this.groupTitle = groupTitle;
        this.weekDayName = weekDayName;
        this.weekDayIndex = weekDayIndex;
        this.subjects = subjects;
    }

    /**
     * Возвращает название группы
     *
     * @return - название группы
     */
    public String getGroupTitle() {
        return groupTitle;
    }

    /**
     * Возвращает название дня недели
     *
     * @return - день недели
     */
    public String getWeekDayName() {
        return weekDayName;
    }

    /**
     * Возвращает индекс дня недели
     *
     * @return - индекс дня недели 0...6
     */
    public Integer getWeekDayIndex() {
        return weekDayIndex;
    }

    /**
     * Возвращает список дисциплин
     *
     * @return - список дисциплин
     */
    public List<Subject> getSubjects() {
        return subjects;
    }
}
