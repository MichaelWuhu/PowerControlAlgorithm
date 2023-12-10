import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;

public class PowerTester {
	static ArrayList<Appliance> wholeArea = new ArrayList<>();
	static ArrayList<Long> locationList = new ArrayList<>();
	static ArrayList<ArrayList<Appliance>> divArea = new ArrayList<>();
	static ArrayList<Appliance> onApps = new ArrayList<>();
	static ArrayList<SmartAppliance> smartOnApps = new ArrayList<>();
	static Scanner scnr = new Scanner(System.in);
	static String folderName = "PowerGrid";
	static String directoryPath = "C:/";
	
	public static void fileWrite(int time) {
		// Names for the file path
        String fileName = "TimeStep " + (time+1) + ".txt";

        // Create the folder
        File directory = new File(directoryPath, folderName);
        directory.mkdirs();

        // Combine the directory path and file name to create the complete file path
        String filePath = directoryPath + folderName + "/" + fileName;
        try {
            // Create a FileWriter object to write to the file
            FileWriter writer = new FileWriter(filePath);

            // Write and print the current time step
            for (int i = 0; i < divArea.size(); ++i) {
            	ArrayList<Appliance> locale = divArea.get(i);
            	for (int j = 0; j < locale.size(); ++j) {
            		writer.write(locale.get(j).toString());
                    writer.write(System.lineSeparator());
            	}
            	
            }
            writer.close();
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }
	}
	
