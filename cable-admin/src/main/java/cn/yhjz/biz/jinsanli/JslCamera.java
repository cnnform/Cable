package cn.yhjz.biz.jinsanli;

import cn.yhjz.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 金三立摄像头对应的类
 * 内部保证token对接和更新
 * 把摄像头的接口封装成对外方法
 */
@Data
@Slf4j
public class JslCamera {

    public static ConcurrentHashMap<String, JslCamera> jslCameraMap = new ConcurrentHashMap<>();

    public static Map<String, String> directionMap = new HashMap<>();

    static {
        directionMap.put("up", "TU");
        directionMap.put("down", "TD");
        directionMap.put("left", "PL");
        directionMap.put("right", "PR");
    }

    private JslConfig jslConfig;

    private String platformUrl;

    private String deviceId;
    private String guid;
    private String token;

    private String refreshToken;

    private String clientId;

    private String clientSecret;

    private String username;
    private String password;

    /**
     * 初始化
     */
    public void init(JslConfig jslConfig, String guid, String clientId, String clientSecret) {
        this.jslConfig = jslConfig;
        this.guid = guid;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.platformUrl = jslConfig.getProtocol() + "://" + jslConfig.getIp() + ":" + jslConfig.getPort();
        this.fetchToken();
    }

    /**
     * 获取token
     *
     * @return
     */
    private void fetchToken() {
        try {
            //连接摄像头获得token
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType,
                    "username=" + username + "&password=" + password + "&grant_type=password");
            okhttp3.Request request = new Request.Builder()
                    .url(this.platformUrl + "/oauth/token")
                    .method("POST", body)
                    .addHeader("Authorization", Credentials.basic(this.clientId, this.clientSecret))
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            String bodyString = response.body().string();
            JSONObject bodyJson = JSONObject.parseObject(bodyString);
            String accessToken = bodyJson.getString("access_token");
            String refreshToken = bodyJson.getString("refresh_token");
            this.token = accessToken;
            this.refreshToken = refreshToken;
        } catch (IOException exception) {
            log.error("获取摄像头token失败，{}", exception);
            this.token = null;
            this.refreshToken = null;
        }
    }

    /**
     * 旋转摄像头
     *
     * @param direct
     * @param stop
     */
    public void turn(String direct, String stop) {
        log.info("{},{}", direct, stop);
        //先检查是否获取了token，
        if (StringUtils.isEmpty(this.token)) {
            this.fetchToken();
        }
        String direction = directionMap.get(direct);
        if (null != direction) {
            int resCode = this.ptzCtrl(direction, stop);
            if (resCode != 200) {
                this.fetchToken();
                this.ptzCtrl(direction, stop);
            }
        }
    }

    /**
     * 对应云台控制接口
     *
     * @return
     */
    public int ptzCtrl(String direction, String stop) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("guid", this.guid)
                    .addFormDataPart("ptzcmd", direction)
//                    .addFormDataPart("presetIndex", "0") // 预置位
                    .addFormDataPart("speed", "4")
                    .addFormDataPart("ptzlevel", "5") // 云台等级，不知道啥玩意儿
                    .addFormDataPart("isstop", stop)
                    .build();
            Request request = new Request.Builder()
                    .url(this.platformUrl + "/ptz/ctrl")
                    .method("POST", body)
                    .addHeader("Authorization", "bearer " + this.token).build();
            Response response = client.newCall(request).execute();
            log.info(response.body().string());
            if (response.code() != 200) {
                log.error("控制云台失败,{}", response.body().string());
                return response.code();
            }
            return 200;
        } catch (IOException exception) {
            log.error("{}", exception);
            return 500;
        }
    }

}
