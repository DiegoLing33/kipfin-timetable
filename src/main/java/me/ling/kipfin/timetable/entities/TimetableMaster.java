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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ling.kipfin.core.log.Logger;
import me.ling.kipfin.core.utils.DateUtils;
import me.ling.kipfin.core.utils.JsonUtils;
import me.ling.kipfin.database.university.GroupsDB;
import me.ling.kipfin.timetable.exceptions.timetable.NoSubjectsException;
import me.ling.kipfin.timetable.parsing.WeekExcelParser;
import me.ling.kipfin.timetable.parsing.ClassroomsExcelParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Мастер-расписание
 */
public class TimetableMaster {

    /**
     * Выполняет построение мастер-расписания
     *
     * @param classroomsBytes - байты аудиторий
     * @param weekBytes       - байты недели
     * @return - объект мастер-расписания
     * @throws IOException - ошибки при чтении файлов для парсинга
     */
    @NotNull
    public static TimetableMaster create(byte[] classroomsBytes, byte[] weekBytes) throws IOException {
        return TimetableMaster.create(classroomsBytes, weekBytes, null);
    }

    /**
     * Выполняет построение мастер-расписания
     *
     * @param classroomsBytes - байты аудиторий
     * @param weekBytes       - байты недели
     * @param timeInfo        - информация времени
     * @return - объект мастер-расписания
     * @throws IOException - ошибки при чтении файлов для парсинга
     */
    @NotNull
    public static TimetableMaster create(byte[] classroomsBytes, byte[] weekBytes, @Nullable TimeInfo timeInfo) throws IOException {
        ClassroomsExcelParser classroomsExcelParser = new ClassroomsExcelParser(classroomsBytes);
        WeekExcelParser weekExcelParser = new WeekExcelParser(weekBytes);
        return TimetableMaster.create(classroomsExcelParser, weekExcelParser, timeInfo);
    }

    /**
     * Выполняет построение мастер-расписания
     *
     * @param classroomsFile - файл аудиторий
     * @param weekFile       - файл недели
     * @return - объект мастер-расписания
     * @throws IOException - ошибки при чтении файлов для парсинга
     */
    @NotNull
    public static TimetableMaster create(String classroomsFile, String weekFile) throws IOException {
        return TimetableMaster.create(classroomsFile, weekFile, null);
    }

    /**
     * Выполняет построение мастер-расписания
     *
     * @param classroomsFile - файл аудиторий
     * @param weekFile       - файл недели
     * @param timeInfo       - информация времени
     * @return - объект мастер-расписания
     * @throws IOException - ошибки при чтении файлов для парсинга
     */
    @NotNull
    public static TimetableMaster create(String classroomsFile, String weekFile, @Nullable TimeInfo timeInfo) throws IOException {
        ClassroomsExcelParser classroomsExcelParser = new ClassroomsExcelParser(classroomsFile);
        WeekExcelParser weekExcelParser = new WeekExcelParser(weekFile);
        return TimetableMaster.create(classroomsExcelParser, weekExcelParser, timeInfo);
    }

