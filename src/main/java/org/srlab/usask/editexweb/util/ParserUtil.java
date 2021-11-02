package org.srlab.usask.editexweb.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * @author Sadman
 */
@Component
public class ParserUtil {
    public String parseMarkupToHtml(String input, String tag){
        Parser parser = Parser.builder().build();
        Document preEditDoc = Jsoup.parse(input);

        //Parse markup to HTML
        Node preTextDocument = parser.parse(preEditDoc.toString());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        renderer.render(preTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
        preEditDoc = Jsoup.parse(renderer.render(preTextDocument));
        Elements preCode = preEditDoc.select(tag);
        return preCode.text();
    }
}
