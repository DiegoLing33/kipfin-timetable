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

package me.ling.kipfin.timetable.analyzers;

import me.ling.kipfin.timetable.abstracts.AbstractAnalyzer;
import me.ling.kipfin.timetable.entities.Classroom;
import me.ling.kipfin.timetable.entities.TimetableMaster;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Анализатор преподавателей
 */
public class TeacherAnalyzer extends AbstractAnalyzer<Classroom> {

    private final String teacher;

    public TeacherAnalyzer(String teacher, @NotNull TimetableMaster master) {
        super(master.getTimeInfo(), master.getClassroomsForName(teacher));
        this.teacher = teacher;
    }

    /**
     * Возвращает имя преподавателя
     * @return  - имя преподавателя
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * Возвращает аудитории
     * @return  - аудитории
     */
    public List<Classroom> getClassrooms() {
        return this.getObjects();
    }

}
