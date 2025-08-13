package load;

/**
 * 双亲委派机制
 */
public class ParentDelegationModelTest {
    public static void main(String[] args) {
        ClassLoader loader = ParentDelegationModelTest.class.getClassLoader();
        // jdk.internal.loader.ClassLoaders$AppClassLoader，应用类加载器
        System.out.println("Current ClassLoader: " + loader);
        // jdk.internal.loader.ClassLoaders$PlatformClassLoader，平台类加载器（拓展类加载器）
        System.out.println("Parent ClassLoader: " + loader.getParent());
        // null，启动类加载器无法获取，为 C++ 实现
        System.out.println("Parent Parent ClassLoader：" + loader.getParent().getParent());
    }
}
