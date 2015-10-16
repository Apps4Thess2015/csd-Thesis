package kiki__000.walkingstoursapp;

/**
 * Created by kiki__000 on 16-May-15.
 *
 * Representation of a station of a walk
 */
public class Station {

    private String id;
    private String title;
    private String description;
    private Double lat; //latitude
    private Double lng; //longitude
    private String walkId;
    private String turn;

    public Station(){}

    public Station(String id, String title, String description, Double lat, Double lng, String walkId, String turn){

        this.id = id;
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.walkId = walkId;
        this.turn = turn;
    }

    public String getId(){return id;}

    public String getTitle(){return title;}

    public String getDescription(){return description;}

    public Double getLat(){return lat;}

    public Double getLng(){return lng;}

    public String getWalkId(){return walkId;}

    public String getTurn(){return turn;}

    public void setId(String id){this.id = id;}

    public void setTitle(String title){this.title = title;}

    public void setDescription(String description){this.description = description;}

    public void setLat(Double lat){this.lat= lat;}

    public void setLng(Double lng){this.lng = lng;}

    public void setWalkId(String walkId){this.walkId = walkId;}

    public void setTurn(String turn){this.turn = turn;}
}
