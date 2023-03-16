package cn.yhjz.bi.domain;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * SQL存储对象 bi_sql
 * 
 * @author yhjz
 * @date 2022-01-05
 */
public class BiSql extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** SQL主键 */
    private Long bsId;

    /** 数据源Key */
    @Excel(name = "数据源Key")
    private Long dsId;

    @Excel(name = "SQL名称")
    private String bsName;

    /** 名称 */
    @Excel(name = "数据源名称")
    private String dsName;

    /** 动态查询SQL */
    @Excel(name = "动态查询SQL")
    private String bsDynSql;

    @Excel(name = "注释")
    private String bsNotes;

    /** 是否列表，默认为0 */
    @Excel(name = "是否列表，默认为0")
    private Long isList;

    /** 是否分页，默认为0 */
    @Excel(name = "是否分页，默认为0")
    private Long isPage;

    public void setBsId(Long bsId)
    {
        this.bsId = bsId;
    }

    public Long getBsId()
    {
        return bsId;
    }
    public void setDsId(Long dsId)
    {
        this.dsId = dsId;
    }

    public Long getDsId()
    {
        return dsId;
    }
    public void setBsName(String bsName)
    {
        this.bsName = bsName;
    }

    public String getBsName()
    {
        return bsName;
    }
    public void setDsName(String dsName)
    {
        this.dsName = dsName;
    }

    public String getDsName() { return dsName; }
    public void setBsDynSql(String bsDynSql)
    {
        this.bsDynSql = bsDynSql;
    }

    public String getBsDynSql()
    {
        return bsDynSql;
    }
    public void setIsList(Long isList)
    {
        this.isList = isList;
    }

    public String getBsNotes() { return bsNotes; }
    public void setBsNotes(String bsNotes) {
        this.bsNotes = bsNotes;
    }

    public Long getIsList()
    {
        return isList;
    }
    public void setIsPage(Long isPage)
    {
        this.isPage = isPage;
    }

    public Long getIsPage()
    {
        return isPage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("bsId", getBsId())
                .append("dsId", getDsId())
                .append("bsName", getBsName())
                .append("dsName", getDsName())
                .append("bsDynSql", getBsDynSql())
                .append("bsNotes", getBsNotes())
                .append("isList", getIsList())
                .append("isPage", getIsPage())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
