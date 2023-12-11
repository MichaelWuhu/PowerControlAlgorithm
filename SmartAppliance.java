public class SmartAppliance extends Appliance{

    private double lowModeReduction;
    boolean onLow;
    private int onWattage = super.getOnWattage();
	
    public SmartAppliance(boolean type, int onWattage, double probability, int location, String applianceId, double lowModeReduction) { // initializes SmartAppliance object
        super(applianceId, onWattage, probability, location, applianceId);
        this.onLow = onLow;
        this.lowModeReduction = lowModeReduction;
    }

    public void setOnLow(boolean onLow) { //setter method for onLow boolean
        this.onLow = onLow;
    }
    public boolean getOnLow() {
		return this.onLow;
	}

	@Override
	public String toString() {
		return super.toString() + "," + lowModeReduction;
	}
	
    public int getOnWattage() { //checks to see whether to return normal watts if onLow is false, or low watts if onLow is true
        if (onLow == true) {
            return (int) ((double)(onWattage) * lowModeReduction); //returns low wattage (normal onWatts times lowMode percentage)
        }
        else {
            return onWattage; // returns normal watts if onLow is false
        }
    }




    
}
