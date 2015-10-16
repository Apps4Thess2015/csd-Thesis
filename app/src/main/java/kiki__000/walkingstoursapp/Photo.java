package kiki__000.walkingstoursapp;

/**
 * Created by kiki__000 on 27-May-15.
 *
 * Representation of a photo
 */

public class Photo {

    private String id;
    private String stationId;
    private String walkId;
    private String image;


    public Photo(){}

    public Photo(String id, String stationId,  String walkId, String image){

        this.id = id;
        this.stationId = stationId;
        this.walkId = walkId;
        this.image = image;
    }

    public String getId(){return id;}

    public String getStationId(){return stationId;}

    public String getWalkId(){return walkId;}

    public String getImage(){return image;}

    public void setId(String i){id = i;}

    public void setStationId(String sId){stationId = sId;}

    public void setWalkId(String wId){walkId = wId;}

    public void setImage(String im){image = im;}

}
