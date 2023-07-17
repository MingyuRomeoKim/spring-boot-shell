package kim.mingyu.javacommand.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerHelper {

    protected String controllerNamespace;
    protected String print;

    protected String controllerType;

    public ControllerHelper(String controllerNamespace, String print, String controllerType) {
        this.controllerNamespace = controllerNamespace;
        this.controllerType = controllerType;
        this.print = print;
    }

    public String getControllerTemplateContents(String controllerName) throws IOException {
        String stubPath = getStubFilePath(this.controllerType);

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("CONTROLLER_NAMESPACE", this.controllerNamespace);
        replaceMap.put("CONTROLLER_NAME", controllerName);
        replaceMap.put("CONTROLLER_NAME_LOWER_WITHOUT_CONTROLLER", controllerName.toLowerCase().replace("controller",""));

        stubPath = getClass().getResource("/" + stubPath).getPath();
        stubPath = stubPath.substring(1).replace("/","\\");

        ContentMaker contentMaker = new ContentMaker(stubPath, replaceMap);
        return contentMaker.render();
    }

    protected String getStubFilePath(String type) {
        String stub = "";
        switch (type) {
            case "rest":
                stub = "stub/controller/restController.stub";
                break;
            case "default":
                stub = "stub/controller/controller.stub";
                break;
        }
        return stub;
    }
}
