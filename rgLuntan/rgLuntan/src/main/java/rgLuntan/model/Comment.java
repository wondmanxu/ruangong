package rgLuntan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

 
public class Comment implements Serializable {
    private static final long serialVersionUID = 8413239906874427490L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer topicId;
    private Integer userId;
    private String style; // 内容格式化，RICH，MD
    private String content;
    private Date inTime;
    private Integer commentId;
    // 点赞用户的id
    private String upIds;
    private Integer tgMessageId;
    // true: 通过，false: 审核中
    private Boolean status;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUpIds() {
        return upIds;
    }

    public void setUpIds(String upIds) {
        this.upIds = upIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getTgMessageId() {
        return tgMessageId;
    }

    public void setTgMessageId(Integer tgMessageId) {
        this.tgMessageId = tgMessageId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