	public static void readAppFile(String file) {
		try {
			Scanner scnr = new Scanner(new File(file));
			while (scnr.hasNext()) {
				String placeholder = scnr.nextLine();
				try (Scanner parse = new Scanner(placeholder)) {
					parse.useDelimiter(",");
					long location = parse.nextLong();
					String name = parse.next();
					int powerUse = parse.nextInt();
					double prob = parse.nextDouble();
					boolean type = parse.nextBoolean();
					double percent = parse.nextDouble();
					locationList.add(location);
					if (type) {
						SmartAppliance smartApp = new SmartAppliance(type, powerUse, prob, location, name, percent);
						wholeArea.add(smartApp);
					} else {
						Appliance App = new Appliance(type, powerUse, prob, location, name);
						wholeArea.add(App);
					}
				}
			}
			
			for (int i = 0; i < locationList.size(); ++i) {
				for (int j = i+1; j < locationList.size(); ++j) {
					if (locationList.get(i).equals(locationList.get(j))) {
						locationList.remove(j);
						--j;
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found. Try again.");
		}
	}
	public static void simulation() {
		
		int maxWattage = 0;
		int timeSteps = 0;
		//ArrayList<Long> totalLocations = (ArrayList<Long>) locationList.clone();
		
		System.out.println("Enter total allowed wattage:");
		maxWattage = scnr.nextInt();
		while (maxWattage < 0) {
			System.out.println("Please input a positive integer: ");
			maxWattage = scnr.nextInt();
		}
		
		System.out.println("Enter desired amount of time steps for simulation:");
		timeSteps = scnr.nextInt();
		while (timeSteps <= 0) {
			System.out.println("Please input a positive integer: ");
			timeSteps = scnr.nextInt();
		}
		
		/////////////////////////////////////////
		//////// Going through each time step
		///////////////////////////////////////////////////////////
		
		for ( int k = 0; k < timeSteps; ++k ) {
			divArea.clear();
			ArrayList<Long> totalLocations = (ArrayList<Long>) locationList.clone();
			int totalWattage = 0;
			int locationCounter = 0;
			int T = 0;
			// Loop to initialise whether an appliance is on or off
			for (int i = 0; i < wholeArea.size(); ++i) {
				wholeArea.get(i).setIsOn(false);
				wholeArea.get(i).updateStatus();
				if (wholeArea.get(i).isOn() && wholeArea.get(i).getType()) {
					totalWattage += wholeArea.get(i).getOnWattage();
					//smartOnApps.add((SmartAppliance) wholeArea.get(i));
					
				} else if (wholeArea.get(i).isOn()) {
					totalWattage += wholeArea.get(i).getOnWattage();
					//onApps.add(wholeArea.get(i));
				}
			}
			
			// intialize the divArea arrayList with the appliances in each location
			for (int i = 0; i < locationList.size(); ++i) {
				ArrayList<Appliance> locale = new ArrayList<>();
				for (int j = 0; j < wholeArea.size(); ++j) {
					if (locationList.get(i).equals(wholeArea.get(j).getLocation()) && wholeArea.get(j).isOn()) {
						locale.add(wholeArea.get(j));
					}
				}
				divArea.add(locale);
			}
			/////////////////////////////////////////
			//////// Start of the process for turning appliances off
			///////////////////////////////////////////////////////////
			System.out.println("Total Wattage: " + totalWattage);
			while (totalWattage > maxWattage) {
				
				totalWattage = 0;
				smartOnApps.clear();
				onApps.clear();
				
				for (int i = 0; i < divArea.size(); ++i) {
					ArrayList<Appliance> currentLocation = divArea.get(i);
					for (int j = 0; j < currentLocation.size(); ++j) {
						if (currentLocation.get(j).getType()) {
							smartOnApps.add((SmartAppliance) currentLocation.get(j));
						} else {
							onApps.add(currentLocation.get(j));
						}
					}
				}
				
				for (int i = 0; i < smartOnApps.size(); ++i) {				
					smartOnApps.get(i).setOnLow(true);
				}
				
				// checker if all Smart Appliances have been set to low
				if (T < smartOnApps.size()) {
					++T;
					// comparator amd collection to sort the Smart Appliances from lowest to highest wattage
					Comparator<SmartAppliance> comparator = Comparator.comparing(SmartAppliance::getOnWattage);
					
					Collections.sort(smartOnApps, comparator);

					for (int i = T; i < smartOnApps.size(); ++i) {
						smartOnApps.get(i).setOnLow(false);
					}
					
				} else {
					
					++locationCounter;
					int applianceCount = divArea.get(0).size();
					int locationLow = 0;			
					
					// for loop to find the location with the least amount of On Appliances
					
					for (int i = 0; i < divArea.size(); ++i) {
						if (applianceCount > divArea.get(i).size()) {
							applianceCount = divArea.get(i).size();
							locationLow = i;
						}
					}
					totalLocations.remove(locationLow);
					divArea.remove(locationLow);
					smartOnApps.clear();
					onApps.clear();
					
					for (int i = 0; i < divArea.size(); ++i) {
						ArrayList<Appliance> currentLocation = divArea.get(i);
						for (int j = 0; j < currentLocation.size(); ++j) {
							if (currentLocation.get(j).getType()) {
								smartOnApps.add((SmartAppliance) currentLocation.get(j));
							} else {
								onApps.add(currentLocation.get(j));
							}
						}
					}
					T = smartOnApps.size();
				}
				
				for (int i = 0; i < onApps.size(); ++i) {
					totalWattage += onApps.get(i).getOnWattage();
				}
				
				for (int i = 0; i < smartOnApps.size(); ++i) {
					totalWattage += smartOnApps.get(i).getOnWattage();
				}
				
				
			}
			
			/////////////////////////////////////////
			//////// Printing data from the Time Step
			///////////////////////////////////////////////////////////
			
			System.out.println("Total Wattage After: " + totalWattage);
			System.out.println("Ammount of Smart Appliances set to Low: " + T);
			System.out.println("Ammount of Locations Browned Out:" + locationCounter);
			System.out.println();
			fileWrite(k);
			
		}
	}
	
	public static void addApplication() {
		System.out.println("Filler");
	}
	
	public static void main(String[] args) {

		//User interactive part
		String option1;
		while(true){// Application menu to be displayed to the user.
			System.out.println("Select an option:");
			System.out.println("Type \"A\" Add an appliance");
			System.out.println("Type \"D\" Delete an appliance");	
			System.out.println("Type \"L\" List the appliances");
			System.out.println("Type \"F\" Read Appliances from a file");
			System.out.println("Type \"S\" To Start the simulation");
			System.out.println("Type \"Q\" Quit the program");
			option1 = scnr.nextLine();
			
			if (option1.equals("A")) {
				addApplication();
			}
			else if (option1.equals("D")) {
				
			}
			else if (option1.equals("L")) {
				
			}
			else if (option1.equals("F")) {
				System.out.println("Enter the file path: ");
				String filePath = scnr.nextLine();
				readAppFile(filePath);
			}
			else if (option1.equals("S")) {
				if (wholeArea.size() <= 0) {
					System.out.println("Please add appliances first, either manually or from a file.");
				} else {
					simulation();
				}
			}
			else if (option1.equals("Q")) {
				System.exit(0);
			}
			else {
				System.out.println("Invalid Input");
			}
			
		}
	}

}
