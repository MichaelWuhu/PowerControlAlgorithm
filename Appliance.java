import java.util.Random;

public class Appliance {
    private String type;
    protected int onWattage;
    private double probability;
    private int location;
    private String applianceId;
    private boolean isOn;
    private boolean isSmart;

    // Constructor
    public Appliance(String type, int onWattage, double probability, int location, String applianceId, boolean isSmart) {
        this.type = type;
        this.onWattage = onWattage;
        this.probability = probability;
        this.location = location;
        this.applianceId = applianceId;
        this.isOn = false; // default state is off
        this.isSmart = isSmart;
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

    public boolean getIsOn() {
        return isOn;
    }

    public boolean getIsSmart() {
        return isSmart;
    }


    // method to turn appliance on or off based on probability
    public void updateStatus() {
        Random random = new Random();
        double randomValue = random.nextDouble();

        // check probability to turn on/off appliance
        isOn = randomValue < probability;
    }

    
}
