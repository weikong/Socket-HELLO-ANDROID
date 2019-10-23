package com.king.chat.socket.config;

/**
 * Created by maesinfo on 2019/6/19.
 */

public class UrlConfig {


    /**
     * MAC IP
     */
//    public static String HTTP_ROOT = "http://172.17.7.134:9090/";
    /**
     * 公司 IP
     */
//    public static String HTTP_ROOT = "http://172.17.7.164:9090/";
    /**
     * 华为云
     */
//    public static String HTTP_ROOT = "https://114.116.124.23:9090/";
    public static String HTTP_ROOT = "https://deepkeep.top/";

    public static final String DOWNLOAD_SERVER_URL = "http://pic.melinked.com/";//文件下载地址
    public static final String BASE_URL_V3_5050 = "https://www.melinked.com/";
    public static final String HTTP_QINIU_UPTOKEN = BASE_URL_V3_5050 + "qiniu/api/getUploadToken";
    public static final String HTTP_QINIU_DATA_IM_FILE = "https://imfile.melinked.com/"; // 七牛IM文件下载地址

    public static final String BASE_QINIU_DATA = "https://video.melinked.com/"; // 七牛音视频下载地址
    public static final String BASE_QINIU_DATA_IM_FILE = "https://imfile.melinked.com/"; // 七牛IM文件下载地址
    public static final String BASE_QINIU_DATA_TFILE = "https://tfile.melinked.com/"; // 七牛音视频下载地址

    public static final String BASE_QINIIU_SCALE = "?imageView2/2/w/200";
    public static final String BASE_QINIU_Thumbnail = "?vframe/jpg/offset/0"; // 七牛获取视频第一帧图片
    public static final String BASE_QINIU_Thumbnail_2 = "?vframe/jpg/offset/"; // 七牛获取视频某一帧图片


    /**
     * 单文件上传（图片）
     */
    public static String HTTP_UPLOAD_SINGLE_IMAGE = HTTP_ROOT + "upload";

    /**
     * 多文件上传（图片）
     */
    public static String HTTP_UPLOAD_IMAGES = HTTP_ROOT + "batch/upload";

    /**
     * 登录
     */
    public static String HTTP_LOGIN = HTTP_ROOT + "account/login";

    /**
     * 注册
     */
    public static String HTTP_REGISTER = HTTP_ROOT + "account/registerUser";

    /**
     * 退出登录
     */
    public static String HTTP_LOGOUT = HTTP_ROOT + "account/logout";


    /**
     * 查询跑友圈（好友或已关注的人）
     */
    public static String HTTP_CIRCLE_QUERY = HTTP_ROOT + "circle/query";

    /**
     * 点过赞的帖子
     */
    public static String HTTP_CIRCLE_MINE_LIKE_QUERY = HTTP_ROOT + "circle/like/query";
    /**
     * 点赞
     */
    public static String HTTP_CIRCLE_LIKE_INSERT = HTTP_ROOT + "circle/like/insert";
    /**
     * 取消点赞
     */
    public static String HTTP_CIRCLE_LIKE_CLEAR = HTTP_ROOT + "circle/like/clear";

    /**
     * 收藏的帖子
     */
    public static String HTTP_CIRCLE_MINE_COLLECTION_QUERY = HTTP_ROOT + "circle/collection/query";
    /**
     * 收藏
     */
    public static String HTTP_CIRCLE_COLLECTION_INSERT = HTTP_ROOT + "circle/collection/insert";
    /**
     * 取消收藏
     */
    public static String HTTP_CIRCLE_COLLECTION_CLEAR = HTTP_ROOT + "circle/collection/clear";

    /**
     * 关注的人
     */
    public static String HTTP_CIRCLE_MINE_FOCUS_QUERY = HTTP_ROOT + "circle/focus/query";
    /**
     * 关注
     */
    public static String HTTP_CIRCLE_FOCUS_INSERT = HTTP_ROOT + "circle/focus/insert";
    /**
     * 取消关注
     */
    public static String HTTP_CIRCLE_FOCUS_CLEAR = HTTP_ROOT + "circle/focus/clear";

    /**
     * 查询跑友圈（搜索其他跑者）
     */
    public static String HTTP_CIRCLE_SEARCH_QUERY = HTTP_ROOT + "circle/querySearch";

    /**
     * 图片下载
     */
    public static String HTTP_DOWNLOAD_FILE_2 = HTTP_ROOT + "download_file?filename=%s";

    /**
     * 插入跑友圈
     */
    public static String HTTP_CIRCLE_INSERT = HTTP_ROOT + "circle/insert";

    /**
     * 系统注册人员
     */
    public static String HTTP_ACCOUNT_LIST = HTTP_ROOT + "account/list";

    /**
     * 创建群组
     */
    public static String HTTP_GROUP_CREATE = HTTP_ROOT + "group/groupCreate";

    /**
     * 查询群组
     */
    public static String HTTP_GROUP_QUERY_BY_ID = HTTP_ROOT + "group/groupQueryById";

    /**
     * 查询群组
     */
    public static String HTTP_GROUP_QUERY_BY_GROUP_ACCOUNT = HTTP_ROOT + "group/groupQueryByGroupAccount";

    /**
     * 更新群组
     */
    public static String HTTP_GROUP_UPDATE = HTTP_ROOT + "group/groupUpdate";

    /**
     * 查询我的群聊
     */
    public static String HTTP_GROUP_QUERY_MY_GROUPS = HTTP_ROOT + "group/queryMyGroups";
}
