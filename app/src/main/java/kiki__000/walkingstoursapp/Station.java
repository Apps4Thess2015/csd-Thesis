package kiki__000.walkingstoursapp;

/**
 * Created by kiki__000 on 16-May-15.
 *
 * Representation of a station o a walk
 */
public class Station {

    private String title;
    private String description;
    private Double lat; //latitude
    private Double lng; //longitude

    public Station(){}

    public Station(String title, String description, Double lat, Double lng){

        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitle(){return title;}

    public String getDescription(){return description;}

    public Double getLat(){return lat;}

    public Double getLng(){return lng;}

    public void setTitle(String title){this.title = title;}

    public void setDescription(String description){this.description = description;}

    public void setLat(Double lat){this.lat= lat;}

    public void setLng(Double lng){this.lng = lng;}
}
