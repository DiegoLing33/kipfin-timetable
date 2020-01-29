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

import io.github.cdimascio.dotenv.Dotenv;
import me.ling.kipfin.core.Bootloader;
import me.ling.kipfin.timetable.entities.TimetableMaster;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TeacherAnalyzerTest {

    public static String classroomsFile = "./src/test/resources/c.xlsx";
    public static String weekFile = "./src/test/resources/w.xls";
    public static TimetableMaster master;

    @BeforeAll
    static void beforeAll() throws SQLException, IOException {
        Dotenv env = Dotenv.load();
        Bootloader bootloader = new Bootloader(env);
        bootloader.updateDatabase(false);
        TeacherAnalyzerTest.master = TimetableMaster.create(classroomsFile, weekFile);
    }

    @Test
    public void testTeacherAnalyzer(){

        var analyzer = new TeacherAnalyzer("Азовцева Вера Владимировна", master);

        assertEquals(analyzer.getFirstIndex(), 3);
        assertEquals(analyzer.getLastIndex(), 4);
        assertFalse(analyzer.hasIndex(0));
        assertFalse(analyzer.hasIndex(1));
        assertFalse(analyzer.hasIndex(2));
        assertTrue(analyzer.hasIndex(3));
        assertTrue(analyzer.hasIndex(4));

        assertEquals(analyzer.getClassrooms().size(), master.getClassrooms().get("Азовцева Вера Владимировна").size());
        assertEquals(analyzer.getTeacher(), "Азовцева Вера Владимировна");
        assertEquals(analyzer.getObjectByIndex(3).getWhere(), "211");
    }


}