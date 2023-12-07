public class Appliance {
    
    private String type;
    private int onWattageUsed;
    private double probOn;
    private int location;
    private int ID;
    
    public Appliance(String type, int onWattageUsed, double probOn, int location, int ID){
        this.type = type;
        this.onWattageUsed = onWattageUsed;
        this.probOn = probOn;
        this.location = location;
        this.ID = ID;
    }
}