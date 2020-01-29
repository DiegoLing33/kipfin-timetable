package me.ling.kipfin.timetable.entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Информаци о времени
 */
public class TimeInfo extends ArrayList<TimeInfoItem> {

    /**
     * Возвращает стандартную информацию о парах
     * @return  - стандартная информация о парах
     */
    public static TimeInfo getDefault() {
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.add(new TimeInfoItem("09:30", "11:00"));
        timeInfo.add(new TimeInfoItem("11:10", "12:40"));
        timeInfo.add(new TimeInfoItem("13:20", "14:50"));
        timeInfo.add(new TimeInfoItem("15:00", "16:30"));
        timeInfo.add(new TimeInfoItem("16:40", "18:10"));
        return timeInfo;
    }
    /**
     * Возвращает информацию о времени в стандартный день с классным часом
     * @return  - информация о часах с классным часом
     */
    public static TimeInfo getForthShort() {
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.add(new TimeInfoItem("09:30", "11:00"));
        timeInfo.add(new TimeInfoItem("11:10", "12:40"));
        timeInfo.add(new TimeInfoItem("13:20", "14:50"));
        timeInfo.add(new TimeInfoItem("15:00", "15:45"));
        timeInfo.add(new TimeInfoItem("16:00", "17:30"));
        return timeInfo;
    }

    public TimeInfo(int initialCapacity) {
        super(initialCapacity);
    }

    public TimeInfo() {
    }

    public TimeInfo(@NotNull Collection<? extends TimeInfoItem> c) {
        super(c);
    }
}
