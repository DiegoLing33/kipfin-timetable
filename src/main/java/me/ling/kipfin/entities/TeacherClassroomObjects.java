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

package me.ling.kipfin.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Связка: преподаватель -> аудитория
 */
public class TeacherClassroomObjects extends HashMap<String, String> {
    public TeacherClassroomObjects(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TeacherClassroomObjects(int initialCapacity) {
        super(initialCapacity);
    }

    public TeacherClassroomObjects() {
    }

    public TeacherClassroomObjects(Map<? extends String, ? extends String> m) {
        super(m);
    }

    /**
     * Возвращает сет преподавателей через запятую
     * @return - преподаватели через запятую
     */
    public String getTeachersJoin(){
        return String.join(", ", this.keySet());
    }

    /**
     * Возвращает сет аудиторий через запятую
     * @return - аудитории через запятую
     */
    public String getClassroomsJoin(){
        return String.join(", ", this.values());
    }
}
