package cn.yhjz.bi.domain;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 看板与报关联对象 bi_report_paniter_mapping
 *
 * @author yhjz
 * @date 2022-01-13
 */
public class BiReportPaniterMapping extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 报表唯一码 */
    @Excel(name = "报表唯一码")
    private String rmCode;

    /** 看板唯一表 */
    @Excel(name = "看板唯一表")
    private String pmCode;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setRmCode(String rmCode)
    {
        this.rmCode = rmCode;
    }

    public String getRmCode()
    {
        return rmCode;
    }
    public void setPmCode(String pmCode)
    {
        this.pmCode = pmCode;
    }

    public String getPmCode()
    {
        return pmCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("rmCode", getRmCode())
                .append("pmCode", getPmCode())
                .toString();
    }
}
