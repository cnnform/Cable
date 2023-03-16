package cn.yhjz.bi.domain;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 看板管理对象 bi_painter_manager
 *
 * @author yhjz
 * @date 2022-01-12
 */
public class BiPainterManager extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long pmId;

    /** 唯一码 */
    @Excel(name = "唯一码")
    private String pmCode;

    /** 看板名称 */
    @Excel(name = "看板名称")
    private String pmName;

    /** 看板描述 */
    @Excel(name = "看板描述")
    private String pmDesc;

    /** 看板配置信息 */
    @Excel(name = "看板配置信息")
    private String pmOptions;

    /** 看板状态 */
    @Excel(name = "看板状态")
    private Long pmStatus;

    /** 数据状态 */
    @Excel(name = "数据状态")
    private Long status;

    /** 创建用户 */
    @Excel(name = "创建用户")
    private String createUser;

    /** 更新用户 */
    @Excel(name = "更新用户")
    private String updateUser;

    public void setPmId(Long pmId)
    {
        this.pmId = pmId;
    }

    public Long getPmId()
    {
        return pmId;
    }
    public void setPmCode(String pmCode)
    {
        this.pmCode = pmCode;
    }

    public String getPmCode()
    {
        return pmCode;
    }
    public void setPmName(String pmName)
    {
        this.pmName = pmName;
    }

    public String getPmName()
    {
        return pmName;
    }
    public void setPmDesc(String pmDesc)
    {
        this.pmDesc = pmDesc;
    }

    public String getPmDesc()
    {
        return pmDesc;
    }
    public void setPmOptions(String pmOptions)
    {
        this.pmOptions = pmOptions;
    }

    public String getPmOptions()
    {
        return pmOptions;
    }
    public void setPmStatus(Long pmStatus)
    {
        this.pmStatus = pmStatus;
    }

    public Long getPmStatus()
    {
        return pmStatus;
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
                .append("pmId", getPmId())
                .append("pmCode", getPmCode())
                .append("pmName", getPmName())
                .append("pmDesc", getPmDesc())
                .append("pmOptions", getPmOptions())
                .append("pmStatus", getPmStatus())
                .append("status", getStatus())
                .append("createUser", getCreateUser())
                .append("createTime", getCreateTime())
                .append("updateUser", getUpdateUser())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
