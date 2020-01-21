package me.ling.kipfin.parsing;

import me.ling.kipfin.core.utils.StringUtils;

import java.util.Arrays;

/**
 * Исправитель ошибок в расписании
 */
public class TimetableFixer {

    /**
     * Исправляет имя группы
     *
     * @param group - входное имя группы
     * @return - выходное имя группы
     */
    public static String fixGroupName(String group) {
        if (group.equals("1ОИБАС1019")) return "1ОИБАС-1019";
        if (group.equals("1ОИБАС1119")) return "1ОИБАС-1119";
        if (group.equals("1ОИБАС1219")) return "1ОИБАС-1219";
        if (group.equals("2ОИБАС1018")) return "2ОИБАС-1018";
        return group;
    }

    /**
     * Исправляет ФИО преподавателей
     *
     * @param teacherName - преподаватель
     * @return - имя преподавателя с больших букв
     */
    public static String fixTeacherName(String teacherName) {
        Object[] fio = Arrays.stream(teacherName.split(" ")).map(StringUtils::title).toArray();
        String fixed = String.join(" ", Arrays.copyOf(fio, fio.length, String[].class));
        fixed = fixed.replace("ё", "е");
        return fixed;
    }

    /**
     * Исправляет название предмета
     *
     * @param s - название предмета
     * @return - исправленное название предмета
     */
    public static String fixSubjectName(String s) {
        String subjectName = s;
        if (subjectName.contains("_ПР")) subjectName = subjectName.replaceAll("_ПР", "(Практика)");
        if (subjectName.contains("комп.")) subjectName = subjectName.replaceAll("комп.", "компьютерных");
        if(subjectName.contains("/")) subjectName = subjectName.split("/")[0];
        return subjectName;
    }
}
