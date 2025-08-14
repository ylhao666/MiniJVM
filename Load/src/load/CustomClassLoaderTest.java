package load;

import load.custom.classloader.test.ChildFirstClassLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

/**
 * 自定义类加载器的实现
 */
public class CustomClassLoaderTest {
    // 加载不同版本的同一类，打破双亲委派机制
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String classFullName = "load.custom.classloader.test.SomeClass";
        String methodName = "saySomething";
        // 使用应用类加载器，加载得到的是当前 class 路径下的类
        ClassLoader appClassLoader = CustomClassLoaderTest.class.getClassLoader();
        Class<?> appLoadedClass = appClassLoader.loadClass(classFullName);
        Constructor<?> constructor1 = appLoadedClass.getDeclaredConstructor();
        Object obj1 = constructor1.newInstance();
        Method method1 = appLoadedClass.getMethod(methodName);
        System.out.println("ClassLoader: " + appClassLoader);
        System.out.println("Invoke: " + method1.invoke(obj1));

        // 使用自定义类加载器，优先加载了指定目录下的
        ChildFirstClassLoader childFirstClassLoader =
            new ChildFirstClassLoader(Path.of("F:\\Projects\\MiniJVM\\Load\\tmpSrc\\"));
        Class<?> childLoadedClass = childFirstClassLoader.loadClass(classFullName);
        Constructor<?> constructor2 = childLoadedClass.getDeclaredConstructor();
        Object obj2 = constructor2.newInstance();
        Method method2 = childLoadedClass.getMethod(methodName);
        System.out.println("ClassLoader: " + childFirstClassLoader);
        System.out.println("Invoke: " + method2.invoke(obj2));

        // 两个相同名字的类，但经过不同的类加载器加载，所以不是同一个类对象
        System.out.println("childLoadedClass = appLoadedClass ? " + (childLoadedClass == appLoadedClass));

        // 同一个类，使用不同的类加载器加载，得到的是不同的类对象
        ChildFirstClassLoader childFirstClassLoader2 =
                new ChildFirstClassLoader(Path.of("F:\\Projects\\MiniJVM\\Load\\tmpSrc\\"));
        Class<?> childLoadedClass2 = childFirstClassLoader2.loadClass(classFullName);
        System.out.println("childLoadedClass = childLoadedClass2 ? " + (childLoadedClass == childLoadedClass2));
    }
}
