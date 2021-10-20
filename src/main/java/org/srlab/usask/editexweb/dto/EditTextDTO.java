package org.srlab.usask.editexweb.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sadman
 */
@Getter
@Setter
public class EditTextDTO {
    private String preText;
    private String postText;
    private String rollbackUserName;
    private String userID;
    private String rejectedEditUserName;
    private String reputation;
}
