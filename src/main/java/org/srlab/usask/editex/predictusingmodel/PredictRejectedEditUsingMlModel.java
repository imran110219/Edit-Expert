package org.srlab.usask.editex.predictusingmodel;

import java.util.HashMap;
import java.util.Map;

import org.pmml4s.model.Model;

public class PredictRejectedEditUsingMlModel {

	public static void main(String[] args) {
		
		Model model = Model.fromFile("E:/Eclipse/eclipse-workspace/SOEditEx/model/model.pmml");
		Map<String, Object> feature = new HashMap<String, Object>();
		
		double rejected, accepted;
		
		feature.put("text-format", 0);
		feature.put("text-change", 0);
		feature.put("code-format", 59.934853);
		feature.put("code-change", 0);
		feature.put("gratitude", 0);
		feature.put("greetings", 0);
		feature.put("status", 0);
		feature.put("deprecation", 0);
		feature.put("duplication", 0);
		feature.put("signature", 0);
		feature.put("inactive-link", 0);
		feature.put("ref-modification", 0);
		feature.put("deface-post", 0);
		feature.put("complete-change", 0);
		feature.put("reputation", 10000);
		feature.put("emotion", 1);
		
		Map<String, Object> results = model.predict(feature);
		
//		for (String key: results.keySet()) {
//			System.out.println(key+" "+results.get(key));
//		}
		
		rejected = (double) results.get("probability(1)");
		accepted = (double) results.get("probability(0)");
		
		if(rejected >= accepted) {
			System.out.println("Rejected with probability:"+rejected);
		}else {
			System.out.println("Accepted with probability:"+accepted);
		}
		
		
	}
}
