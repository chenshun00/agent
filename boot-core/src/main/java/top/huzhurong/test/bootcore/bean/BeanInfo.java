package top.huzhurong.test.bootcore.bean;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/26
 */
public class BeanInfo {
    private String className;
    private String methodName;

    public BeanInfo(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return className + ":" + methodName;
    }
}
