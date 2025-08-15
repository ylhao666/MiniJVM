package load.thread.context.classloader.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 插件加载器，通过 SPI 机制加载插件
 */
public class PluginLoader {
    public void load(String pluginName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 从线程上下文里获取类加载器
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            System.out.println("Threat context class loader not exist.");
            return;
        }

        // 使用子类加载器加载类
        Class<?> loadedClass = contextClassLoader.loadClass(pluginName);
        if (loadedClass == null) {
            System.out.println("Load plugin failed.");
            return;
        }

        Constructor<?> constructor = loadedClass.getDeclaredConstructor();
        Object instance = constructor.newInstance();
        Method actionMethod = loadedClass.getMethod("action");
        actionMethod.invoke(instance);
    }
}
