package kim.mingyu.javacommand.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ShellComponent
public class ServiceCommand {

    @Value("${project.service.package}")
    private String servicePackage;

    @Value("${project.service.package.path}")
    private String servicePackagePath;

    @ShellMethod(key = "make-service", value="Service creation command")
    public boolean makeService(@ShellOption(help = "Name for the service", defaultValue = "") String serviceName) {

        if (serviceName.trim().length() < 1) {
            System.out.print("You must have serviceName option");
            return false;
        }

        try {
            // [step 1] create service file
            if (serviceName.contains("/")) {
                List<String> dumpArray = new ArrayList<>(Arrays.asList(serviceName.split("/")));
                serviceName = dumpArray.remove(dumpArray.size() - 1); // equivalent to PHP's array_pop
                servicePackage += "." + String.join(".", dumpArray);
                servicePackagePath += "/" + String.join("/", dumpArray);
            }

        } catch (Exception exception) {
            System.out.print(exception.getMessage());
            return false;
        }

        System.out.println("Service Name: " + serviceName);
        System.out.println("service Package : " + servicePackage);
        System.out.println("service Package Path : " + servicePackagePath);
        return true;
    }
}
