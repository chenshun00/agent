package top.huzhurong.test.other.asm.context;

import top.huzhurong.test.bootcore.asm.ASMContext;
import org.objectweb.asm.ClassWriter;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/1
 */
public abstract class AbstractContext implements ASMContext {
    public static void writeToFile(ClassWriter classWriter, String name) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(name.replaceAll("/", ".") + ".class");
            fileOutputStream.write(classWriter.toByteArray());
            fileOutputStream.close();
        } catch (IOException ignore) {

        }
    }
}
