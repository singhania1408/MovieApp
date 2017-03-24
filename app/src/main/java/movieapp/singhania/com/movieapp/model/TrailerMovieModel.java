package movieapp.singhania.com.movieapp.model;

import java.io.Serializable;

/**
 * Created by mrsinghania on 24/3/17.
 */

public class TrailerMovieModel implements Serializable{
    long id;
    String key;
    String name;
    String site;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
