package cat.tecnocampus.favoritejourney.webAdapter.webModel;

import java.util.List;

public class FavoriteJourneyDTO {

    private String origin;

    private String destination;

    private String id;

    private List<DayTimeStartDTO> dayTimes;

    public FavoriteJourneyDTO() {
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DayTimeStartDTO> getDayTimes() {
        return dayTimes;
    }

    public void setDayTimes(List<DayTimeStartDTO> dayTimes) {
        this.dayTimes = dayTimes;
    }
}

