package cn.yhjz.bi.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源
 *
 * @author ldl
 */
public class DsBaseVo {

    public static Map<String, DsBaseVo> dsBaseInit() {
        DsBaseVo mysql = new DsBaseVo();
        mysql.setDbType("mysql");
        mysql.setDriver("com.mysql.cj.jdbc.Driver");
        mysql.setUrl("jdbc:mysql://ip:port/dbname");

        Map<String, DsBaseVo> dsBaseUtilMap = new HashMap<>();
        dsBaseUtilMap.put("mysql", mysql);
        return dsBaseUtilMap;
    }

    private String DbType;

    private String Driver;

    private String Url;

    public String getDbType() {
        return DbType;
    }
    public void setDbType(String dbType) {
        DbType = dbType;
    }

    public String getDriver() {
        return Driver;
    }
    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getUrl() {
        return Url;
    }
    public void setUrl(String url) {
        Url = url;
    }

}
