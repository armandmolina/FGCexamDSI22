package cat.tecnocampus.favoritejourney.webAdapter.webModel;

public class DayTimeStartDTO {

    private String dayOfWeek;

    private String time;

    public DayTimeStartDTO() {
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTime() {
        return time;
    }
}
