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

package me.ling.kipfin.timetable.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ling.kipfin.core.ftp.FTPClient;
import me.ling.kipfin.core.log.Logger;
import me.ling.kipfin.core.managers.FTPManager;
import me.ling.kipfin.timetable.entities.TimetableMaster;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

/**
 * Загрузчик расписания
 */
public class TimetableManager {

    private static HashMap<String, TimetableMaster> cache = new HashMap<>();

    /**
     * Путь до файлов расписания на сервере
     */
    public static String TIMETABLE_WEB_PATH = "localhost";

    /**
     * Загружает расписание на сервер через FTP
     *
     * @param path       - файл расписания
     * @param removeName - удаленное имя
     * @return - результат выгрузки
     * @throws IOException - исключения при подключении и загрузке
     */
    public static boolean upload(String path, String removeName) throws IOException {
        FTPClient client = FTPManager.createClient();
        boolean result = client.upload(path, removeName);
        client.getRawClient().logout();
        client.getRawClient().disconnect();
        return result;
    }

    /**
     * Загружает расписание на сервер через FTP
     *
     * @param timetableMaster - объект расписания
     * @return - результат выгрузки
     * @throws IOException - исключения при подключении и загрузке
     */
    public static boolean upload(TimetableMaster timetableMaster) throws IOException {
        FTPClient client = FTPManager.createClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = String.format("%s.json", timetableMaster.getDate());
        byte[] bytes = objectMapper.writeValueAsBytes(timetableMaster);

        Logger.logAs("FTP", "Побайтовая отправка файла ->", fileName);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        client.getRawClient().enterLocalPassiveMode();
        boolean result = client.getRawClient().storeFile(fileName, inputStream);
        inputStream.close();
        Logger.logAs("FTP", "Побайтовая отправка файла ->", fileName, result);

        client.getRawClient().logout();
        client.getRawClient().disconnect();
        return result;
    }

    /**
     * Скачивает расписания с сервера
     *
     * @param date - дата расписания
     * @return - объект расписания
     */
    @Nullable
    public static TimetableMaster download(String date) {
        try {
            Logger.logAs("Timetable Manager", Logger.WAIT, "Получение расписания на " + date);
            URL url = new URL(TIMETABLE_WEB_PATH + "/" + date + ".json");
            Logger.logAs("Timetable Manager", Logger.YES, "Получение расписания на " + date);
            return new ObjectMapper().readValue(url, TimetableMaster.class);
        } catch (Exception e) {
            Logger.logAs("Timetable Manager", Logger.NO, "Получение расписания на " + date);
            return null;
        }
    }

    /**
     * Скачивает расписание или достает его из кэша
     *
     * @param date - дата расписания
     * @return - объект расписания
     */
    @Nullable
    public static TimetableMaster downloadOrGetCache(String date) {
        if (!TimetableManager.cache.containsKey(date)) {
            var master = TimetableManager.download(date);
            if (master != null) TimetableManager.cache.put(date, master);
            else return null;
        }
        return TimetableManager.cache.get(date);
    }
}
