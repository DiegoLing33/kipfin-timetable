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

package me.ling.kipfin.timetable.abstracts;

import me.ling.kipfin.abstracts.Indexable;
import me.ling.kipfin.core.utils.ListUtils;
import me.ling.kipfin.exceptions.NotFoundEntityException;
import me.ling.kipfin.timetable.entities.timeinfo.TimeIndexes;
import me.ling.kipfin.timetable.entities.timeinfo.TimeInfo;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Абстрактный анализатор
 *
 * @param <T>
 */
public abstract class AbstractAnalyzer<T extends Indexable<T>> {

    private int firstIndex;
    private int lastIndex;
    private TimeInfo timeInfo;
    private List<T> objects;

    public AbstractAnalyzer(TimeInfo timeInfo, List<T> objects) {
        this.timeInfo = timeInfo;
        this.objects = objects;
        Collections.sort(this.objects);

        this.firstIndex = this.getObjects().get(0).getIndex();
        this.lastIndex = this.getObjects().get(this.getObjects().size() - 1).getIndex();
    }

    /**
     * Возвращает первый индекс
     *
     * @return - первый индекс
     */
    public int getFirstIndex() {
        return firstIndex;
    }

    /**
     * Возвращает последний индекс
     *
     * @return - последний индекс
     */
    public int getLastIndex() {
        return lastIndex;
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
     * Возвращает информацию о времени
     * @return  - информация о времени
     */
    public TimeInfo getTimeInfo() {
        return timeInfo;
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
        return ListUtils.contains(this.getObjects(), Indexable::getIndex, index);
    }

    /**
     * Возвращает информацию о ближайшей паре
     *
     * @param time - время
     * @return - результат TimeIndexes
     */
    public TimeIndexes getClosetInfo(LocalTime time) {
        int closetIndex = -1;
        try {
            closetIndex = ListUtils.get(this.getObjects(), s -> {
                return time.isBefore(this.timeInfo.get(s.getIndex()).getEndsTime());
            }).getIndex();
        }catch (Exception e){
            // Nothing is done
        }
        return new TimeIndexes(closetIndex, this.isEnded(time), this.isStarted(time));
    }

    /**
     * Возвращает список индексируемых объектов
     *
     * @return - индексируемые объекты
     */
    public List<T> getObjects() {
        return objects;
    }

    /**
     * Возвращает предмет по индексу
     *
     * @param index - индекс
     * @return - предмет
     * @throws NotFoundEntityException - исключение, если предмет не найден
     */
    public T getObjectByIndex(Integer index) throws NotFoundEntityException {
        return ListUtils.get(this.getObjects(), Indexable::getIndex, index);
    }
}
