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

package me.ling.kipfin.parsing;

import me.ling.kipfin.core.log.LoggerColors;
import me.ling.kipfin.core.parsers.ExcelParser;
import me.ling.kipfin.core.utils.StringUtils;
import me.ling.kipfin.database.GroupsDB;
import me.ling.kipfin.database.TeachersDB;
import me.ling.kipfin.exceptions.GroupNotFoundException;
import me.ling.kipfin.exceptions.TeacherNotFoundException;
import org.apache.poi.ss.usermodel.CellType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Загрузчик KIPFIN с элементами тестирования
 *
 * @param <T>
 */
public abstract class KipfinExcelParser<T> extends ExcelParser<T> {

    public KipfinExcelParser(String path) throws IOException {
        super(path);
    }

    public static List<String> IGNORED_GROUP_NAMES = List.of("конс");

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
                    if (!IGNORED_GROUP_NAMES.contains(__group) && !GroupsDB.shared.getCache().containsValue(__group))
                        throw new GroupNotFoundException(__group);
                    this.result(true);
                }
                return fixed;
            }
            if (GroupsDB.shared.getCache().containsValue(fixed) || IGNORED_GROUP_NAMES.contains(fixed)) return fixed;
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
        var cell = this.getCell(rowIndex, colIndex);
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING)
                return StringUtils.removeAllSpaces(cell.getStringCellValue());
            if (cell.getCellType() == CellType.NUMERIC)
                return StringUtils.removeAllSpaces(String.valueOf((int) cell.getNumericCellValue()));
        }
        return null;
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
