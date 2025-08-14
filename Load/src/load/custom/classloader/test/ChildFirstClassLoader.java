package load.custom.classloader.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChildFirstClassLoader extends ClassLoader {
    // 查找字节码文件的路径
    private final Path loadLocation;

    public ChildFirstClassLoader(Path loadLocation) {
        this.loadLocation = loadLocation;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // 先判断类是否已加载，已加载则直接返回
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            resolveClass(loadedClass);
            return loadedClass;
        }

        try {
            // 先尝试自己加载，自己加载不到再交给父类加载，打破双亲委派机制
            loadedClass = findClass(name);
            if (loadedClass != null) {
                resolveClass(loadedClass);
                return loadedClass;
            }
        } catch (ClassNotFoundException e) {
            // 自身找不到，再交给父加载器加载
        }

        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 将包名转换为路径名，并拼接上 .class 后缀
        String byteCodeFileName = name.replace(".", "/") + ".class";
        Path path = loadLocation.resolve(byteCodeFileName);
        // 找不到字节码文件，则直接抛出异常
        if (!Files.exists(path)) {
            throw new ClassNotFoundException();
        }

        // 通过文件流加载得到字节码文件
        try (InputStream inputStream = Files.newInputStream(path)) {
            byte[] byteCode = inputStream.readAllBytes();
            return defineClass(name, byteCode, 0, byteCode.length);
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }
}
