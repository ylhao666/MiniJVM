package load;

import load.custom.classloader.test.ChildFirstClassLoader;
import load.thread.context.classloader.test.PluginLoader;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

/**
 * 线程上下文类加载器，用于父加载器使用子加载器进行类加载，打破双亲委派机制
 */
public class ThreadContextClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        PluginLoader pluginLoader = new PluginLoader();
        ChildFirstClassLoader pluginClassLoader =
            new ChildFirstClassLoader(Path.of("F:\\Projects\\MiniJVM\\Load\\tmpSrc\\"));
        Thread.currentThread().setContextClassLoader(pluginClassLoader);
        // 成功通过子类加载器加载类
        pluginLoader.load("load.thread.context.classloader.plugin.CustomPlugin");
    }
}
