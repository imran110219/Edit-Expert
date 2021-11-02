package org.srlab.usask.editex.predictors;

import java.util.Arrays;

public class LevenshteinDistance {
	
	public static int calculate(String preEditText, String postEditText) {
	    int[][] dp = new int[preEditText.length() + 1][postEditText.length() + 1];

	    for (int i = 0; i <= preEditText.length(); i++) {
	        for (int j = 0; j <= postEditText.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            }
	            else if (j == 0) {
	                dp[i][j] = i;
	            }
	            else {
	                dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(preEditText.charAt(i - 1), postEditText.charAt(j - 1)), 
	                  dp[i - 1][j] + 1, 
	                  dp[i][j - 1] + 1);
	            }
	        }
	    }

	    return dp[preEditText.length()][postEditText.length()];
	}
	
	public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
	
	public static int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }

}
