package cn.yhjz.nio.camera;

import lombok.extern.slf4j.Slf4j;

/**
 * ptz和角度转化的工具类
 *
 * @author maguoping
 */
@Slf4j
public class PtzUtil {

    /**
     * 把ptz参数转换成真实角度，供地图上显示
     *
     * @param p         摄像机返回的p
     */
    public static Double convertPTZ2degreeP(Double p) {
        //p的取值范围是[-1,1]
        //rootAngle是摄像头云台P=0时的真实指向，正北为0，顺时针为正

        double delta = 0.0;
        if (p > 0) {
            delta = p / 1 * 180;
        }
        if (p < 0) {
            delta = (1 - (-p)) / 1 * 180 + 180;
        }
        return delta ;
    }

    public static Long convertPTZ2degreeT(Double t){
        return Math.round(Math.abs(t) / ((double) 1 / 45));
    }

    public static void main(String[] args) {
        Double res = convertPTZ2degreeP(0.5);
        log.info("{}", res);
    }
}
