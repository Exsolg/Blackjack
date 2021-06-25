package NetworkTools;

import java.io.Serializable;

public class Message implements Serializable {
    private String msg;
    private Object obj;
    private Object obj2;

    public Message(String msg) {
        this.msg = msg;
    }

    public Message(String msg, Object obj) {
        this.msg = msg;
        this.obj = obj;
    }

    public Message(String msg, Object obj, Object obj2) {
        this.msg = msg;
        this.obj = obj;
        this.obj2 = obj2;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getObj() {
        return this.obj;
    }

    public Object getObj2() {
        return this.obj2;
    }
}
