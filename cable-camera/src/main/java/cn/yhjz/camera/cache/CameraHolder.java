package cn.yhjz.camera.cache;

import cn.yhjz.camera.entity.AbstractCamera;
import cn.yhjz.camera.entity.Camera;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 摄像头的缓存，保存着一个网络区域下的所有摄像头
 *
 * @author 马国平
 */
public class CameraHolder {

    private CameraHolder() {
    }

    /**
     * 摄像头列表
     */
    public static List<AbstractCamera> cameraList = null;

    /**
     * 创建一个线程用于循环获得摄像头的图像，这里缓存每个线程
     */
    public Map<String, Thread> previewThreadMap = new ConcurrentHashMap<>();


    public Map<String, Thread> ptzThreadMap = new ConcurrentHashMap<>();

}
