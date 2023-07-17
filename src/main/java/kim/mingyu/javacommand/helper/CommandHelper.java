package kim.mingyu.javacommand.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandHelper {

    protected String commandNamespace;
    protected String print;

    public CommandHelper(String commandNamespace, String print) {
        this.commandNamespace = commandNamespace;
        this.print = print;
    }

    public String getCommandTemplateContents(String commandName) throws IOException {
        String stubPath = getStubFilePath("default");

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("COMMAND_NAMESPACE", this.commandNamespace);
        replaceMap.put("COMMAND_NAME", commandName);
        replaceMap.put("COMMAND_NAME_LOWER",commandName.toLowerCase().replace("command",""));

        stubPath = getClass().getResource("/" + stubPath).getPath();
        stubPath = stubPath.substring(1).replace("/","\\");

        ContentMaker contentMaker = new ContentMaker(stubPath, replaceMap);
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

