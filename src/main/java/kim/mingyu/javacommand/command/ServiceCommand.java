package kim.mingyu.javacommand.command;

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
public class ServiceCommand {

    @Value("${project.service.package}")
    private String servicePackage;

    @Value("${project.service.package.path}")
    private String servicePackagePath;

    @ShellMethod(key = "make:service", value="Service creation command")
    public boolean makeService(@ShellOption(help = "Name for the service", defaultValue = "") String serviceName) {

        if (serviceName.trim().length() < 1) {
            System.out.println("You must have serviceName option");
            return false;
        }

        try {
            // [step 1] create service file
            if (serviceName.contains("/")) {
                List<String> dumpArray = new ArrayList<>(Arrays.asList(serviceName.split("/")));
                serviceName = dumpArray.remove(dumpArray.size() - 1); // equivalent to PHP's array_pop
                this.servicePackage += "." + String.join(".", dumpArray);
                this.servicePackagePath += "/" + String.join("/", dumpArray);
            }

            ServiceHelper serviceHelper = new ServiceHelper(this.servicePackage, this.servicePackagePath);
            String serviceFileContent = serviceHelper.getServiceTemplateContents(serviceName);
            String serviceRealPath = Paths.get(System.getProperty("user.dir"), this.servicePackagePath, serviceName + ".java").toString();

            FileMaker fileMaker = new FileMaker(serviceRealPath,serviceFileContent);
            fileMaker.generate();

        } catch (Exception exception) {
            System.out.println("Service creation Failed. Error Message ::" + exception.getMessage());
            return false;
        }

        System.out.println("Service creation successful!! Service Name :: " + serviceName);
        return true;
    }
}
