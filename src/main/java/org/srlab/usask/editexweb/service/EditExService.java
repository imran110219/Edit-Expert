package org.srlab.usask.editexweb.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pmml4s.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srlab.usask.editex.predictors.*;
import org.srlab.usask.editexweb.dto.EditTextDTO;
import org.srlab.usask.editexweb.dto.ResultDTO;
import org.srlab.usask.editexweb.util.ParserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sadman
 */
@Service
public class EditExService {

    @Autowired
    ParserUtil parserUtil;

    public ResultDTO detect(EditTextDTO editTextDTO) {

        Model model = Model.fromFile("./model/model.pmml");
        String[] rollbackUN = editTextDTO.getRollbackUserName().trim().split(" ");
        String rollbackUserName = rollbackUN[0];

        Elements preText = null;
        Elements preCode = null;
        Elements postText = null;
        Elements postCode = null;

        String preEditText = "";
        String postEditText = "";
        String preEditCode = "";
        String postEditCode = "";

        int editDistance = 0;
        int textFormatting = 0;
        float textChange = 0;
        int codeFormatting = 0;
        float codeChange = 0;
        int gratitude = 0;
        int greeting = 0;

        int status = 0;
        int deprecation = 0;
        int duplication = 0;
        int signature = 0;

        int inactiveLink = 0;
        int referenceModification = 0;

        int defacePost = 0;
        int completeChange = 0;

        int defaceCode = 0;
        int defaceText = 0;
        int completeChangeCode = 0;
        int completeChangeText = 0;

        int reputation = 0;

        String suggestion = "";
        int numberOfReason = 0;

        Parser parser = Parser.builder().build();
        //Parse markup to HTML
        Node preTextDocument = parser.parse(editTextDTO.getPreText());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        renderer.render(preTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
        Document preEditDoc = Jsoup.parse(renderer.render(preTextDocument));
        preText = preEditDoc.select("p");
        preEditText = preText.text().toString();

        preCode = preEditDoc.select("pre");
        preEditCode = preCode.text();


        Node postTextDocument = parser.parse(editTextDTO.getPostText());
        HtmlRenderer rendererPost = HtmlRenderer.builder().build();
        rendererPost.render(postTextDocument);  // "<p>This is <em>Sparta</em></p>\n"
        Document postEditDoc = Jsoup.parse(renderer.render(postTextDocument));
//					System.out.println(preEditDoc);

        postText = postEditDoc.select("p");
        postEditText = postText.text().toString();

        postCode = postEditDoc.select("pre");
        postEditCode = postCode.text();

        if (preEditText != null && !preEditText.isEmpty() && postEditText != null && !postEditText.isEmpty()) {

            editDistance = LevenshteinDistance.calculate(preEditText.replaceAll(" ", "").replaceAll("[\n\r]", "").toLowerCase(), postEditText.replaceAll(" ", "").replaceAll("[\n\r]", "").toLowerCase());
            int editDistanceWithTags = LevenshteinDistance.calculate(preText.toString().toLowerCase(), postText.toString().toLowerCase());

            if (editDistance == 0 && editDistanceWithTags != 0) {
                textFormatting = 1;
            }
        }
        editDistance = 0; //reset edit distance value

        if (preText != null || postText != null) {
            editDistance = LevenshteinDistance.calculate(preText.toString(), postText.toString());
            if (preText.toString().length() != 0) {
                textChange = (editDistance / (float) preText.toString().length()) * 100;
            }
        }

        if (preEditCode != null && !preEditCode.isEmpty() && postEditCode != null && !postEditCode.isEmpty()) {

            editDistance = LevenshteinDistance.calculate(preEditCode.replaceAll(" ", "").replaceAll("[\n\r]", "").toLowerCase(), postEditCode.replaceAll(" ", "").replaceAll("[\n\r]", "").toLowerCase());

            int spaceDifference = (preEditCode.length() - preEditCode.replaceAll(" ", "").length()) - (postEditCode.length() - postEditCode.replaceAll(" ", "").length());
            if (spaceDifference < 0) spaceDifference = (-1) * spaceDifference;
            //int charDifference = preEditCode.replaceAll(" ", "").replaceAll("[\n\r]", "").length() - postEditCode.replaceAll(" ", "").replaceAll("[\n\r]", "").length();

            int locPreEditCode = 0;
            String[] codeLinesPreEditCode = preEditCode.split("\r\n|\r|\n");

            for (int i = 0; i < codeLinesPreEditCode.length; i++) {
                if (!codeLinesPreEditCode[i].isEmpty()) locPreEditCode++;
            }

            int locPostEditCode = 0;
            String[] codeLinesPostEditCode = postEditCode.split("\r\n|\r|\n");

            for (int i = 0; i < codeLinesPostEditCode.length; i++) {
                if (!codeLinesPostEditCode[i].isEmpty()) locPostEditCode++;
            }

            if ((editDistance == 0 && spaceDifference >= 5) || (editDistance == 0 && (locPreEditCode != locPostEditCode))) {
                codeFormatting = 1;
            }
        }

        editDistance = 0; //reset edit distance value
        if (preCode != null || postCode != null) {
            editDistance = LevenshteinDistance.calculate(preCode.toString(), postCode.toString());
            if (preCode.toString().length() != 0) {
                codeChange = (editDistance / (float) preCode.toString().length()) * 100;
            }
        }

        gratitude = GratitudeDetection.detectGratitute(preEditText.toLowerCase(), postEditText.toLowerCase());
        greeting = GreetingsDetection.detectGreetings(preEditText.toLowerCase(), postEditText.toLowerCase());

        status = StatusDetection.detectStatus(preEditText.toLowerCase(), postEditText.toLowerCase());
        deprecation = DeprecationDetecton.detectDeprecation(preEditText.toLowerCase(), postEditText.toLowerCase());
        duplication = DuplicationDetection.detectDeprecation(preEditText.toLowerCase(), postEditText.toLowerCase());
        signature = SignatureDetection.detectSignature(postEditText.toLowerCase(), rollbackUserName.toLowerCase());

        inactiveLink = EditHyperLink.detectInactiveHyperlink(preEditDoc, postEditDoc);
        referenceModification = EditHyperLink.detectHyperLinkModification(preEditText, postEditText);

        if ((preEditText.length() != 0 || !preEditText.isEmpty()) && (postEditText.length() == 0 || postEditText.isEmpty())) {
            defacePost = 1;
            defaceText = 1;
        }
        if ((!preEditCode.isEmpty() || preEditCode.length() != 0) && (postEditCode.length() == 0 || postEditCode.isEmpty())) {
            defacePost = 1;
            defaceCode = 1;
        }

        if ((!preEditText.isEmpty()) && (!postEditText.isEmpty())) {

            editDistance = 0; //reset edit distance value
            editDistance = LevenshteinDistance.calculate(preEditText, postEditText);

            if (editDistance == preEditText.length() || editDistance == postEditText.length()) {
                completeChange = 1;
                completeChangeText = 1;
            }
        }

        if ((!preEditCode.isEmpty()) && (!postEditCode.isEmpty())) {

            editDistance = 0; //reset edit distance value
            editDistance = LevenshteinDistance.calculate(preEditCode, postEditCode);

            if (editDistance == preEditCode.length() || editDistance == postEditCode.length()) {
                completeChange = 1;
                completeChangeCode = 1;
            }
        }

        reputation = Integer.parseInt(editTextDTO.getReputation());

        //csvWriter.write(postId,textFormatting,textChange,codeFormatting,codeChange,gratitude,greeting,status,deprecation,duplication,signature,inactiveLink,referenceModification,defacePost,completeChange,reputation);

        Map<String, Object> feature = new HashMap<String, Object>();

        double rejected, accepted;

        feature.put("text-format", textFormatting);
        feature.put("text-change", textChange);
        feature.put("code-format", codeFormatting);
        feature.put("code-change", codeChange);
        feature.put("gratitude", gratitude);
        feature.put("greetings", greeting);
        feature.put("status", status);
        feature.put("deprecation", deprecation);
        feature.put("duplication", duplication);
        feature.put("signature", signature);
        feature.put("inactive-link", inactiveLink);
        feature.put("ref-modification", referenceModification);
        feature.put("deface-post", defacePost);
        feature.put("complete-change", completeChange);
        feature.put("reputation", reputation);
//				feature.put("emotion", 0);

        Map<String, Object> results = model.predict(feature);

//				for (String key: results.keySet()) {
//					System.out.println(key+" "+results.get(key));
//				}

        rejected = (double) results.get("probability(1)");
        accepted = (double) results.get("probability(0)");

        int reasonNo = 1;

        ResultDTO resultDTO = new ResultDTO();

        if ((rejected - accepted) >= 0.25) {
            //System.out.println(rejected+"  "+ accepted);
            suggestion = "Sorry!\n" +
                    "Your editing is more likely to be rejected due to the following potential reason(s):\n\n";

            if (textFormatting == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you only modified the format of the text. Would you please avoid unnecessary text formatting?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (codeFormatting == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you only modified the format of the code. Would you please avoid unnecessary code formatting?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (gratitude == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like there is a gratitude (e.g., thanks, sorry). Addition of gratitude is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (greeting == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like there is a greeting (e.g., hi, hello, dear). Addition of greeting is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (status == 1) {
                suggestion += "Reason " + reasonNo + ": You added/removed personal notes. Please check. Your edit could be rejected due to the addition of unnecessary personal notes or removal of important notes.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (deprecation == 1) {
                suggestion += "Reason " + reasonNo + ": You added/removed a deprecation note inside the body of the post. Addition of a deprecation note is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (duplication == 1) {
                suggestion += "Reason " + reasonNo + ": You added/removed a duplication note inside the body of the post. Addition of a duplication note is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (signature == 1) {
                suggestion += "Reason " + reasonNo + ": You added a signature (e.g., your name) to the post. Addition of a signature is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (inactiveLink == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like there is an inactive hyperlink. Would you please check the link and avoid adding inactive links?\n";
                numberOfReason++;
                reasonNo++;
            }

            if (defaceText == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you defaced the previous textual description of the post. Would you please avoid the unnecessary complete removal of problem description?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (defaceCode == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you defaced the previous code segment of the post. Would you please avoid the unnecessary complete removal of a code segment?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (completeChangeText == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you changed textual description completely!\n";
                numberOfReason++;
                reasonNo++;
            }
            if (completeChangeCode == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you changed a code segment completely!\n";
                numberOfReason++;
                reasonNo++;
            }
            if (reputation < 10 && numberOfReason == 0) {
                suggestion += "Reason " + reasonNo + ": The system is predicted that your edit could be rejected due to your low reputation. However, probably your editing will not be rejected if you contribute to improving the post quality.\n";
                numberOfReason++;
                reasonNo++;
            }

            if (codeChange > 50.0 && preEditCode.length() > 0 && postEditCode.length() > 0) {
                suggestion += "Reason " + reasonNo + ": System detected that there might be undesired changes of code. Would you please avoid undesired code modifications? If you conduct the required changes of code, go ahead with your edits.\n";
                numberOfReason++;
                reasonNo++;
            }

            if ((textChange > 40.0 && textChange < 50.0) && preEditText.length() > 0 && postEditText.length() > 0) {
                suggestion += "Reason " + reasonNo + ": System detected that there might be undesired changes of text. Would you please avoid undesired text modifications? If you conduct the required changes of text, go ahead with your edits.\n";
                numberOfReason++;
                reasonNo++;
            }

            suggestion += "\nPlease consider the suggestion(s) to avoid rejection. Good luck!\n";

            if (numberOfReason == 0) {
//						suggestion += "Reason "+reasonNo+": System detected that there might be undesired changes of text or code. Would you please avoid undesired code or text modifications? If you conduct the required changes of text/code, go ahead with your edits.\n";
//						reasonNo++;

                suggestion = "Good job!\n" +
                        "Your editing is more likely to be accepted.\n" +
                        "Thanks for your contribution to improving the quality of the posts.\n";
            }

            resultDTO.setPermission("Rejected");
        } else if (gratitude == 1 || greeting == 1 || signature == 1 || deprecation == 1 || duplication == 1 || inactiveLink == 1 || defacePost == 1 || textFormatting == 1 || codeFormatting == 1) {

            suggestion = "Sorry!\n" +
                    "Your editing is more likely to be rejected due to the following potential reason(s):\n\n";

            if (gratitude == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like there is a gratitude (e.g., thanks, sorry). Addition of gratitude is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (greeting == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like there is a greeting (e.g., hi, hello, dear). Addition of greeting is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (deprecation == 1) {
                suggestion += "Reason " + reasonNo + ": You added/removed a deprecation note inside the body of the post. Addition of a deprecation note is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (duplication == 1) {
                suggestion += "Reason " + reasonNo + ": You added/removed a duplication note inside the body of the post. Addition of a duplication note is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (signature == 1) {
                suggestion += "Reason " + reasonNo + ": You added a signature (e.g., your name) to the post. Addition of a signature is often rejected later.\n";
                numberOfReason++;
                reasonNo++;
            }
            if (inactiveLink == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like there is an inactive hyperlink. Would you please check the link and avoid adding inactive links?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (defaceText == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you defaced the previous textual description of the post. Would you please avoid the unnecessary complete removal of problem description?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (defaceCode == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you defaced the previous code segment of the post. Would you please avoid the unnecessary complete removal of a code segment?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (textFormatting == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you only modified the format of the text. Would you please avoid unnecessary text formatting?\n";
                numberOfReason++;
                reasonNo++;
            }
            if (codeFormatting == 1) {
                suggestion += "Reason " + reasonNo + ": It looks like you only modified the format of the code. Would you please avoid unnecessary code formatting?\n";
                numberOfReason++;
                reasonNo++;
            }

            suggestion += "\nPlease consider the suggestion(s) to avoid rejection. Good luck!\n";
            resultDTO.setPermission("Rejected");

        } else {
            suggestion = "Good job!\n" +
                    "Your editing is more likely to be accepted.\n" +
                    "Thanks for your contribution to improving the quality of the posts.\n";
            resultDTO.setPermission("Accepted");
        }

        System.out.println(suggestion);

        resultDTO.setAcceptProbability(accepted);
        resultDTO.setRejectProbability(rejected);
        resultDTO.setReason(suggestion);
        return resultDTO;
    }
}
