package movieapp.singhania.com.movieapp.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import movieapp.singhania.com.movieapp.model.MovieModel;
import movieapp.singhania.com.movieapp.model.TrailerMovieModel;

/**
 * Created by mrsinghania on 28/2/17.
 */

public class ParseJSONResponse {


    public static String parseJSONResponse(String response){
        int page;
        JSONArray resultObject;
        try {
            JSONObject responseObject=new JSONObject(response);
            if(responseObject.has(Constants.PAGE)){
               page =responseObject.getInt(Constants.PAGE);
            }else{
                page=0;
            }
            if(responseObject.has(Constants.RESULTS)){
                resultObject=responseObject.getJSONArray(Constants.RESULTS);
                return resultObject.toString();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
    public static List<MovieModel> parseResultObjectForMovieList(String parseResponse){
        int page;
        JSONArray resultObject;
        List<MovieModel> movieModelList;
        try {
            JSONArray resultArray=new JSONArray(parseResponse);
            movieModelList=new ArrayList<>();
            for(int i=0;i<resultArray.length();i++){
                JSONObject jsonObject=resultArray.getJSONObject(i);
                MovieModel movieModel=parseMovieModel(jsonObject);
                movieModelList.add(movieModel);
            }
            return movieModelList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<TrailerMovieModel> parseResultObjectForTrailerList(String parseResponse){
        int page;
        List<TrailerMovieModel> trailerMovieModelList;
        try {
            JSONArray resultArray=new JSONArray(parseResponse);
            trailerMovieModelList=new ArrayList<>();
            for(int i=0;i<resultArray.length();i++){
                JSONObject jsonObject=resultArray.getJSONObject(i);
                TrailerMovieModel movieModel=parseMovieTrailerModel(jsonObject);
                trailerMovieModelList.add(movieModel);
            }
            return trailerMovieModelList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MovieModel parseMovieModel(JSONObject jsonObject){
        MovieModel movieModel=new MovieModel();
            try {
                if(jsonObject.has(Constants.ID)) {
                    movieModel.setId(jsonObject.getLong(Constants.ID));
                }
                if(jsonObject.has(Constants.ADULT)){
                    movieModel.setAdult(jsonObject.getBoolean(Constants.ADULT));
                }
                if(jsonObject.has(Constants.TITLE)){
                    movieModel.setTitle(jsonObject.getString(Constants.TITLE));
                }
                if(jsonObject.has(Constants.OVERVIEW)){
                    movieModel.setOverView(jsonObject.getString(Constants.OVERVIEW));
                }
                if(jsonObject.has(Constants.POSTER_PATH)){
                    movieModel.setPosterPath(jsonObject.getString(Constants.POSTER_PATH));
                }
                if(jsonObject.has(Constants.POPULARITY)){
                    movieModel.setPopularity(jsonObject.getDouble(Constants.POPULARITY));
                }
                if(jsonObject.has(Constants.VOTE_COUNT)){
                    movieModel.setVote_count(jsonObject.getInt(Constants.VOTE_COUNT));
                }
                if(jsonObject.has(Constants.VOTE_AVERAGE)){
                    movieModel.setVote_average(jsonObject.getDouble(Constants.VOTE_AVERAGE));
                }
                if(jsonObject.has(Constants.OVERVIEW)){
                    movieModel.setReleaseDate(jsonObject.getString(Constants.RELEASE_DATE));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        return movieModel;
    }

    public static TrailerMovieModel parseMovieTrailerModel(JSONObject jsonObject){
        TrailerMovieModel trailerMovieModel=new TrailerMovieModel();
        try {
            if(jsonObject.has(Constants.ID)) {
                trailerMovieModel.setId(jsonObject.getLong(Constants.ID));
            }
            if(jsonObject.has(Constants.SITE)){
                trailerMovieModel.setSite(jsonObject.getString(Constants.SITE));
            }
            if(jsonObject.has(Constants.KEY)){
                trailerMovieModel.setKey(jsonObject.getString(Constants.KEY));
            }
            if(jsonObject.has(Constants.NAME)){
                trailerMovieModel.setName(jsonObject.getString(Constants.NAME));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerMovieModel;
    }
}
