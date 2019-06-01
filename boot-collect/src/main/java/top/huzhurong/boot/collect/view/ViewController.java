package top.huzhurong.boot.collect.view;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2019/4/4
 */
@Controller
@Slf4j
public class ViewController {

    private static SerializeConfig mapping = new SerializeConfig();

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd'T'HH:mm:ssZZ"));
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    @Resource
    private Client client;

    @PostMapping("/info")
    @ResponseBody
    public String user(String json) {
        log.info("json:[{}]", json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> param = new HashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            param.put(entry.getKey(), entry.getValue());
        }
        param.put("date", new Date());
        IndexResponse response = client.prepareIndex("collect-info", "agent")
                .setSource(JSONObject.toJSONString(param, mapping), XContentType.JSON)
                .get();
        return response.getId();
    }


}
