package kim.mingyu.javacommand.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentMaker {

    protected InputStream stream;
    protected Map<String, String> replaces;

    public ContentMaker(InputStream stream, Map<String, String> replaces) {
        this.stream = stream;
        this.replaces = replaces;
    }

    public InputStream getStream() {
        return this.stream;
    }

    public String getContents() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.stream));
        String content = reader.lines().collect(Collectors.joining("\n"));

        for (Map.Entry<String, String> replace : this.replaces.entrySet()) {
            content = content.replace("$$" + replace.getKey().toUpperCase() + "$$", replace.getValue());
        }

        return content;
    }

    public String render() throws IOException {
        return this.getContents();
    }
}
