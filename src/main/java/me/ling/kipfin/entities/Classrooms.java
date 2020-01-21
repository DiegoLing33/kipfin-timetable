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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Аудитории
 */
public class Classrooms extends HashMap<String, List<Classroom>> {
    public Classrooms(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public Classrooms(int initialCapacity) {
        super(initialCapacity);
    }

    public Classrooms() {
    }

    public Classrooms(Map<? extends String, ? extends List<Classroom>> m) {
        super(m);
    }

    /**
     * Возвращает аудитории по группе
     *
     * @param group - группа
     * @return - список аудиторий, который ключает в себя группу
     */
    public List<Classroom> getClassroomsByGroup(String group) {
        var items = new ArrayList<Classroom>();
        // for each teacher
        this.values().forEach(teacherSection -> teacherSection.forEach(item -> {
            if (item.group.contains(group)) items.add(item);
        }));
        return items;
    }
}
