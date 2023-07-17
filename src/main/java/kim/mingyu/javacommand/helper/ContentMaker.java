package kim.mingyu.javacommand.helper;

import java.nio.file.*;
import java.io.IOException;
import java.util.Map;

public class ContentMaker {

    protected String path;
    protected Map<String, String> replaces;

    public ContentMaker(String path, Map<String, String> replaces) {
        this.path = path;
        this.replaces = replaces;
    }

    public String getPath() {
        return this.path;
    }

    public String getContents() throws IOException {
        Path filePath = Paths.get(this.path);
        String contents = new String(Files.readAllBytes(filePath));

        for (Map.Entry<String, String> entry : this.replaces.entrySet()) {
            String search = "$$" + entry.getKey().toUpperCase() + "$$";
            String replace = entry.getValue();
            contents = contents.replace(search, replace);
        }

        return contents;
    }

    public String render() throws IOException {
        return this.getContents();
    }
}
