package org.srlab.usask.editexweb.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.srlab.usask.editexweb.dto.EditTextDTO;
import org.srlab.usask.editexweb.dto.ResultDTO;
import org.srlab.usask.iedit.inconsistencydetector.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sadman
 */
@Service
public class IEditService {
    public ResultDTO detect(EditTextDTO editTextDTO) {
        Parser parser = Parser.builder().build();

//        Document preEditDoc = null;
//        Document postEditDoc = null;
        Elements preText = null;
        Elements postText = null;

        String preEditText = "";
        String postEditText = "";
        String userName = "";
        String userFirstName = "";
        String userLastName = "-9999";


        List<Integer> gratitudeInconsistency = new ArrayList<Integer>(); //Index 0: inconsistency, Index 1: Accept, Index 2: Reject
        List<Integer> presentationInconsistency = new ArrayList<Integer>(); //Index 0: inconsistency, Index 1: Accept, Index 2: Reject
        List<Integer> deprecationInconsistency = new ArrayList<Integer>();
        List<Integer> duplicationInconsistency = new ArrayList<Integer>();
        List<Integer> statusInconsistency = new ArrayList<Integer>();
        List<Integer> signatureInconsistency = new ArrayList<Integer>();

//        Node preTextDocument = null;
//        Node postTextDocument = null;
//
//
//        preEditDoc = Jsoup.parse(editTextDTO.getPreText());
//
//        //Parse markup to HTML
//        preTextDocument = parser.parse(preEditDoc.toString());
//        HtmlRenderer renderer = HtmlRenderer.builder().build();
//        renderer.render(preTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
//        preEditDoc = Jsoup.parse(renderer.render(preTextDocument));
//
//        preText = preEditDoc.select("p");
//        preEditText = preText.text().toString();
//
//        postEditDoc = Jsoup.parse(editTextDTO.getPostText());
//
//        //Parse markup to HTML
//        postTextDocument = parser.parse(postEditDoc.toString());
//        HtmlRenderer rendererPost = HtmlRenderer.builder().build();
//        rendererPost.render(postTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
//        postEditDoc = Jsoup.parse(rendererPost.render(postTextDocument));
////				System.out.println(postEditDoc);
//
//        postText = postEditDoc.select("p");
//        postEditText = postText.text().toString();

        ///sfdssf
        Node preTextDocument = parser.parse(editTextDTO.getPreText());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        renderer.render(preTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
        Document preEditDoc = Jsoup.parse(renderer.render(preTextDocument));
        preText = preEditDoc.select("p");
        preEditText = preText.text().toString();


        Node postTextDocument = parser.parse(editTextDTO.getPostText());
        HtmlRenderer rendererPost = HtmlRenderer.builder().build();
        rendererPost.render(postTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
        Document postEditDoc = Jsoup.parse(renderer.render(postTextDocument));
//					System.out.println(preEditDoc);

        postText = postEditDoc.select("p");
        postEditText = postText.text().toString();
        ///dfds

        userName = editTextDTO.getRollbackUserName();
        String[] names = userName.split(" ");
        userFirstName = names[0];

        if (names.length >= 2) {
            userLastName = names[names.length - 1];
        }

        presentationInconsistency = DetectPresentationInconsistency.detectPresentationInconsistency(preEditDoc, postEditDoc, preEditText, postEditText);
        gratitudeInconsistency = DetectGratitudeInconsistency.detectGratitudeInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());
        statusInconsistency = DetectStatusInconsistency.detectStatusInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());
        signatureInconsistency = DetectSignatureInconsistency.detectSignatureInconsistencyForTool(preEditText, postEditText, userFirstName, userLastName);
        deprecationInconsistency = DetectDeprecationInconsistency.detectDeprecationInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());
        duplicationInconsistency = DetectDuplicationInconsistency.detectDuplicationInconsistency(preEditText.toLowerCase(), postEditText.toLowerCase());

        int inconsistencyCount = 0;
        String suggestion = "";

        ResultDTO resultDTO = new ResultDTO();

