package cn.yhjz.bi.domain;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * BI报对象 bi_report_manager
 *
 * @author yhjz
 * @date 2022-01-12
 */
public class BiReportManager extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long rmId;

    /** 唯一码 */
    @Excel(name = "唯一码")
    private String rmCode;

    /** 报表名称 */
    @Excel(name = "报表名称")
    private String rmName;

    /** 报表描述 */
    @Excel(name = "报表描述")
    private String rmDesc;

    /** 关联sql */
    @Excel(name = "关联sql")
    private Long bsId;

    /** SQL名称 */
    @Excel(name = "SQL名称")
    private String bsName;

    /** 报表类型 */
    @Excel(name = "报表类型")
    private String rmType;

    /** 报表配置 */
    @Excel(name = "报表配置")
    private String configOption;

    /** 数据状态 */
    @Excel(name = "数据状态")
    private Long status;

    /** 创建用户 */
    @Excel(name = "创建用户")
    private String createUser;

    /** 更新时间 */
    @Excel(name = "更新用户")
    private String updateUser;

    public void setRmId(Long rmId)
    {
        this.rmId = rmId;
    }

    public Long getRmId()
    {
        return rmId;
    }
    public void setRmCode(String rmCode)
    {
        this.rmCode = rmCode;
    }

    public String getRmCode()
    {
        return rmCode;
    }
    public void setRmName(String rmName)
    {
        this.rmName = rmName;
    }

    public String getRmName()
    {
        return rmName;
    }
    public void setRmDesc(String rmDesc)
    {
        this.rmDesc = rmDesc;
    }

    public String getRmDesc()
    {
        return rmDesc;
    }
    public void setBsId(Long bsId)
    {
        this.bsId = bsId;
    }

    public Long getBsId()
    {
        return bsId;
    }
    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public String getBsName() { return bsName; }
    public void setRmType(String rmType)
    {
        this.rmType = rmType;
    }

    public String getRmType()
    {
        return rmType;
    }
    public void setConfigOption(String configOption)
    {
        this.configOption = configOption;
    }

    public String getConfigOption()
    {
        return configOption;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("rmId", getRmId())
                .append("rmCode", getRmCode())
                .append("rmName", getRmName())
                .append("rmDesc", getRmDesc())
                .append("bsId", getBsId())
                .append("bsName", getBsName())
                .append("rmType", getRmType())
                .append("configOption", getConfigOption())
                .append("status", getStatus())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
