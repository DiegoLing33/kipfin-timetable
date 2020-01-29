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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class GroupAnalyzerTest {

    public String classroomsFile = "./src/test/resources/c.xlsx";
    public String weekFile = "./src/test/resources/w.xls";

    @BeforeEach
    void setUp() throws SQLException {
        Dotenv env = Dotenv.load();
        Bootloader bootloader = new Bootloader(env);
        bootloader.updateDatabase(false);

    }

    @Test
    public void testGroupAnalyzer() throws IOException {

        var master = TimetableMaster.create(classroomsFile, weekFile);
        var analyzer = new GroupAnalyzer("1ИСИП-319", master);

        var indexes = analyzer.getClosetInfo(LocalTime.of(10, 0));
        assertTrue(indexes.isStarted());

        var indexes1 = analyzer.getClosetInfo(LocalTime.of(16, 35));
        assertEquals(indexes1.getClosetIndex(), 4);

        var indexes2 = analyzer.getClosetInfo(LocalTime.of(18, 11));
        assertTrue(indexes2.isStarted());
        assertTrue(indexes2.isEnded());
    }

}