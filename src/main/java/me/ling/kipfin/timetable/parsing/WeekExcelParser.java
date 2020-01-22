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

import me.ling.kipfin.timetable.entities.WeekSubjects;
import me.ling.kipfin.timetable.entities.DaySubjects;
import me.ling.kipfin.timetable.entities.Subject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeekExcelParser extends UniversityTimetableExcelParser<WeekSubjects<Subject>> {

    public WeekExcelParser(String path) throws IOException {
        super(path);
    }

    @Override
    public WeekSubjects<Subject> start() {
        log(this.getStringValue(1, 0));
        WeekSubjects<Subject> weekSubjects = new WeekSubjects<>();

        for (int rowIndex = 1; rowIndex < this.getRowsCount(); rowIndex += 7) {
//            weekSubjects.add(new DaySubjects<>()); // <-- Adds empty
            DaySubjects<Subject> daySubjects = new DaySubjects<>();

            for (int colIndex = 1; colIndex < this.getColsCount(rowIndex); colIndex++) {
                String group = this.getGroupCell(rowIndex, colIndex);
                List<Subject> subjectList = new ArrayList<>();
                if (group == null) continue;
                for (int i = 1; i < 7; i++) {
                    String subject = this.getSubjectCell(rowIndex + i, colIndex);
                    if (subject == null) continue;
                    subjectList.add(new Subject(subject, i - 1));
                }
                daySubjects.put(group, subjectList);
            }
            weekSubjects.add(daySubjects);
        }
        return weekSubjects;
    }
}
