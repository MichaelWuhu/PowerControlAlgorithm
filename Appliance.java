import java.util.Random;

public class Appliance {
    private boolean type;
    protected int onWattage;
    private double probability;
    private int location;
    private String applianceId;
    private boolean isOn;
    private boolean isSmart;

    // Constructor
    public Appliance(boolean type, int onWattage, double probability, int location, String applianceId) {
        this.type = type;
        this.onWattage = onWattage;
        this.probability = probability;
        this.location = location;
        this.applianceId = applianceId;
        this.isOn = false; // default state is off
    }

    // Getter methods
    public boolean getType() {
        return type;
    }

    public int getOnWattage() {
        return onWattage;
    }

    public double getProbability() {
        return probability;
    }

    public int getLocation() {
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

    public String toString() {
		return location + "," + applianceId + "," + onWattage + "," + probability + "," + type;
	}
    
    // method to turn appliance on or off based on probability
    public void updateStatus() {
        Random random = new Random();
        double randomValue = random.nextDouble();

        // check probability to turn on/off appliance
        isOn = randomValue < probability;
    }

    
}
