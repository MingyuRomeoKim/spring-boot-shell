package kim.mingyu.javacommand.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CommandHelper {

    protected String commandNamespace;

    public CommandHelper(String commandNamespace) {
        this.commandNamespace = commandNamespace;
    }

    public String getCommandTemplateContents(String commandName) throws IOException {
        String stubPath = getStubFilePath("default");

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("COMMAND_NAMESPACE", this.commandNamespace);
        replaceMap.put("COMMAND_NAME", commandName);
        replaceMap.put("COMMAND_NAME_LOWER",commandName.toLowerCase().replace("command",""));

        InputStream stream = getClass().getResourceAsStream("/" + stubPath);
        ContentMaker contentMaker = new ContentMaker(stream, replaceMap);

        return contentMaker.render();
    }

    protected String getStubFilePath(String type) {
        String stub = "";
        switch (type) {
            case "default":
                stub = "stub/command/command.stub";
                break;
        }
        return stub;
    }
}

