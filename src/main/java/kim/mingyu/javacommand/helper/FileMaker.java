package kim.mingyu.javacommand.helper;

import java.nio.file.*;
import java.io.*;

public class FileMaker {
    protected String path;
    protected String contents;

    public FileMaker(String path, String contents) {
        this.path = path;
        this.contents = contents;
    }

    public String getPath() {
        return this.path;
    }

    public String getContents() {
        return this.contents;
    }

    public boolean generate() throws IOException {
        Path filePath = Paths.get(this.path);
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent()); // Create directory if not exist
            Files.write(filePath, this.contents.getBytes(), StandardOpenOption.CREATE);
            return true;
        } else {
            return false;
        }
    }
}

