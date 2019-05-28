package top.huzhurong.test.common.trace;


import java.util.EmptyStackException;
import java.util.Vector;

/**
 * 通过修改stack的实现来达到这个目的，在span内部使用一个index用来查找数据，pop取出来修改数据，到tomcat/dubbo中进行一次统一的Span处理
 *
 * @author chenshun00@gmail.com
 * @since 2019/5/27
 */
public class BuilderStack<E> extends Vector<E> {
    /**
     * Creates an empty Stack.
     */
    public BuilderStack() {
    }

    /**
     * Pushes an item onto the top of this stack. This has exactly
     * the same effect as:
     * <blockquote><pre>
     * addElement(item)</pre></blockquote>
     *
     * @param item the item to be pushed onto this stack.
     * @return the <code>item</code> argument.
     * @see java.util.Vector#addElement
     */
    public int push(E item) {
        addElement(item);
        return size();
    }

    /**
     * Removes the object at the top of this stack and returns that
     * object as the value of this function.
     *
     * @return The object at the top of this stack (the last item
     * of the <tt>Vector</tt> object).
     * @throws EmptyStackException if this stack is empty.
     */
    public synchronized E pop(int i) {
        return (E) elementData[i];
    }


    public synchronized E getOne() {
        E obj;
        int len = size();
        obj = peek();
        removeElementAt(len - 1);
        return obj;
    }

    /**
     * Looks at the object at the top of this stack without removing it
     * from the stack.
     *
     * @return the object at the top of this stack (the last item
     * of the <tt>Vector</tt> object).
     * @throws EmptyStackException if this stack is empty.
     */
    public synchronized E peek() {
        int len = size();

        if (len == 0)
            throw new EmptyStackException();
        return elementAt(len - 1);
    }

    /**
     * Tests if this stack is empty.
     *
     * @return <code>true</code> if and only if this stack contains
     * no items; <code>false</code> otherwise.
     */
    public boolean empty() {
        return size() == 0;
    }

    /**
     * Returns the 1-based position where an object is on this stack.
     * If the object <tt>o</tt> occurs as an item in this stack, this
     * method returns the distance from the top of the stack of the
     * occurrence nearest the top of the stack; the topmost item on the
     * stack is considered to be at distance <tt>1</tt>. The <tt>equals</tt>
     * method is used to compare <tt>o</tt> to the
     * items in this stack.
     *
     * @param o the desired object.
     * @return the 1-based position from the top of the stack where
     * the object is located; the return value <code>-1</code>
     * indicates that the object is not on the stack.
     */
    public synchronized int search(Object o) {
        int i = lastIndexOf(o);

        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }

    /**
     * use serialVersionUID from JDK 1.0.2 for interoperability
     */
    private static final long serialVersionUID = 1224463164541339165L;
}
