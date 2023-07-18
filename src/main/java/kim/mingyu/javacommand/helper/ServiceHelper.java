package kim.mingyu.javacommand.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceHelper {

    protected String serviceNamespace;

    public ServiceHelper(String serviceNamespace) {
        this.serviceNamespace = serviceNamespace;
    }

    public String getServiceTemplateContents(String serviceName) throws IOException {
        String stubPath = getStubFilePath("default");

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("SERVICE_NAMESPACE", this.serviceNamespace);
        replaceMap.put("SERVICE_NAME", serviceName);

        InputStream stream = getClass().getResourceAsStream("/" + stubPath);
        ContentMaker contentMaker = new ContentMaker(stream, replaceMap);

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

