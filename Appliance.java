import java.util.Random;

public class Appliance {
    private String type;
    private int onWattage;
    private double probability;
    private long location;
    private String applianceId;
    private boolean isOn;

    // Constructor
    public Appliance(String type, int onWattage, double probability, long location, String applianceId) {
        this.type = type;
        this.onWattage = onWattage;
        this.probability = probability;
        this.location = location;
        this.applianceId = applianceId;
        this.isOn = false; // default state is off
    }

    // Getter methods
    public String getType() {
        return type;
    }

    public int getOnWattage() {
        return onWattage;
    }

    public double getProbability() {
        return probability;
    }

    public long getLocation() {
        return location;
    }

    public String getApplianceId() {
        return applianceId;
    }

    public boolean isOn() {
        return isOn;
    }

    // Setter method to turn the appliance on or off based on probability
    public void updateStatus() {
        Random random = new Random();
        double randomValue = random.nextDouble();

        // check probability to turn on/off appliance
        isOn = randomValue < probability;
    }

    // Method to get current wattage based on appliance state
    public int getCurrentWattage() {
        return isOn ? onWattage : 0;
    }

    // Method to get current state of appliance (ON/OFF)
    public String getCurrentState() {
        return isOn ? "ON" : "OFF";
    }
}
