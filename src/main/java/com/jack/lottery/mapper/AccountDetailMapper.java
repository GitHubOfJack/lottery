package com.jack.lottery.mapper;

import com.jack.lottery.entity.AccountDetail;
import com.jack.lottery.entity.AccountDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int countByExample(AccountDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int deleteByExample(AccountDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int insert(AccountDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int insertSelective(AccountDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    List<AccountDetail> selectByExample(AccountDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    AccountDetail selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") AccountDetail record, @Param("example") AccountDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") AccountDetail record, @Param("example") AccountDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(AccountDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_detail
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(AccountDetail record);
}