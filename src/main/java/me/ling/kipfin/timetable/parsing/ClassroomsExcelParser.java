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

package me.ling.kipfin.timetable.parsing;

import me.ling.kipfin.core.utils.DateUtils;
import me.ling.kipfin.timetable.entities.Classroom;
import me.ling.kipfin.timetable.entities.Classrooms;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсер файла excel аудиторий
 */
public final class ClassroomsExcelParser extends UniversityTimetableExcelParser<Classrooms> {

    /**
     * Конструктор
     *
     * @param path - путь до excel файла
     * @throws IOException - исключение открытия файла
     */
    public ClassroomsExcelParser(String path) throws IOException {
        super(path);
    }

    /**
     * Возвращает дату аудиторий
     *
     * @return - объект даты
     * @throws DateTimeParseException - дата указана не верно
     */
    public LocalDate getDate() throws DateTimeParseException {
        String cell = this.getStringValue(0, 0, "").replace("Аудитории на ", "");
        return DateUtils.fromLocalDateString(cell);
    }

    @Override
    public Classrooms start() {
        log("Аудитории на ", DateUtils.toLocalDateString(this.getDate()));
        var classrooms = new Classrooms();

        // Цикл по строкам аудиторий
        for (int rowIndex = 2; rowIndex < this.getRowsCount(); rowIndex += 2) {
            String teacher = this.getTeacherCell(rowIndex, 0);
            List<Classroom> classroomList = new ArrayList<>();

            for (int colIndex = 1; colIndex < this.getColsCount(rowIndex); colIndex++) {
                String group = this.getGroupCell(rowIndex, colIndex);
                String classroom = this.getClassroomCell(rowIndex + 1, colIndex);
                if (group == null || classroom == null) continue;
                classroomList.add(new Classroom(teacher, classroom, group, colIndex - 1));
            }
            classrooms.put(teacher, classroomList);
        }
        return classrooms;
    }
}
