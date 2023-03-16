package cn.yhjz.biz.utils;

/**
 * @author yhjz
 * @desc 经纬度工具类
 */
public class LonAndLatUtil {

    /**
     * 根据经纬度计算两点的距离
     *
     * @param lon_a > 坐标1经度
     * @param lat_a > 坐标1维度
     * @param lon_b > 坐标2经度
     * @param lat_b > 坐标1维度
     * @return
     */
    public static double distance(double lon_a, double lat_a, double lon_b, double lat_b) {

        double a, b, R;
        // 地球半径
        R = 6378137;
        lat_a = lat_a * Math.PI / 180.0;
        lat_b = lat_b * Math.PI / 180.0;
        a = lat_a - lat_b;
        b = (lon_a - lon_b) * Math.PI / 180.0;
        double d = 2 * R * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat_a) * Math.cos(lat_b) * Math.pow(Math.sin(b / 2), 2)));
        return d;
    }

    /**
     * 计算经纬度角度
     * 计算坐标2位于坐标1的方位
     *
     * @param lon_a > 坐标1经度
     * @param lat_a > 坐标1维度
     * @param lon_b > 坐标2经度
     * @param lat_b > 坐标2维度
     * @return
     */
    public static double bearing(double lon_a, double lat_a, double lon_b, double lat_b) {
        double dRotateAngle = Math.atan2(Math.abs(lon_a - lon_b), Math.abs(lat_a - lat_b));
        if (lon_b >= lon_a) {
            if (lat_b >= lat_a) {
            } else {
                dRotateAngle = Math.PI - dRotateAngle;
            }
        } else {
            if (lat_b >= lat_a) {
                dRotateAngle = 2 * Math.PI - dRotateAngle;
            } else {
                dRotateAngle = Math.PI + dRotateAngle;
            }
        }
        dRotateAngle = dRotateAngle * 180 / Math.PI;
        return dRotateAngle;
    }


    // 计算方位角,正北向为0度，以顺时针方向递增
    private static double computeAzimuth(double lon_a, double lat_a, double lon_b, double lat_b) {
        double lat1 = lat_a, lon1 = lon_a, lat2 = lat_b,
                lon2 = lon_b;
        double result = 0.0;

        int ilat1 = (int) (0.50 + lat1 * 360000.0);
        int ilat2 = (int) (0.50 + lat2 * 360000.0);
        int ilon1 = (int) (0.50 + lon1 * 360000.0);
        int ilon2 = (int) (0.50 + lon2 * 360000.0);

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        if ((ilat1 == ilat2) && (ilon1 == ilon2)) {
            return result;
        } else if (ilon1 == ilon2) {
            if (ilat1 > ilat2) {
                result = 180.0;
            }
        } else {
            double c = Math
                    .acos(Math.sin(lat2) * Math.sin(lat1) + Math.cos(lat2)
                            * Math.cos(lat1) * Math.cos((lon2 - lon1)));
            double A = Math.asin(Math.cos(lat2) * Math.sin((lon2 - lon1))
                    / Math.sin(c));
            result = Math.toDegrees(A);
            if ((ilat2 > ilat1) && (ilon2 > ilon1)) {
            } else if ((ilat2 < ilat1) && (ilon2 < ilon1)) {
                result = 180.0 - result;
            } else if ((ilat2 < ilat1) && (ilon2 > ilon1)) {
                result = 180.0 - result;
            } else if ((ilat2 > ilat1) && (ilon2 < ilon1)) {
                result += 360.0;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        double lon_a = 112;
        double lat_a = 22;
        double lon_b = 113;
        double lat_b = 22;
//        double lon_b = 120.429277;
//        double lat_b = 36.166539;
//        double lon_a = 120.431723;
//        double lat_a = 36.166556;
        double a = distance(lon_a, lat_a, lon_b, lat_b);
        double b = bearing(lon_a, lat_a, lon_b, lat_b);
        double c = computeAzimuth(lon_a, lat_a, lon_b, lat_b);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }

}
