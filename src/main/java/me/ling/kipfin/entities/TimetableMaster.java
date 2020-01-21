package me.ling.kipfin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ling.kipfin.core.log.Logger;
import me.ling.kipfin.core.utils.DateUtils;
import me.ling.kipfin.core.utils.JsonUtils;
import me.ling.kipfin.database.GroupsDB;
import me.ling.kipfin.exceptions.TimetableException;
import me.ling.kipfin.parsing.ClassroomsExcelParser;
import me.ling.kipfin.parsing.WeekExcelParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimetableMaster {

    /**
     * Выполняет построение мастер-расписания
     *
     * @param classroomsFile - файл аудиторий
     * @param weekFile       - файл недели
     * @return - объект мастер-расписания
     * @throws IOException        - ошибки при чтении файлов для парсинга
     * @throws TimetableException - ошибки при парсинге
     */
    public static TimetableMaster create(String classroomsFile, String weekFile) throws IOException, TimetableException {
        ClassroomsExcelParser classroomsExcelParser = new ClassroomsExcelParser(classroomsFile);
        WeekExcelParser weekExcelParser = new WeekExcelParser(weekFile);

        var classrooms = classroomsExcelParser.start();
        var week = weekExcelParser.start();

        JsonUtils.saveJson(classrooms, "c.json");
        JsonUtils.saveJson(week, "w.json");

        var date = classroomsExcelParser.getDate();
        var dateString = DateUtils.toLocalDateString(date);
        var weekDayIndex = DateUtils.getLocalWeekDay(date);
        var weekNumber = DateUtils.getWeeksCount(DateUtils.fromLocalDateString("01.09.2019"), date);

        Logger.logAs("TMR Builder", "Debug: week size", week.size());
        Logger.logAs("TMR Builder", "Debug: today [", weekDayIndex, "] size:", week.get(weekDayIndex).values().size());

        DaySubjects<ExtendedSubject> daySubjects = new DaySubjects<>();
        GroupsDB.shared.getCache().values().forEach(group -> {
            List<Subject> subjectList = week.getSubjects(weekDayIndex, group);
            if (subjectList != null) {
                daySubjects.put(group, subjectList.stream().map(subject -> {
                    TeacherClassroomObjects tco = new TeacherClassroomObjects();
                    classrooms.getClassroomsByGroup(group).forEach(item -> {
                        if (item.getIndex().equals(subject.getIndex())) {
                            tco.put(item.getWho(), item.getWhere());
                        }
                    });
                    return new ExtendedSubject(subject.getTitle(), subject.getIndex(), tco);
                }).collect(Collectors.toCollection(ArrayList::new)));
            } else {
                daySubjects.put(group, new ArrayList<>());
            }
        });

        return new TimetableMaster(dateString, weekNumber, weekDayIndex, daySubjects, classrooms, week);
    }

    /**
     * Извлекает объект мастер-расписания из файла
     *
     * @param file - файл с мастер-расписанием
     * @return - объект масте-расписания
     * @throws IOException - исключения при работе с файлом
     */
    public static TimetableMaster open(String file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(file), TimetableMaster.class);
    }

    @JsonProperty
    protected String date;

    @JsonProperty("week_number")
    protected Integer weekNumber;

    @JsonProperty("week_day_index")
    protected Integer weekDayIndex;

    @JsonProperty
    protected DaySubjects<ExtendedSubject> timetable;

    @JsonProperty
    protected Classrooms classrooms;

    @JsonProperty
    protected WeekSubjects<Subject> week;

    public TimetableMaster() {
    }

    public TimetableMaster(String date, Integer weekNumber, Integer weekDayIndex, DaySubjects<ExtendedSubject> timetable,
                           Classrooms classrooms, WeekSubjects<Subject> week) {
        this.date = date;
        this.weekNumber = weekNumber;
        this.weekDayIndex = weekDayIndex;
        this.timetable = timetable;
        this.classrooms = classrooms;
        this.week = week;
    }

    /**
     * Возвращает дату расписания
     *
     * @return - строковая дата в формате дд.мм.гггг
     */
    public String getDate() {
        return date;
    }

    /**
     * Возвращает номер недели
     *
     * @return - номер недели
     */
    public Integer getWeekNumber() {
        return weekNumber;
    }

    /**
     * Возвращает расписание на дату TimetableMaster::date
     *
     * @return - расписание
     */
    public DaySubjects<ExtendedSubject> getTimetable() {
        return timetable;
    }

    /**
     * Возвращает расписание аудиторий на дату TimetableMaster::date
     *
     * @return - расписание аудиторий
     */
    public Classrooms getClassrooms() {
        return classrooms;
    }

    /**
     * Возвращает расписание на неделю
     *
     * @return - расписание на текущую неделю
     */
    public WeekSubjects<Subject> getWeek() {
        return week;
    }

    /**
     * Возвращает индекс дня недели
     *
     * @return - индекс дня недели
     */
    public Integer getWeekDayIndex() {
        return weekDayIndex;
    }

    /**
     * Возвращает true, если неделя четная
     *
     * @return - четность недели
     */
    @JsonIgnore
    public boolean isEven() {
        return this.getWeekNumber() % 2 > 0;
    }

    /**
     * Возвращает расписание для группы
     *
     * @param group - группа
     * @return - массив расписания
     */
    public List<ExtendedSubject> getGroupSubjects(String group) {
        return this.timetable.get(group);
    }
}
