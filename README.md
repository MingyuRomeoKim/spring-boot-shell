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
contoller 클래스를 생성하여 사용할 경우 `org.springframework.boot:spring-boot-starter-web`를 주입받아야 한다. <br/>
사용법은 아래와 같다.<br/>
1. default controller 클래스 생성 방법
```shell
shell:> make:controller {controllerNmae}
```
2. rest controller 클래스 생성 방법
```shell
shell:> make:controller {controllerNmae} rest
```

## service 만들기
만들어진 service는 config.properties에서 선언한 package 및 path로 생성된다. <br/>
사용법은 아래와 같다.<br/>
1. default service 클래스 생성 방법
```shell
shell:> make:service {serviceNmae}
```

## command 만들기
만들어진 command는 config.properties에서 선언한 package 및 path로 생성된다. <br/>
커맨드 클래스를 생성하여 사용할 경우, `org.springframework.shell:spring-shell-starter`를 주입받아야 한다.<br/>
사용법은 아래와 같다.<br/>
1. default command 클래스 생성 방법
```shell
shell:> make:command {commandNmae}
```