package me.ling.kipfin;

import io.github.cdimascio.dotenv.Dotenv;
import me.ling.kipfin.core.Bootloader;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        var env = Dotenv.load();
        Bootloader bootloader = new Bootloader(env);
        bootloader.updateDatabase();

    }

}
