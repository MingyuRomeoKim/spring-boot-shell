package kim.mingyu.javacommand.commands;

import kim.mingyu.javacommand.helper.CommandHelper;
import kim.mingyu.javacommand.helper.FileMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ShellComponent
public class CommandCommand {

    @Value("${project.command.package}")
    private String commandPackage;

    @Value("${project.command.package.path}")
    private String commandPackagePath;

    @ShellMethod(key = "make:command", value="make:command {commandName} 을 입력하면 commandName에 해당하는 이름의 command shell 생성 파일을 만든다.")
    public boolean makecommand(@ShellOption(help = "Name for the command", defaultValue = "") String commandName) {

        if (commandName.trim().length() < 1) {
            System.out.println("You must have commandName option");
            return false;
        }

        try {
            // [step 1] create command file
            if (commandName.contains("/")) {
                List<String> dumpArray = new ArrayList<>(Arrays.asList(commandName.split("/")));
                commandName = dumpArray.remove(dumpArray.size() - 1); // equivalent to PHP's array_pop
                this.commandPackage += "." + String.join(".", dumpArray);
                this.commandPackagePath += "/" + String.join("/", dumpArray);
            }

            CommandHelper commandHelper = new CommandHelper(this.commandPackage, this.commandPackagePath);
            String commandFileContent = commandHelper.getCommandTemplateContents(commandName);
            String commandRealPath = Paths.get(System.getProperty("user.dir"), this.commandPackagePath, commandName + ".java").toString();

            FileMaker fileMaker = new FileMaker(commandRealPath,commandFileContent);
            fileMaker.generate();

        } catch (Exception exception) {
            System.out.println("command creation Failed. Error Message ::" + exception.getMessage());
            return false;
        }

        System.out.println("command creation successful!! command Name :: " + commandName);
        return true;
    }
}
