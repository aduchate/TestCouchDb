package org.taktik.jrebel.test.applications.cli;

import org.slf4j.LoggerFactory;
import org.taktik.jrebel.test.applications.app.App;
import org.taktik.jrebel.test.applications.app.TestApp;

import java.io.File;
import java.net.URISyntaxException;

public class Console {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) throws Exception {
        new Console(args).run();
    }

    public Console(String[] args) throws Exception {
    }

    private void run() {
        TestApp serverApp = new TestApp(getHomeDir(), "node1", "::1");
        startApp(serverApp);
    }

    private File getHomeDir() {
        File homeDir;

        String jarPath = getJarPath();
        if (jarPath != null) {
            // Build home from jarPath
            homeDir = new File(jarPath).getParentFile().getParentFile();
        } else {
            // Use working directory as home
            homeDir = new File(System.getProperty("user.dir"));
        }
        if (!homeDir.isDirectory()) {
            System.err.println("'" + homeDir + "' is not a valid home directory !");
            System.exit(1);
        }
        return homeDir;
    }

    private String getJarPath() {
        // Note : This will only work when icure is packaged as JAR of course
        try {
            String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Make sure we found a jar path
            if (jarPath != null && jarPath.toLowerCase().endsWith(".jar")) {
                return jarPath;
            }
        } catch (URISyntaxException ignored) {
        }

        return null;
    }

    private void startApp(App app) {
        try {
            app.start();

        } catch (Exception e) {
            log.error("Exception", e);
        }
    }
}
