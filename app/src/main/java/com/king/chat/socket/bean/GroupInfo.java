package com.king.chat.socket.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GroupInfo implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.groupaccount
     *
     * @mbg.generated
     */
    private String groupaccount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.groupname
     *
     * @mbg.generated
     */
    private String groupname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.groupdesc
     *
     * @mbg.generated
     */
    private String groupdesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.groupheader
     *
     * @mbg.generated
     */
    private String groupheader;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.groupownerid
     *
     * @mbg.generated
     */
    private Integer groupownerid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.groupmembers
     *
     * @mbg.generated
     */
    private String groupmembers;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.groupmembers
     *
     * @mbg.generated
     */
    private List<ContactBean> members;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column groupinfo.createtime
     *
     * @mbg.generated
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table groupinfo
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.id
     *
     * @return the value of groupinfo.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.id
     *
     * @param id the value for groupinfo.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.groupaccount
     *
     * @return the value of groupinfo.groupaccount
     *
     * @mbg.generated
     */
    public String getGroupaccount() {
        return groupaccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.groupaccount
     *
     * @param groupaccount the value for groupinfo.groupaccount
     *
     * @mbg.generated
     */
    public void setGroupaccount(String groupaccount) {
        this.groupaccount = groupaccount == null ? null : groupaccount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.groupname
     *
     * @return the value of groupinfo.groupname
     *
     * @mbg.generated
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.groupname
     *
     * @param groupname the value for groupinfo.groupname
     *
     * @mbg.generated
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname == null ? null : groupname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.groupdesc
     *
     * @return the value of groupinfo.groupdesc
     *
     * @mbg.generated
     */
    public String getGroupdesc() {
        return groupdesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.groupdesc
     *
     * @param groupdesc the value for groupinfo.groupdesc
     *
     * @mbg.generated
     */
    public void setGroupdesc(String groupdesc) {
        this.groupdesc = groupdesc == null ? null : groupdesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.groupheader
     *
     * @return the value of groupinfo.groupheader
     *
     * @mbg.generated
     */
    public String getGroupheader() {
        return groupheader;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.groupheader
     *
     * @param groupheader the value for groupinfo.groupheader
     *
     * @mbg.generated
     */
    public void setGroupheader(String groupheader) {
        this.groupheader = groupheader == null ? null : groupheader.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.groupownerid
     *
     * @return the value of groupinfo.groupownerid
     *
     * @mbg.generated
     */
    public Integer getGroupownerid() {
        return groupownerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.groupownerid
     *
     * @param groupownerid the value for groupinfo.groupownerid
     *
     * @mbg.generated
     */
    public void setGroupownerid(Integer groupownerid) {
        this.groupownerid = groupownerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.groupmembers
     *
     * @return the value of groupinfo.groupmembers
     *
     * @mbg.generated
     */
    public String getGroupmembers() {
        return groupmembers;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.groupmembers
     *
     * @param groupmembers the value for groupinfo.groupmembers
     *
     * @mbg.generated
     */
    public void setGroupmembers(String groupmembers) {
        this.groupmembers = groupmembers == null ? null : groupmembers.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column groupinfo.createtime
     *
     * @return the value of groupinfo.createtime
     *
     * @mbg.generated
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column groupinfo.createtime
     *
     * @param createtime the value for groupinfo.createtime
     *
     * @mbg.generated
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}