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

import me.ling.kipfin.core.log.LoggerColors;
import me.ling.kipfin.core.parsers.ExcelParser;
import me.ling.kipfin.core.utils.StringUtils;
import me.ling.kipfin.database.university.GroupsDB;
import me.ling.kipfin.database.university.TeachersDB;
import me.ling.kipfin.exceptions.university.GroupNotFoundException;
import me.ling.kipfin.exceptions.university.TeacherNotFoundException;
import org.apache.poi.ss.usermodel.CellType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Загрузчик KIPFIN с элементами тестирования
 *
 * @param <T>
 */
public abstract class UniversityTimetableExcelParser<T> extends ExcelParser<T> {

    public UniversityTimetableExcelParser(byte[] bytes) throws IOException {
        super(bytes);
    }

    public UniversityTimetableExcelParser(String path) throws IOException {
        super(path);
    }

    /**
     * Возвращает true, если это файл аудиторий
     *
     * @return - результат проверки файла
     */
    public Boolean isClassroomsFile() {
        var cell = this.getCell(0, 0);
        return cell != null && cell.getCellType().equals(CellType.STRING) &&
                cell.getStringCellValue().contains("Аудитор");
    }

    /**
     * Возвращает true, если стрка похожа на группу
     *
     * @param s - строка
     * @return - результат
     */
    public boolean isGroupString(String s) {
        return s != null && s.contains("-");
    }

    /**
     * Возвращает группу из ячейки
     *
     * @param rowIndex - строка
     * @param colIndex - столбец
     * @return - группа или null
     * @throws TeacherNotFoundException - группа не найдена в базе данных
     */
    @Nullable
    public String getGroupCell(int rowIndex, int colIndex) throws GroupNotFoundException {
        String fixed = this.getPreCell(rowIndex, colIndex, TimetableFixer::fixGroupName);
        if (fixed != null) {
            log("Группа", LoggerColors.getColoredString(LoggerColors.ANSI_CYAN, fixed));

            // Many groups
            if (fixed.contains(" ")) {
                this.wait("Найден пробел. Изучение по частям!");
                List<String> groups = List.of(fixed.split(" "));
                for (String __group : groups) {
                    if (this.isGroupString(__group) && !GroupsDB.shared.contains(universityGroup ->
                            universityGroup.getTitle().equals(__group)))
                        throw new GroupNotFoundException(__group);
                    this.result(true);
                }
                return fixed;
            }
            if (GroupsDB.shared.contains(universityGroup ->
                    universityGroup.getTitle().equals(fixed)) || !this.isGroupString(fixed)) return fixed;
            else throw new GroupNotFoundException(fixed);
        }
        return null;
    }

    /**
     * Возвращает преподавателя из ячейки
     *
     * @param rowIndex - строка
     * @param colIndex - столбец
     * @return - преподаватель или null
     * @throws TeacherNotFoundException - преподаватель не найден в базе данных
     */
    @Nullable
    public String getTeacherCell(int rowIndex, int colIndex) throws TeacherNotFoundException {
        String fixed = this.getPreCell(rowIndex, colIndex, TimetableFixer::fixTeacherName);
        if (fixed != null) {
            log("Преподаватель", LoggerColors.getColoredString(LoggerColors.ANSI_CYAN, fixed));
            if (TeachersDB.shared.getCache().values().stream().anyMatch(v -> v.getName().equals(fixed)))
                return fixed;
            throw new TeacherNotFoundException(fixed);
        }
        return null;
    }

    /**
     * Возвращает дисцеплину из ячейки
     *
     * @param rowIndex - строка
     * @param colIndex - столбец
     * @return - дисцеплина или null
     */
    @Nullable
    public String getSubjectCell(int rowIndex, int colIndex) {
        String cell = TimetableFixer.fixSubjectName(this.getStringValue(rowIndex, colIndex, ""));
        String fixed = this.getPreCell(rowIndex, colIndex, TimetableFixer::fixSubjectName);
        if (fixed != null) return cell;
        return null;
    }

    /**
     * Возвращает аудиторию
     *
     * @param rowIndex - строка
     * @param colIndex - столбец
     * @return - аудитория или null
     */
    @Nullable
    protected String getClassroomCell(int rowIndex, int colIndex) {
        try {
            var cell = this.getCell(rowIndex, colIndex);
            if (cell != null) {
                if (cell.getCellType() == CellType.STRING)
                    return StringUtils.removeAllSpaces(cell.getStringCellValue());
                if (cell.getCellType() == CellType.NUMERIC)
                    return StringUtils.removeAllSpaces(String.valueOf((int) cell.getNumericCellValue()));
            }
            return null;
        }catch (NullPointerException e){
            return null;
        }
    }

    /**
     * Возвращает обработанную ячейку или null
     *
     * @param rowIndex - строка
     * @param colIndex - столбец
     * @param fixer    - функция обработки
     * @return - строка или null
     */
    @Nullable
    protected String getPreCell(int rowIndex, int colIndex, Function<String, String> fixer) {
        String cell = this.getStringValue(rowIndex, colIndex);
        return cell == null ? null : fixer.apply(cell);
    }

}
