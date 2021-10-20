package org.srlab.usask.editexweb.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sadman
 */
@Getter
@Setter
public class ResultDTO {
    private double acceptProbability;
    private double rejectProbability;
    private String permission;
    private String reason;
}
