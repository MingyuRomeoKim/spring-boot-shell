package kim.mingyu.javacommand.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EntityHelper {

    protected String entityNamespace;
    protected String entityProperties;

    protected String entityType;

    public EntityHelper(String entityNamespace, String entityType, String entityProperties) {
        this.entityNamespace = entityNamespace;
        this.entityType = entityType;
        this.entityProperties = entityProperties;
    }

    public String getEntityTemplateContents(String entityName) throws IOException {
        String stubPath = getStubFilePath(this.entityType);

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("ENTITY_NAMESPACE", this.entityNamespace);
        replaceMap.put("ENTITY_NAME", entityName);
        replaceMap.put("ENTITY_PROPERTIES",this.entityProperties);

        InputStream stream = getClass().getResourceAsStream("/" + stubPath);
        ContentMaker contentMaker = new ContentMaker(stream, replaceMap);

        return contentMaker.render();
    }

    protected String getStubFilePath(String type) {
        String stub = "";
        switch (type) {
            case "jpa" :
            case "default":
                stub = "stub/entity/entity.stub";
                break;
        }
        return stub;
    }
}
