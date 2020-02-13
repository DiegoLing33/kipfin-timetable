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

package me.ling.kipfin.timetable;

import me.ling.kipfin.abstracts.Indexable;
import me.ling.kipfin.core.utils.DateUtils;
import me.ling.kipfin.timetable.abstracts.AbstractAnalyzer;
import me.ling.kipfin.timetable.analyzers.GroupAnalyzer;
import me.ling.kipfin.timetable.analyzers.TeacherAnalyzer;
import me.ling.kipfin.timetable.entities.TimetableMaster;
import me.ling.kipfin.timetable.enums.StudentDayMomentState;
import me.ling.kipfin.timetable.enums.StudentDayState;
import me.ling.kipfin.timetable.managers.TimetableManager;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Запрос расписания
 */
public class TimetableRequest {

    private final LocalDate date;
    private final LocalTime time;

    public TimetableRequest(@NotNull LocalDate date, @NotNull LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public TimetableRequest(@NotNull LocalDate date) {
        this(date, LocalTime.now());
    }

    public TimetableRequest(@NotNull LocalTime time) {
        this(LocalDate.now(), time);
    }

    public TimetableRequest(@NotNull LocalDateTime dateTime) {
        this(dateTime.toLocalDate(), dateTime.toLocalTime());
    }

    public TimetableRequest() {
        this(LocalDateTime.now());
    }

    public TimetableRequest(String localDate) {
        this(DateUtils.fromLocalDateString(localDate));
    }

    /**
     * Возвращает дату
     *
     * @return - дата
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Возвращает время
     *
     * @return - время
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Возвращает состояние дня
     *
     * @return - состояние дня
     */
    public StudentDayState getDayState() {
        int index = DateUtils.getLocalWeekDay(this.getDate());
        if (index < 5) return StudentDayState.WORKING;
        return StudentDayState.WEEKENDS;
    }

    /**
     * Возвращает мастер - расписание
     *
     * @return - мастер - расписание
     */
    public TimetableMaster getMaster() {
        return TimetableManager.downloadOrGetCache(DateUtils.toLocalDateString(this.getDate()));
    }

    /**
     * Возвращает состояние момента дня
     *
     * @param abstractAnalyzer - анализатор
     * @return - состояние
     */
    public StudentDayMomentState getDayMomentState(@NotNull AbstractAnalyzer<?> abstractAnalyzer) {
        var closet = abstractAnalyzer.getClosetInfo(this.getTime());
        if (closet.isStarted() && !closet.isEnded()) return StudentDayMomentState.STARTED;
        if (closet.isEnded()) return StudentDayMomentState.ENDED;
        if (abstractAnalyzer.getObjects().size() == 0) return StudentDayMomentState.EMPTY;
        return StudentDayMomentState.EARLY;
    }

    /**
     * Возвращает состояние момента дня
     * <p>
     * На основе ключа будет создан анализатор `TimetableRequest::createAnalyzer`.
     *
     * @param key - ключ
     * @return - состояние
     */
    public StudentDayMomentState getDayMomentState(@NotNull String key) {
        return this.getDayMomentState(this.createAnalyzer(key));
    }

    /**
     * Создает анализатор по ключу
     *
     * @param key - ключ
     * @return - анализатор GroupAnalyzer или TeacherAnalyzer
     */
    public AbstractAnalyzer<? extends Indexable<?>> createAnalyzer(@NotNull String key) {
        if (key.contains(" ")) return new TeacherAnalyzer(key, this.getMaster());
        return new GroupAnalyzer(key, this.getMaster());
    }
}
