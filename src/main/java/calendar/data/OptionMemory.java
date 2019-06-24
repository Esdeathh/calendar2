package main.java.calendar.data;

public class OptionMemory {
    private static final OptionMemory instance = new OptionMemory();
    private boolean playSound = true;

    private OptionMemory() {}

    public static OptionMemory getInstance() {
        return instance;
    }

    public void setSoundStatus(boolean status) {
        playSound = status;
    }

    public boolean getSoundStatus() {
        return playSound;
    }
}
