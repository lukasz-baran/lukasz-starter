package com.lukaszbaran.starter.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;

public class CommandExecutor {
    private static final Logger LOGGER = Logger.getLogger(CommandExecutor.class);

    private static String toString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    public static void run(String command) {
        LOGGER.debug("executing " + command);
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();

            String commandOutput = toString(p.getInputStream());
            String errorOutput = toString(p.getErrorStream());
            LOGGER.debug("input stream " + commandOutput);
            LOGGER.debug("error stream " + errorOutput);
        } catch (IOException | InterruptedException ex) {
            LOGGER.error("exception caught ", ex);
        }
    }
}
