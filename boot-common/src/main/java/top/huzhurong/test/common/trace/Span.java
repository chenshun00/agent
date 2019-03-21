package top.huzhurong.test.common.trace;

/**
 * @author chenshun00@gmail.com
 * @since 2019/3/3
 */
public class Span {
    private String spanId;
    private String pspanId = "-1";
    private String tag;
    private String url;
    private long sTime;
    private long eTime;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public long geteTime() {
        return eTime;
    }

    public void seteTime(long eTime) {
        this.eTime = eTime;
    }
}
