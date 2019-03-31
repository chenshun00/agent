package top.huzhurong.plugin.impl;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;
import top.huzhurong.plugin.impl.intercepter.MysqlHook;
import top.huzhurong.test.other.asm.BeanAgentHook;
import top.huzhurong.test.other.asm.TraceClassWriter;
import top.huzhurong.test.other.asm.context.AbstractContext;

import java.io.*;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/27
 */
public class App {
    public static void main(String[] args) throws IOException {
        String pp = "/Users/chenshun/work/chat-plugin-parent";
        File file = new File(pp);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });
        for (File file1 : files) {
            String path = pp + "/" + file1.getName();
            System.out.println(path);
            InputStream inputStream = new FileInputStream(path);
            int available = inputStream.available();
            byte[] bytes = new byte[available];
            inputStream.read(bytes);
//        ClassReader classReader = new ClassReader(inputStream);
//        ClassWriter classWriter = new TraceClassWriter(classReader, ClassWriter.COMPUTE_MAXS, App.class.getClassLoader());
//        classReader.accept(new BeanAgentHook(Opcodes.ASM7, classWriter, MysqlHook.Instance, "executeInternal"), ClassReader.EXPAND_FRAMES);
            PrintWriter pw = new PrintWriter(System.out);
//        AbstractContext.writeToFile(classWriter, "ViewController");
            CheckClassAdapter.verify(new ClassReader(bytes), false, pw);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
