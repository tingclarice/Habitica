package Model;
public class Achievement {
    private String achievement;
    private String description;

    public Achievement(String achievement, String description) {
        this.achievement = achievement;
        this.description = description;
    }
    public String getAchievement() {
        return achievement;
    }
    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


}
