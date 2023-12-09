public class Location {
    private int locationNo;
    private String[] appliances;

     public Location(int locationNo, String[] appliances) {
        this.locationNo = locationNo;
        this.appliances = appliances;
     }

    public int getLocationNo() {
        return locationNo;
    }

    public String[] getAppliances(){
        return appliances;
    }

    public int getNumAppliances(){
        return appliance.length;
    }

    public void brownOut(){
        for (int i = 0 ; i < appliance.length ; i++){
                appliances[i].isOn(false);
        }
    }

    public String toString(){
        return "Location Number: " locationNo + "Appliances: " + Arrays.toString(appliances));
    }

}
