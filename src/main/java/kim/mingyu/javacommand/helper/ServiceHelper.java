package kim.mingyu.javacommand.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServiceHelper {

    protected String serviceNamespace;
    protected String print;

    public ServiceHelper(String serviceNamespace, String print) {
        this.serviceNamespace = serviceNamespace;
        this.print = print;
    }

    public String getServiceTemplateContents(String serviceName) throws IOException {
        String stubPath = getStubFilePath("default");

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("SERVICE_NAMESPACE", this.serviceNamespace);
        replaceMap.put("SERVICE_NAME", serviceName);

        stubPath = getClass().getResource("/" + stubPath).getPath();
        stubPath = stubPath.substring(1).replace("/","\\");

        ContentMaker contentMaker = new ContentMaker(stubPath, replaceMap);
        return contentMaker.render();
    }

    protected String getStubFilePath(String type) {
        String stub = "";
        switch (type) {
            case "default":
                stub = "stub/service/service.stub";
                break;
        }
        return stub;
    }
}