        if ((presentationInconsistency.get(0) + gratitudeInconsistency.get(0) + signatureInconsistency.get(0)
                + statusInconsistency.get(0) + deprecationInconsistency.get(0) + duplicationInconsistency.get(0) > 0)) {

            suggestion = "It looks like your suggested edits have the following inconsistency:\n";

            if(presentationInconsistency.get(0) == 1) {
                if(presentationInconsistency.get(1) == 1) {
                    suggestion += "\nPresentation Inconsistency: You formatted text elements as code elements. "
                            + "\nCould you please double-check that those formatted elements are code elements?"
                            + "\nHowever, such formatting has 74.8% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }else {
                    suggestion += "\nPresentation Inconsistency: You formatted code elements as text elements. "
                            + "\nCould you please double-check that those formatted elements are text elements?"
                            + "\nHowever, such formatting has 25.2% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }

            }
            if(gratitudeInconsistency.get(0) == 1) {
                if(gratitudeInconsistency.get(1)== 1) {
                    suggestion += "\nGratitudinal Inconsistency: Your suggested edit has a gratitude/emotion (e.g., thanks)."
                            + "\nHowever, the addition of such signature has 49.7% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }else {
                    suggestion += "\nGratitudinal Inconsistency: Your suggested edit has deleted a gratitude/emotion (e.g., thanks)."
                            + "\nHowever, the deletion of such signature has 50.3% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }
            }
            if(signatureInconsistency.get(0) == 1) {
                if(signatureInconsistency.get(1) == 1) {
                    suggestion += "\nSignature Inconsistency: Your suggested edit has a signature (e.g., user name)."
                            + "\nHowever, the addition of such signature has 57.9% of rejection possibility by rollback.";
                    inconsistencyCount++;
                }else {
                    suggestion += "\nSignature Inconsistency: Your suggested edit has deleted a signature (e.g., user name)."
                            + "\nHowever, the deletion of such signature has 42.1% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }

            }
            if(statusInconsistency.get(0) == 1) {
                if(statusInconsistency.get(1) == 1) {
                    suggestion += "\nStatus Inconsistency: Your suggested edit has personal notes. Make sure the personal notes are necessary."
                            + "\nHowever, the addition of such personal notes has 70.5% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }else {
                    suggestion += "\nStatus Inconsistency: Your suggested edit deletes personal notes. Make sure the personal notes are not necessary."
                            + "\nHowever, the deletion of such personal notes has 29.5% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }
            }
            if(deprecationInconsistency.get(0) == 1) {
                if(deprecationInconsistency.get(1) == 1) {
                    suggestion += "\nDeprecation Inconsistency: Your suggested edit has a deprecation note. Please make sure the code is deprecated."
                            + "\nHowever, the addition of such deprecation note inside the body of a post has 46.7% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }else {
                    suggestion += "\nDeprecation Inconsistency: Your suggested edit has deleted a deprecation note. Please make sure the code is not deprecated."
                            + "\nHowever, the deletion of such deprecation note inside the body of a post has 53.3% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }
            }
            if(duplicationInconsistency.get(0) == 1) {
                if(duplicationInconsistency.get(1) == 1) {
                    suggestion += "\nDuplication Inconsistency: Your suggested edit has a duplication note. Please make sure that this is a duplicate of another post."
                            + "\nHowever, the addition of such duplication note inside the body of a post has 65.1% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }else {
                    suggestion += "\nDuplication Inconsistency: Your suggested edit has deleted a duplication note. Please make sure that this is not a duplicate of another post."
                            + "\nHowever, the deletion of such duplication note inside the body of a post has 34.9% of rejection possibility by rollback.\n";
                    inconsistencyCount++;
                }
            }

            suggestion += "\n\nWe hope the suggestion(s) help you to avoid inconsistent edits and rejections. Good Luck!";
            resultDTO.setPermission("Rejected");
            System.out.println(suggestion);

        } else {
            suggestion = "Go ahead! There is no inconsistency in your suggested edits.";
            resultDTO.setPermission("Accepted");
            System.out.println(suggestion);
        }
        resultDTO.setReason(suggestion);
        return resultDTO;
    }
}
