package kim.mingyu.javacommand.commands;

import kim.mingyu.javacommand.helper.ControllerHelper;
import kim.mingyu.javacommand.helper.FileMaker;
import kim.mingyu.javacommand.helper.ServiceHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ShellComponent
public class ControllerCommand {

    @Value("${project.controller.package}")
    private String controllerPackage;

    @Value("${project.controller.package.path}")
    private String controllerPackagePath;

    @ShellMethod(key = "make:controller", value = "make:controller {controllerName} {controllerType = default|rest} 을 입력하면 controllerName에 해당하는 이름, controllerType에 따라 용도에 맞는 controller를 생성한다.")
    public boolean makeController(@ShellOption(help = "Name for the controller", defaultValue = "") String controllerName,
                                  @ShellOption(help = "type for the controller", defaultValue = "default") String controllerType) {
        if (controllerName.trim().length() < 1) {
            System.out.println("You must have controllerName option");
            return false;
        }

        try {
            // [step 1] create controllerName file
            if (controllerName.contains("/")) {
                List<String> dumpArray = new ArrayList<>(Arrays.asList(controllerName.split("/")));
                controllerName = dumpArray.remove(dumpArray.size() - 1); // equivalent to PHP's array_pop
                this.controllerPackage += "." + String.join(".", dumpArray);
                this.controllerPackagePath += "/" + String.join("/", dumpArray);
            }

            ControllerHelper controllerHelper = new ControllerHelper(this.controllerPackage, this.controllerPackagePath, controllerType);
            String controllerFileContent = controllerHelper.getControllerTemplateContents(controllerName);
            String controllerRealPath = Paths.get(System.getProperty("user.dir"), this.controllerPackagePath, controllerName + ".java").toString();

            FileMaker fileMaker = new FileMaker(controllerRealPath, controllerFileContent);
            fileMaker.generate();

        } catch (Exception exception) {
            System.out.println("Controller creation Failed. Error Message ::" + exception.getMessage());
            return false;
        }

        System.out.println("Controller creation successful!! Service Name :: " + controllerName);
        return true;
    }
}
