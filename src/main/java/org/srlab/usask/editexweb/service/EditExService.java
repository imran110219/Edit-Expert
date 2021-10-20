package org.srlab.usask.editexweb.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pmml4s.model.Model;
import org.springframework.stereotype.Service;
import org.srlab.usask.editex.predictors.*;
import org.srlab.usask.editexweb.dto.EditTextDTO;
import org.srlab.usask.editexweb.dto.ResultDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sadman
 */
@Service
public class EditExService {

    public ResultDTO detect(EditTextDTO editTextDTO) {

        Model model = Model.fromFile("./model/model.pmml");

        Document preEditDoc = null;
        Document postEditDoc = null;
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

        try {
            preEditDoc = Jsoup.parse(editTextDTO.getPreText());
            preText = preEditDoc.select("p");
            preEditText = preText.text().toString();

            preCode = preEditDoc.select("pre");
            preEditCode = preCode.text();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            postEditDoc = Jsoup.parse(editTextDTO.getPostText());
            postText = postEditDoc.select("p");
            postEditText = postText.text().toString();

            postCode = postEditDoc.select("pre");
            postEditCode = postCode.text();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Attribute Detector

        if (preEditText != null && !preEditText.isEmpty() && postEditText != null && !postEditText.isEmpty()) {

            editDistance = LevenshteinDistance.calculate(preEditText.toLowerCase(), postEditText.toLowerCase());

            if (editDistance == 0) {
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

            editDistance = LevenshteinDistance.calculate(preEditCode.toLowerCase(), postEditCode.toLowerCase());

            if (editDistance == 0) {
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
        signature = SignatureDetection.detectSignature(preEditText, postEditText, editTextDTO.getRollbackUserName(), editTextDTO.getRejectedEditUserName());

        inactiveLink = EditHyperLink.detectInactiveHyperlink(preEditText, postEditText);
        referenceModification = EditHyperLink.detectHyperLinkModification(preEditText, postEditText);

        if ((preEditText == null && preEditCode == null) || (postEditText == null && postEditCode == null)) {
            defacePost = 1;
        }

        if ((preEditText != null && !preEditText.isEmpty()) || (postEditText != null && !postEditText.isEmpty())) {

            editDistance = 0; //reset edit distance value
            editDistance = LevenshteinDistance.calculate(preEditText, postEditText);

            if (editDistance == preEditText.length() || editDistance == postEditText.length()) {
                completeChange = 1;
            }
        }

        if ((preEditCode != null && !preEditCode.isEmpty()) || (postEditCode != null && !postEditCode.isEmpty())) {

            editDistance = 0; //reset edit distance value
            editDistance = LevenshteinDistance.calculate(preEditCode, postEditCode);

            if (editDistance == preEditCode.length() || editDistance == postEditCode.length()) {
                completeChange = 1;
            }
        }

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
        feature.put("reputation", Integer.parseInt(editTextDTO.getReputation()));
        feature.put("emotion", 0);

        Map<String, Object> results = model.predict(feature);

        rejected = (double) results.get("probability(1)");
        accepted = (double) results.get("probability(0)");

        ResultDTO featureDTO = new ResultDTO();
        featureDTO.setAcceptProbability(accepted);
        featureDTO.setRejectProbability(rejected);

        if (rejected >= accepted) {
            System.out.println("Rejected with probability:" + rejected);
            featureDTO.setPermission("rejected");
        } else {
            System.out.println("Accepted with probability:" + accepted);
            featureDTO.setPermission("accepted");
        }
        return featureDTO;
    }
}
