package com.persival.go4lunch.data.places.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StructuredFormatting{

    @SerializedName("main_text_matched_substrings")
    private List<MainTextMatchedSubstringsItem> mainTextMatchedSubstrings;

    @SerializedName("secondary_text")
    private String secondaryText;

    @SerializedName("main_text")
    private String mainText;

    public List<MainTextMatchedSubstringsItem> getMainTextMatchedSubstrings(){
        return mainTextMatchedSubstrings;
    }

    public String getSecondaryText(){
        return secondaryText;
    }

    public String getMainText(){
        return mainText;
    }

    @Override
     public String toString(){
        return 
            "StructuredFormatting{" + 
            "main_text_matched_substrings = '" + mainTextMatchedSubstrings + '\'' + 
            ",secondary_text = '" + secondaryText + '\'' + 
            ",main_text = '" + mainText + '\'' + 
            "}";
        }
}