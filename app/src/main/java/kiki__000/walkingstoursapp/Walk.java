package kiki__000.walkingstoursapp;

/**
 * Created by kiki__000 on 24-Apr-15.
 *
 * Representation of a walk
 */
public class Walk {

    private String id;
    private String name;
    private String date;
    private String time;
    private String venue;
    private String kind;
    private String guide;
    private String description;
    private int stations;
    private int status;

    public Walk(String id, String name, String date, String time, String venue, String kind, String guide, String description, int stations, int status){

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.kind = kind;
        this.guide = guide;
        this.description = description;
        this.stations = stations;
        this.status = status;

    }

    public Walk(){}

    public String getId(){return id;}

    public String getName(){return name;}

    public String getDate(){return date;}

    public String getTime(){return time;}

    public String getVenue(){return venue;}

    public String getKind(){return kind;}

    public String getGuide(){return guide;}

    public String getDescription(){return description;}

    public int getStations(){return stations;}

    public int getStatus(){return status;}

    public void setId(String id){this.id = id;}

    public void setName(String name){this.name = name;}

    public void setDate(String date){this.date = date;}

    public void setTime(String time){this.time = time;}

    public void setVenue(String venue){this.venue = venue;}

    public void setKind(String kind){this.kind = kind;}

    public void setGuide(String guide){this.guide = guide;}

    public void setDescription(String description){this.description = description;}

    public void setStations(int stations){this.stations = stations;}

    public void setStatus(int status){this.status = status;}

}
