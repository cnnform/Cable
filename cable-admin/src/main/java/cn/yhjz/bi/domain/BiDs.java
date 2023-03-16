package cn.yhjz.bi.domain;

import java.util.Date;
import java.util.List;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据源管理对象 bi_ds
 * 
 * @author yhjz
 * @date 2022-01-04
 */
public class BiDs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long dsId;

    /** 名称 */
    @Excel(name = "名称")
    private String dsName;

    /** 类型 */
    @Excel(name = "类型")
    private String dsType;

    /** IP */
    @Excel(name = "IP")
    private String dsIp;

    /** 端口 */
    @Excel(name = "端口")
    private String dsPort;

    /** 数据库名称 */
    @Excel(name = "数据库名称")
    private String dsDbName;

    /** 用户名 */
    @Excel(name = "用户名")
    private String dsUsername;

    /** 密码 */
    @Excel(name = "密码")
    private String dsPassword;

    /** 编码 */
    @Excel(name = "编码")
    private String dsCode;

    /** 备注 */
    @Excel(name = "备注")
    private String dsRemark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dsCreateTime;

    /** SQL存储信息 */
    private List<BiSql> biSqlList;

    public void setDsId(Long dsId)
    {
        this.dsId = dsId;
    }

    public Long getDsId()
    {
        return dsId;
    }
    public void setDsName(String dsName)
    {
        this.dsName = dsName;
    }

    public String getDsName()
    {
        return dsName;
    }
    public void setDsType(String dsType)
    {
        this.dsType = dsType;
    }

    public String getDsType()
    {
        return dsType;
    }
    public void setDsIp(String dsIp)
    {
        this.dsIp = dsIp;
    }

    public String getDsIp()
    {
        return dsIp;
    }
    public void setDsPort(String dsPort)
    {
        this.dsPort = dsPort;
    }

    public String getDsPort()
    {
        return dsPort;
    }
    public void setDsDbName(String dsDbName)
    {
        this.dsDbName = dsDbName;
    }

    public String getDsDbName()
    {
        return dsDbName;
    }
    public void setDsUsername(String dsUsername)
    {
        this.dsUsername = dsUsername;
    }

    public String getDsUsername()
    {
        return dsUsername;
    }
    public void setDsPassword(String dsPassword)
    {
        this.dsPassword = dsPassword;
    }

    public String getDsPassword()
    {
        return dsPassword;
    }
    public void setDsCode(String dsCode)
    {
        this.dsCode = dsCode;
    }

    public String getDsCode()
    {
        return dsCode;
    }
    public void setDsRemark(String dsRemark)
    {
        this.dsRemark = dsRemark;
    }

    public String getDsRemark()
    {
        return dsRemark;
    }
    public void setDsCreateTime(Date dsCreateTime)
    {
        this.dsCreateTime = dsCreateTime;
    }

    public Date getDsCreateTime()
    {
        return dsCreateTime;
    }

    public List<BiSql> getBiSqlList()
    {
        return biSqlList;
    }

    public void setBiSqlList(List<BiSql> biSqlList)
    {
        this.biSqlList = biSqlList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("dsId", getDsId())
                .append("dsName", getDsName())
                .append("dsType", getDsType())
                .append("dsIp", getDsIp())
                .append("dsPort", getDsPort())
                .append("dsDbName", getDsDbName())
                .append("dsUsername", getDsUsername())
                .append("dsPassword", getDsPassword())
                .append("dsCode", getDsCode())
                .append("dsRemark", getDsRemark())
                .append("dsCreateTime", getDsCreateTime())
                .append("biSqlList", getBiSqlList())
                .toString();
    }
}
