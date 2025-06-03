package Model;
public class Achievement {
    private String title;
    private boolean unlocked;
    private String description;

    public Achievement(String title, String description) {
        this.title = title;
        this.unlocked = false; // Default to not unlocked
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isUnlocked() {
        return unlocked;
    }
    public void unlock() {
        this.unlocked = true; // Set to true when the achievement is unlocked
    }
    public String toString(){
        return (unlocked ? "[UNLOCKED]" : "[LOCKED]") + " " + title + ": " + description;
    }


}