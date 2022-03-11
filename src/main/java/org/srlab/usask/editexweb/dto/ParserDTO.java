package org.srlab.usask.editexweb.dto;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

/**
 * @author Sadman
 */
@Getter
@Setter
public class ParserDTO {
    private String text;
    private Document document;
}
