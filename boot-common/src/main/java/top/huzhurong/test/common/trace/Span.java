package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Span {
    private String spanId;
    private String pspanId;
    private String tag;
    private String sTime;
    private String eTime;

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getPspanId() {
        return pspanId;
    }

    public void setPspanId(String pspanId) {
        this.pspanId = pspanId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }
}
