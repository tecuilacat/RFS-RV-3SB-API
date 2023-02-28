package shapedetectionprogram;

import api.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Pythonrunner {

    private static final Logger logger = new Logger(Pythonrunner.class);

    public static List<String> runPythonFile(String path) {
        String command = "py " + path;
        List<String> res = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            reader.lines().forEach(res::add);
        } catch (Exception e) {
            logger.error("Python file could not be excuted", e);
        }
        return res;
    }

}