package kim.mingyu.javacommand.command;

import kim.mingyu.javacommand.helper.ControllerHelper;
import kim.mingyu.javacommand.helper.EntityHelper;
import kim.mingyu.javacommand.helper.FileMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Paths;
import java.util.*;

@ShellComponent
public class EntityCommand {

    @Value("${project.root.path}")
    private String projectRootPath;

    @Value("${project.entity.package}")
    private String entityPackage;

    @Value("${project.entity.package.path}")
    private String entityPackagePath;

    @ShellMethod(key = "make:entity", value=
            "make:entity {entityName} {entityType = jpa} 을 입력하면 entityName에 해당하는 이름, " +
            "entityType에 따라 용도에 맞는 entity를 생성한다. 현재 entity type jpa only.")
    public boolean makeEntityCommand(@ShellOption(help = "Name for the entity", defaultValue = "") String entityName,
                                     @ShellOption(help = "type for the entity", defaultValue = "jpa") String entityType) {

        if (entityName.trim().length() < 1) {
            System.out.println("You must have entityName option");
            return false;
        }

        if (!entityType.equals("jpa")) {
            System.out.println("현재 지원되는 entity type은 jpa 1개뿐!");
            return false;
        }

        Map<String,String> entityKeyAndValue = this.getEntityKeyAndValue();
        if (entityKeyAndValue.isEmpty()) {
            System.out.println("엔티티 생성에는 최소 1개 이상의 변수에 대한 선언이 필요합니다.");
            return false;
        }

        String entityPropertiesString =  this.getEntityPropertiesString(entityKeyAndValue);
        if (entityPropertiesString.isEmpty()) {
            System.out.println("엔티티 생성에는 최소 1개 이상의 변수에 대한 선언이 필요합니다.");
            return false;
        }

        try {
            // [step 1] init
            if (entityName.contains("/")) {
                List<String> dumpArray = new ArrayList<>(Arrays.asList(entityName.split("/")));
                entityName = dumpArray.remove(dumpArray.size() - 1);
                this.entityPackage += "." + String.join(".", dumpArray);
                this.entityPackagePath += "/" + String.join("/", dumpArray);
            }

            // [step 2] create entityName file
            EntityHelper entityHelper = new EntityHelper(this.entityPackage, entityType, entityPropertiesString);
            String entityFileContent = entityHelper.getEntityTemplateContents(entityName);
            String entityRealPath = Paths.get(this.projectRootPath, this.entityPackagePath, entityName + ".java").toString();

            FileMaker fileMaker = new FileMaker(entityRealPath, entityFileContent);
            fileMaker.generate();

        } catch (Exception exception) {
            System.out.println("Entity creation Failed. Error Message ::" + exception.getMessage());
            return false;
        }

        System.out.println("Entity creation successful!! Entity Name :: " + entityName);
        return true;
    }

    protected Map<String,String> getEntityKeyAndValue () {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> map = new HashMap<>();

        while (true) {
            System.out.println("자료형을 입력해주세요. 그만 입력하고 싶을 경우 exit 를 입력하세요. ex) String,Long,Integer,Float 등 :: ");
            String key = scanner.nextLine();

            // 특정 조건에 따라 반복을 종료
            if ("exit".equals(key)) {
                break;
            }

            System.out.println("변수명을 입력하세요 :: ");
            String value = scanner.nextLine();

            map.put(key, value);
        }

        return map;
    }

    protected String getEntityPropertiesString(Map<String,String> entityKeyAndValue) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : entityKeyAndValue.entrySet()) {
            String type = entry.getKey();
            String variableName = entry.getValue();

            if (type.equals("Long") && variableName.equals("id")) {
                stringBuilder.append("@Id\n");
            }
            stringBuilder.append("    private ").append(type).append(" ").append(variableName).append(";\n");
        }

        String output = stringBuilder.toString();

        return output;
    }
}