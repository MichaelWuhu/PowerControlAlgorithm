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
        int indexSmartOn;
        int indexOn;
        for (int i = 0 ; i < appliance.length ; i++){
            if (appliances[i].getIsSmart) {
                appliance[i].setOnlow(true);
                indexSmartOn += 1;
            } else{
                appliances[i].isOn(false);
                indexOn +=1;
            }
        }
    }

    public String toString(){
        return "Number of smart appliances set low: " indexSmartOn + "Number of appliances turned off: " + indexOn;
    }

}
