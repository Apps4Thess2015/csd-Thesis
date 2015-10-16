package kiki__000.walkingstoursapp;

/**
 * Created by kiki__000 on 28-Aug-15.
 */
public class Rating {

    private String id;
    private String stationId;
    private String walkId;
    private int points;
    private String rated; //rated = 1 if the user has rated this station else 0
    private String sent; //sent = 1 if the points of this station have been sent to server else 0

    public Rating(){}

    public Rating(String id, String stationId, String walkId, int points, String rated, String sent){

        this.id = id;
        this.stationId = stationId;
        this.walkId = walkId;
        this.points = points;
        this.rated = rated;
        this.sent = sent;
    }

    public String getId(){return id;}

    public String getStationId(){return stationId;}

    public String getWalkId(){return walkId;}

    public int getPoints(){return points;}

    public String getRated(){return rated;}

    public String getSent(){return sent;}

    public void setId(String i){id = i;}

    public void setStationId(String sI){stationId = sI;}

    public void setWalkId(String wI){walkId = wI;}

    public void setPoints(int p){points = p;}

    public void setRated(String r){rated = r;}

    public void setSent(String s){sent = s;}

}