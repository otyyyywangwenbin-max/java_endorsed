1. 先编译本项目      
```sh
mvn clean package
```     

2. 进入 target 目录     
```sh
cd target 
```     
 
3. 选择合适的JDK版本(需要和实际程序运行的JDK版本一致), 设置JAVA_HOME, 进入target目录, 执行     
```sh
java -jar generator.jar
```
执行命令后, 会在当前目录的generated目录下生成新的java.io.ObjectInputStream相关的Class
 
4. 将生成的代码打包     
```sh
 cd generated &&  zip ../primeton-object-input-stream.jar -r * && cd ..
```

5. 将 primeton-object-input-stream*.jar 复制到一个其他目录, 如 `/opt/primeton/java_endorsed`

6. 在启动脚本里 增加  
```sh 
-Djava.endorsed.dirs=/opt/primeton/java_endorsed
```

