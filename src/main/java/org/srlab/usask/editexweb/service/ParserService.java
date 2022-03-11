package org.srlab.usask.editexweb.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.srlab.usask.editexweb.dto.EditTextDTO;
import org.srlab.usask.editexweb.dto.ParserDTO;

/**
 * @author Sadman
 */
@Service
public class ParserService {
    public ParserDTO parseText(String text){
        ParserDTO parserDTO = new ParserDTO();
        Parser parser = Parser.builder().build();
        //Parse markup to HTML
        Node textDocument = parser.parse(text);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        renderer.render(textDocument);  // "<p>This is <em>Sparta</em></p>\n"
        Document editDoc = Jsoup.parse(renderer.render(textDocument));
        Elements elementText = editDoc.select("p");
        String editText = elementText.text().toString();

        Elements code = editDoc.select("pre");
        String editCode = code.text();
        parserDTO.setText(editText);
        return parserDTO;
    }
}
