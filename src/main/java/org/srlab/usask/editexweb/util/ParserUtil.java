package org.srlab.usask.editexweb.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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

    public static void main(String[] args) throws IOException {
        ParserUtil parserUtil = new ParserUtil();
        ICsvListReader editReader = new CsvListReader(new FileReader("./testdata/testdataset.csv"), CsvPreference.STANDARD_PREFERENCE);
        editReader.getHeader(true);
        final CellProcessor[] processors = getProcessors();
        List<Object> editList = editReader.read(processors);
        System.out.println(parserUtil.parseMarkupToHtml(editList.get(5).toString(),"p"));
    }
    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processor = new CellProcessor[] {
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional()

        };
        return processor;
    }
}