    /**
     * Выполняет построение мастер-расписания
     *
     * @param classroomsExcelParser - парсер аудиторий
     * @param weekExcelParser       - парсер недели
     * @return - объект мастер-расписания
     * @throws IOException - ошибки при чтении файлов для парсинга
     */
    public static TimetableMaster create(ClassroomsExcelParser classroomsExcelParser, WeekExcelParser weekExcelParser, @Nullable TimeInfo timeInfo) throws IOException {
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
        GroupsDB.shared.getCache().values().forEach(universityGroup -> {
            String group = universityGroup.getTitle();
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

        return new TimetableMaster(dateString, weekNumber, weekDayIndex, daySubjects, classrooms, week, timeInfo);
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

    @JsonProperty("time_info")
    protected TimeInfo timeInfo;

    public TimetableMaster() {
    }

    public TimetableMaster(String date, Integer weekNumber, Integer weekDayIndex, DaySubjects<ExtendedSubject> timetable,
                           Classrooms classrooms, WeekSubjects<Subject> week) {
        this(date, weekNumber, weekDayIndex, timetable, classrooms, week, null);
    }

    public TimetableMaster(String date, Integer weekNumber, Integer weekDayIndex, DaySubjects<ExtendedSubject> timetable,
                           Classrooms classrooms, WeekSubjects<Subject> week, @Nullable TimeInfo timeInfo) {
        this.date = date;
        this.weekNumber = weekNumber;
        this.weekDayIndex = weekDayIndex;
        this.timetable = timetable;
        this.classrooms = classrooms;
        this.week = week;
        this.timeInfo = timeInfo;
        if (this.timeInfo == null)
            this.timeInfo = this.getTimeInfo();
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
        return this.getWeekNumber() % 2 != 0;
    }

    /**
     * Возвращает расписание для группы
     *
     * @param group - группа
     * @return - массив расписания
     * @throws NoSubjectsException - выбрасывает исключение, когда у группы нет предметов
     */
    @NotNull
    public List<ExtendedSubject> getGroupSubjects(String group) throws NoSubjectsException {

        var subjects = this.timetable.get(group);
        if (subjects.size() == 0) throw new NoSubjectsException(group, this.getDate());
        return subjects;
    }

    /**
     * Возвращает индекс первого предмета для группы
     *
     * @param group - группа
     * @return - индекс
     * @throws NoSubjectsException - исключение, когда у группы нет предметов в этот день
     */
    @NotNull
    public Integer getFirstSubjectIndex(String group) throws NoSubjectsException {
        int min = 999;
        for (ExtendedSubject subject : this.getGroupSubjects(group))
            if (subject.getIndex() < min) min = subject.getIndex();
        if (min == 999) throw new NoSubjectsException(group, this.getDate());
        return min;
    }

    /**
     * Возвращает индекс последнего предмета для группы
     *
     * @param group - группа
     * @return - индекс
     * @throws NoSubjectsException - исключение, когда у группы нет предметов в этот день
     */
    @NotNull
    public Integer getLastSubjectIndex(String group) throws NoSubjectsException {
        int max = -999;
        for (ExtendedSubject subject : this.getGroupSubjects(group))
            if (subject.getIndex() > max) max = subject.getIndex();
        if (max == -999) throw new NoSubjectsException(group, this.getDate());
        return max;
    }

    /**
     * Возвращает предмет по индексу
     *
     * @param group - группа
     * @param index - индекс
     * @return - предмет
     * @throws NoSubjectsException - у группы нет предметов
     */
    @NotNull
    public ExtendedSubject getGroupSubjectByIndex(String group, Integer index) throws NoSubjectsException {
        for (ExtendedSubject subject : this.getGroupSubjects(group)) {
            if (subject.getIndex().equals(index)) return subject;
        }
        throw new NoSubjectsException(group, this.getDate());
    }

    /**
     * Возвращает true, если расписание группы содержит индекс
     *
     * @param group - группа
     * @param index - индекс
     * @return - результат выполнения
     * @throws NoSubjectsException - исключение, когда у группы нет предметов в этот день
     */
    public boolean isGroupHasIndex(String group, Integer index) throws NoSubjectsException {
        var subjects = this.getGroupSubjects(group);
        if (subjects.size() == 0) throw new NoSubjectsException(group, this.getDate());
        return subjects.stream()
                .anyMatch(extendedSubject -> extendedSubject.getIndex().equals(index));
    }


    /**
     * Формирует расписание группы на неделю
     *
     * @param group - группа
     * @return - расписание группы на неделю
     */
    @JsonIgnore
    public List<WeekTimetable> getWeekTimetable(String group) {
        final int[] weekDayIndex = {0};
        return this.getWeek().getGroupWeek(group).stream().map(subjects -> new WeekTimetable(
                group,
                DateUtils.weekDaysNames[weekDayIndex[0]],
                weekDayIndex[0]++,
                subjects
        )).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Возвращает время
     *
     * @return -   время
     */
    @NotNull
    public TimeInfo getTimeInfo() {
        if (this.timeInfo == null) {
            if (this.weekDayIndex == 3) this.timeInfo = TimeInfo.getForthShort();
            else this.timeInfo = TimeInfo.getDefault();
        }
        return this.timeInfo;
    }
}
