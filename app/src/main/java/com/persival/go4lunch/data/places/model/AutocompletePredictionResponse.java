package com.persival.go4lunch.data.places.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AutocompletePredictionResponse{

    @SerializedName("predictions")
    private List<PredictionsItem> predictions;

    @SerializedName("status")
    private String status;

    public List<PredictionsItem> getPredictions(){
        return predictions;
    }

    public String getStatus(){
        return status;
    }

    @Override
     public String toString(){
        return 
            "AutocompletePredictionResponse{" + 
            "predictions = '" + predictions + '\'' + 
            ",status = '" + status + '\'' + 
            "}";
        }
}