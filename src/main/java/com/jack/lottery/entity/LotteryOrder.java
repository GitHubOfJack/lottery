package com.jack.lottery.entity;

import java.math.BigDecimal;
import java.util.Date;

public class LotteryOrder {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.userId
     *
     * @mbggenerated
     */
    private Long userid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.amount
     *
     * @mbggenerated
     */
    private BigDecimal amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.order_id
     *
     * @mbggenerated
     */
    private String orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.order_id_ext
     *
     * @mbggenerated
     */
    private String orderIdExt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.num
     *
     * @mbggenerated
     */
    private Integer num;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.lottery_type
     *
     * @mbggenerated
     */
    private String lotteryType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.lottery_term
     *
     * @mbggenerated
     */
    private String lotteryTerm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.lottery_content
     *
     * @mbggenerated
     */
    private String lotteryContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.status
     *
     * @mbggenerated
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.msg
     *
     * @mbggenerated
     */
    private String msg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lottery_order.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.id
     *
     * @return the value of lottery_order.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.id
     *
     * @param id the value for lottery_order.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.userId
     *
     * @return the value of lottery_order.userId
     *
     * @mbggenerated
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.userId
     *
     * @param userid the value for lottery_order.userId
     *
     * @mbggenerated
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.amount
     *
     * @return the value of lottery_order.amount
     *
     * @mbggenerated
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.amount
     *
     * @param amount the value for lottery_order.amount
     *
     * @mbggenerated
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.order_id
     *
     * @return the value of lottery_order.order_id
     *
     * @mbggenerated
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.order_id
     *
     * @param orderId the value for lottery_order.order_id
     *
     * @mbggenerated
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.order_id_ext
     *
     * @return the value of lottery_order.order_id_ext
     *
     * @mbggenerated
     */
    public String getOrderIdExt() {
        return orderIdExt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.order_id_ext
     *
     * @param orderIdExt the value for lottery_order.order_id_ext
     *
     * @mbggenerated
     */
    public void setOrderIdExt(String orderIdExt) {
        this.orderIdExt = orderIdExt == null ? null : orderIdExt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.num
     *
     * @return the value of lottery_order.num
     *
     * @mbggenerated
     */
    public Integer getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.num
     *
     * @param num the value for lottery_order.num
     *
     * @mbggenerated
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.lottery_type
     *
     * @return the value of lottery_order.lottery_type
     *
     * @mbggenerated
     */
    public String getLotteryType() {
        return lotteryType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.lottery_type
     *
     * @param lotteryType the value for lottery_order.lottery_type
     *
     * @mbggenerated
     */
    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType == null ? null : lotteryType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.lottery_term
     *
     * @return the value of lottery_order.lottery_term
     *
     * @mbggenerated
     */
    public String getLotteryTerm() {
        return lotteryTerm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.lottery_term
     *
     * @param lotteryTerm the value for lottery_order.lottery_term
     *
     * @mbggenerated
     */
    public void setLotteryTerm(String lotteryTerm) {
        this.lotteryTerm = lotteryTerm == null ? null : lotteryTerm.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.lottery_content
     *
     * @return the value of lottery_order.lottery_content
     *
     * @mbggenerated
     */
    public String getLotteryContent() {
        return lotteryContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.lottery_content
     *
     * @param lotteryContent the value for lottery_order.lottery_content
     *
     * @mbggenerated
     */
    public void setLotteryContent(String lotteryContent) {
        this.lotteryContent = lotteryContent == null ? null : lotteryContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.status
     *
     * @return the value of lottery_order.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.status
     *
     * @param status the value for lottery_order.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.msg
     *
     * @return the value of lottery_order.msg
     *
     * @mbggenerated
     */
    public String getMsg() {
        return msg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.msg
     *
     * @param msg the value for lottery_order.msg
     *
     * @mbggenerated
     */
    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.create_time
     *
     * @return the value of lottery_order.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.create_time
     *
     * @param createTime the value for lottery_order.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lottery_order.update_time
     *
     * @return the value of lottery_order.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lottery_order.update_time
     *
     * @param updateTime the value for lottery_order.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lottery_order
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LotteryOrder other = (LotteryOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getOrderIdExt() == null ? other.getOrderIdExt() == null : this.getOrderIdExt().equals(other.getOrderIdExt()))
            && (this.getNum() == null ? other.getNum() == null : this.getNum().equals(other.getNum()))
            && (this.getLotteryType() == null ? other.getLotteryType() == null : this.getLotteryType().equals(other.getLotteryType()))
            && (this.getLotteryTerm() == null ? other.getLotteryTerm() == null : this.getLotteryTerm().equals(other.getLotteryTerm()))
            && (this.getLotteryContent() == null ? other.getLotteryContent() == null : this.getLotteryContent().equals(other.getLotteryContent()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getMsg() == null ? other.getMsg() == null : this.getMsg().equals(other.getMsg()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lottery_order
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getOrderIdExt() == null) ? 0 : getOrderIdExt().hashCode());
        result = prime * result + ((getNum() == null) ? 0 : getNum().hashCode());
        result = prime * result + ((getLotteryType() == null) ? 0 : getLotteryType().hashCode());
        result = prime * result + ((getLotteryTerm() == null) ? 0 : getLotteryTerm().hashCode());
        result = prime * result + ((getLotteryContent() == null) ? 0 : getLotteryContent().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getMsg() == null) ? 0 : getMsg().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lottery_order
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", amount=").append(amount);
        sb.append(", orderId=").append(orderId);
        sb.append(", orderIdExt=").append(orderIdExt);
        sb.append(", num=").append(num);
        sb.append(", lotteryType=").append(lotteryType);
        sb.append(", lotteryTerm=").append(lotteryTerm);
        sb.append(", lotteryContent=").append(lotteryContent);
        sb.append(", status=").append(status);
        sb.append(", msg=").append(msg);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}