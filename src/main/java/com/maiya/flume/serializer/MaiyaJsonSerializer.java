package com.maiya.flume.serializer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.EventSerializer;
import org.apache.flume.source.kafka.KafkaSourceConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;


/**
 * 处理kafka队列里面的JSON数据格式，提取data里面的具体内容写入OutputStream
 * {
 * "table_name": "hy_membercallhistory",
 * "data": [        {
 * "sGuid": "40464ce75eec412a9b026113589add38",
 * "sUserId": "e219866f603e46769a7b822294cb3eb4",
 * "sUserNo": "170905242600000002",
 * "sDeviceNo": "",
 * "sCallingMobile": "",
 * "sCalledMobile": "95589",
 * "iTalkTime": "0",
 * "dTalkDate": "20170825094727",
 * "iTalkResult": 1,
 * "iDelFlag": 1,
 * "sAddPerson": "",
 * "dAddDate": "2017-09-05 10:34:25",
 * "sModifyPerson": "",
 * "dModifyDate": "",
 * "sName": ""
 * },
 * {
 * "sGuid": "72920d0c87784db7ad3f367239c15717",
 * "sUserId": "e219866f603e46769a7b822294cb3eb4",
 * "sUserNo": "170905242600000002",
 * "sDeviceNo": "",
 * "sCallingMobile": "",
 * "sCalledMobile": "13813893883",
 * "iTalkTime": "4",
 * "dTalkDate": "20170417105259",
 * "iTalkResult": 0,
 * "iDelFlag": 1,
 * "sAddPerson": "",
 * "dAddDate": "2017-09-05 10:34:25",
 * "sModifyPerson": "",
 * "dModifyDate": "",
 * "sName": ""
 * }  ],
 * "table_status": "insert"
 * }
 *
 * @author wuxiang
 */
public class MaiyaJsonSerializer implements EventSerializer {


    private final static String FILE_ENCODING = "UTF-8";
    private static final Logger logger =
            LoggerFactory.getLogger(MaiyaJsonSerializer.class);


    private final OutputStream out;

    private MaiyaJsonSerializer(OutputStream out, Context ctx) {
        this.out = out;
    }

    public boolean supportsReopen() {
        return true;
    }

    public void afterCreate() {
        // noop
    }

    public void afterReopen() {
        // noop
    }

    public void beforeClose() {
        // noop
    }

    public void write(Event e) throws IOException {
        StringBuffer sb = new StringBuffer(100);

        try {
            String header = e.getHeaders().get(KafkaSourceConstants.TOPIC_HEADER).concat(":")
                    .concat(e.getHeaders().get(KafkaSourceConstants.PARTITION_HEADER));
            String body = new String(e.getBody(), FILE_ENCODING);
            JSONObject jsonObj = JSONObject.fromObject(body);
            JSONArray dataArr = jsonObj.getJSONArray("data");
            String tableName = jsonObj.getString("table_name");
            sb.append("process data，table_name:").append(tableName);

            String tableStatus = jsonObj.getString("table_status");
            sb.append("\ttable_status:").append(tableStatus);

            int dataSize = jsonObj.getJSONArray("data").size();
            sb.append("\tsize:").append(dataSize);

            for (int i = 0; i < dataArr.size(); i++) {
                JSONObject data = dataArr.getJSONObject(i);
                data.put("consume_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss.S"));
                /*if (i == 0) {
                    String sUserId = data.getString("sUserId");
                    sb.append("\tuserid:").append(sUserId);
                }*/
//                data.put("header", header);
                String dataStr = data.toString().replaceAll("\n", "") + "\n";
                out.write(dataStr.getBytes());
            }
            logger.debug(sb.toString());
        } catch (Exception e1) {
            logger.error("process data error:" + sb.toString() + new String(e.getBody(), FILE_ENCODING), e1);
            if (e1 instanceof IOException) {
                throw (IOException) e1;
            }
        }
    }

    public void flush() throws IOException {
        // noop
        out.flush();
    }

    public static class Builder implements EventSerializer.Builder {
        public EventSerializer build(Context context, OutputStream out) {
            return new MaiyaJsonSerializer(out, context);
        }
    }
}
