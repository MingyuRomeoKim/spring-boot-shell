# spring-boot-shell
Laravel에서 cli 형식으로 사용하는 artisan 명령어처럼 생산성을 높이는 모든 것들을 Spring boot Shell 스크립트로 구현해서 Spring으로도 cli의 생산성을 느껴보자고! <br/>

## 설치 및 실행하기
해당 애플리케이셔을 사용하기 위해 아래의 순서대로 설치 및 실행 해보자.
```shell
$ git clone git@github.com:MingyuRomeoKim/spring-boot-shell.git
$ cd spring-boot-shell
```
1. application.properties 파일 설정하기
```shell
$ vi src/main/resource/application.properties
```
2. config.properties 파일 설정하기
```shell
$ vi src/main/resource/config.properties
```
3. build 하기
```shell
$ ./gradlew build
```
4. 실행하기
```shell
$ java -jar ./build/libs/{your-snapshot}.jar
```

## Controller 만들기
controller는 크게 2가지로 구분된다. default 컨트롤러와 rest형식의 controller이다.<br/>
만들어진 contoller는 config.properties에서 선언한 package 및 path로 생성된다. <br/>
contoller 클래스를 생성하여 사용할 경우 `org.springframework.boot:spring-boot-starter-web` dependency를 추가해야한다. <br/>
사용법은 아래와 같다.<br/>

How to use : 
```shell
# default controller
#shell:> make:controller {controllerName}
shell:> make:controller ProductController

# rest controller
#shell:> make:controller {controllerName} rest
shell:> make:controller UserController rest
```

output : 
```java
// default controller
package kim.mingyu.javacommand.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController
{
    @Autowired
    public ProductController() {
        // 생성자
    }

    @GetMapping("/product")
    public String index() {
        return "";
    }

}
```
```java
// rest controller
package kim.mingyu.javacommand.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController
{

    private final ExampleRepository repository;

    public UserController(ExampleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Example> all() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Example new(@RequestBody Example newExample) {
    return repository.save(newExample);
}

    @GetMapping("/{id}")
    public Example one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ExampleNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Example replaceExample(@RequestBody Example newExample, @PathVariable Long id) {
        return repository.findById(id)
                .map(example -> {
                    example.setName(newExample.getName());
                    example.setPrice(newExample.getPrice());
                    return repository.save(example);
                })
                .orElseGet(() -> {
                    newExample.setId(id);
                    return repository.save(newExample);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteExample(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
```

## service 만들기
만들어진 service는 config.properties에서 선언한 package 및 path로 생성된다. <br/>
사용법은 아래와 같다.<br/>

how to use :
```shell
#shell:> make:service {serviceNmae}
shell:> make:service UserService
```
output :
```java
package kim.mingyu.javacommand.service;

public class UserService
{
    public UserService() {
        // 생성자
    }
}
```

## command 만들기
만들어진 command는 config.properties에서 선언한 package 및 path로 생성된다. <br/>
커맨드 클래스를 생성하여 사용할 경우, `org.springframework.shell:spring-shell-starter` dependency를 추가해야한다.<br/>
사용법은 아래와 같다.<br/>

How to use :
```shell
#shell:> make:command {commandNmae}
shell:> make:command ViewCommand
```
output : 
```java
package kim.mingyu.javacommand.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ShellComponent
public class ViewCommand {

    @Value("${project.root.path}")
    private String projectRootPath;

    @Value("${project.view.package}")
    private String viewPackage;

    @Value("${project.view.package.path}")
    private String viewPackagePath;

    @ShellMethod(key = "make:view", value="view creation command")
    public boolean makeViewCommand(@ShellOption(help = "Name for the command", defaultValue = "") String viewName) {

        if (viewName.trim().length() < 1) {
            System.out.println("You must have viewName option");
            return false;
        }

        return true;
    }
}
```

## entity 만들기
만들어진 entity는 config.properties에서 선언한 package 및 path로 생성된다. <br/>
커맨드 클래스를 생성하여 사용할 경우, `org.springframework.boot:spring-boot-starter-data-jpa` dependency를 추가해야한다.<br/>
사용법은 아래와 같다.<br/>

How to use : 
```shell
# shell:> make:entity {entityNmae} {entityType}
shell:> make:entity User jpa

자료형을 입력해주세요. 그만 입력하고 싶을 경우 exit 를 입력하세요. ex) String,Long,Integer,Float 등 :: 
Long # String, Bollen, Float, Integer, Double, Long 등 입력
변수명을 입력하세요 :: 
id # 사용할 컬럼에 해당하는 변수명
자료형을 입력해주세요. 그만 입력하고 싶을 경우 exit 를 입력하세요. ex) String,Long,Integer,Float 등 :: 
exit # 그만 입력하고 싶은 경우 exit 입력
```
output :
```java
package kim.mingyu.javacommand.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private Long id;


    // getters and setters
}
```