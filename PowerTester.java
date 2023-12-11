import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;

public class PowerTester {
	static ArrayList<Appliance> wholeArea = new ArrayList<>();
	static ArrayList<Integer> locationList = new ArrayList<>();
	static ArrayList<ArrayList<Appliance>> divArea = new ArrayList<>();
	static ArrayList<Appliance> onApps = new ArrayList<>();
	static ArrayList<SmartAppliance> smartOnApps = new ArrayList<>();
	static Scanner scnr = new Scanner(System.in);
	static String folderName = "PowerGrid";
	static String directoryPath = "C:/";
	static String fileName = "Simulation.txt";

	public static void fileWrite(int time, int preWatt, int postWatt, int sLow, int locationCount, int appCount) {

		// Create the folder
		File directory = new File(directoryPath, folderName);
		directory.mkdirs();

		// Combine the directory path and file name to create the complete file path
		String filePath = directoryPath + folderName + "/" + fileName;
		try {
			// Create a FileWriter object to write to the file
			FileWriter writer = new FileWriter(filePath, true);

			// Write and print the current time step
			writer.write("/////////////// Time Step: " + (time + 1) + " ///////////////");
			writer.write(System.lineSeparator());
			writer.write("Total Starting Wattage: " + preWatt);
			writer.write(System.lineSeparator());
			writer.write("Total Wattage After: " + postWatt);
			writer.write(System.lineSeparator());
			writer.write("Ammount of Smart Appliances set to Low: " + sLow);
			writer.write(System.lineSeparator());
			writer.write("Ammount of Locations Browned Out: " + locationCount);
			writer.write(System.lineSeparator());
			writer.write("Amount of Appliances Turned Off: " + appCount);
			writer.write(System.lineSeparator());
			writer.write(System.lineSeparator());
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
					int location = parse.nextInt();
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
				for (int j = i + 1; j < locationList.size(); ++j) {
					if (locationList.get(i).equals(locationList.get(j))) {
						locationList.remove(j);
						--j;
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	public static void simulation() {

		int maxWattage = 0;
		int timeSteps = 0;
		int[] locationPoints = new int[locationList.size()];
		int[] smartPoints = new int[locationList.size()];

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

		for (int k = 0; k < timeSteps; ++k) {
			divArea.clear();
			ArrayList<Integer> totalLocations = (ArrayList<Integer>) locationList.clone();
			int totalWattage = 0;
			int locationCounter = 0;
			int T = 0;
			int appCount = 0;
			// Loop to initialise whether an appliance is on or off
			for (int i = 0; i < wholeArea.size(); ++i) {
				wholeArea.get(i).setIsOn(false);
				wholeArea.get(i).updateStatus();
				if (wholeArea.get(i).getIsOn() && wholeArea.get(i).getType()) {
					totalWattage += wholeArea.get(i).getOnWattage();
					// smartOnApps.add((SmartAppliance) wholeArea.get(i));

				} else if (wholeArea.get(i).getIsOn()) {
					totalWattage += wholeArea.get(i).getOnWattage();
					// onApps.add(wholeArea.get(i));
				}
			}

			// intialize the divArea arrayList with the appliances in each location
			for (int i = 0; i < locationList.size(); ++i) {
				ArrayList<Appliance> locale = new ArrayList<>();
				for (int j = 0; j < wholeArea.size(); ++j) {
					if (locationList.get(i).equals(wholeArea.get(j).getLocation()) && wholeArea.get(j).getIsOn()) {
						locale.add(wholeArea.get(j));
					}
				}
				divArea.add(locale);
			}
			/////////////////////////////////////////
			//////// Start of the process for turning appliances off or low
			///////////////////////////////////////////////////////////
			int preWattage = totalWattage;

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
					// comparator amd collection to sort the Smart Appliances from lowest to highest
					// wattage
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
					appCount += divArea.get(locationLow).size();
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

			for (int i = 0; i < locationList.size(); ++i) {
				int location = locationList.get(i);
				for (int h = 0; h < smartOnApps.size(); ++h) {
					if (smartOnApps.get(h).getOnLow() && smartOnApps.get(h).getLocation() == location) {
						smartPoints[i] += 1;
					}
				}
				for (int j = 0; j < totalLocations.size(); ++j) {
					if (location == totalLocations.get(j)) {
						locationPoints[i] += 1;
					}
				}
			}
			/////////////////////////////////////////
			//////// Printing data from the Time Step to console and file
			///////////////////////////////////////////////////////////
			System.out.println("/////////////// Time Step: " + (k + 1) + " ///////////////");
			System.out.println("Total Starting Wattage: " + preWattage);
			System.out.println("Total Wattage After: " + totalWattage);
			System.out.println("Ammount of Smart Appliances set to Low: " + T);
			System.out.println("Ammount of Locations Browned Out: " + locationCounter);
			System.out.println("Amount of Appliances Turned Off: " + appCount);
			System.out.println();
			fileWrite(k, preWattage, totalWattage, T, locationCounter, appCount);
		}

		/////////////////////////////////////////
		//////// Printing Summary of all Time Steps to console and file
		///////////////////////////////////////////////////////////
		System.out.println("/////////////// Summary ///////////////");
		ArrayList<Integer> brownLocation = new ArrayList<>();
		int impactLocation = 0;
		int active = locationPoints[0];
		int smart = 0;
		for (int i = 0; i < locationPoints.length; ++i) {
			if (locationPoints[i] == 0) {
				brownLocation.add(i);
			}
			if (active > locationPoints[i]) {
				active = locationPoints[i];
			}
		}
		for (int i = 0; i < smartPoints.length; ++i) {
			if (active == locationPoints[i] && smart <= smartPoints[i]) {
				smart = smartPoints[i];
				impactLocation = i;

			}
		}
		// Create the folder
		File directory = new File(directoryPath, folderName);
		directory.mkdirs();

		// Combine the directory path and file name to create the complete file path
		String filePath = directoryPath + folderName + "/" + fileName;

		try {
			// Create a FileWriter object to write to the file
			FileWriter writer = new FileWriter(filePath, true);
			writer.write("/////////////// Summary ///////////////");
			writer.write(System.lineSeparator());
			if (smart == 0 && active == 0) {
				int brown = 0;
				for (int i = 0; i < locationPoints.length; ++i) {
					if (locationPoints[i] == 0) {
						brown += 1;
					}
				}
				writer.write(brown + " locations were browned out every time for all " + timeSteps + " runs");
				writer.write(System.lineSeparator());
				System.out.println(brown + " locations were browned out every time for all " + timeSteps + " runs");
				System.out.println("List of those locations: ");
				writer.write("List of those locations: ");
				writer.write(System.lineSeparator());
				for (int i = 0; i < brownLocation.size(); ++i) {
					System.out.println(locationList.get(brownLocation.get(i)));
					writer.write(Integer.toString(locationList.get(brownLocation.get(i))));
					writer.write(System.lineSeparator());
				}

			} else if (timeSteps == locationPoints[impactLocation]) {
				writer.write("Most Impacted Location: " + locationList.get(impactLocation) + " with "
						+ smartPoints[impactLocation] +
						" total appliances set to low over the course of " + timeSteps + " runs");
				System.out.println("Most Impacted Location: " + locationList.get(impactLocation) + " with "
						+ smartPoints[impactLocation] +
						" total appliances set to low over the course of " + timeSteps + " runs");

			} else {
				writer.write("Most Impacted Location: " + locationList.get(impactLocation) + " with "
						+ (timeSteps - locationPoints[impactLocation]) +
						" total brown outs and " + smartPoints[impactLocation]
						+ " total appliances set to low over the course of " + timeSteps + " runs");
				System.out.println("Most Impacted Location: " + locationList.get(impactLocation) + " with "
						+ (timeSteps - locationPoints[impactLocation]) +
						" total brown outs and " + smartPoints[impactLocation]
						+ " total appliances set to low over the course of " + timeSteps + " runs");
			}

			System.out.println();
			writer.close();
		} catch (IOException e) {
			// Handle IO exception
			e.printStackTrace();
		}
		scnr.nextLine();

	}

	public static void addApplication() {
		System.out.println("Type \"true\" if the appliance is smart:");
		boolean type = scnr.nextBoolean();
		System.out.println("Enter the appliance's location ID:");
		int location = scnr.nextInt();
		System.out.println("Enter the name of the appliance:");
		String name = scnr.next();
		System.out.println("Enter the appliance's watt usage:");
		int watts = scnr.nextInt();
		System.out.println("Enter the appliance's probability of being on:");
		double prob = scnr.nextDouble();
		if (type) {
			System.out.println("Enter the appliance's low usage percent (1.0-0.0):");
			double low = scnr.nextDouble();
			SmartAppliance newSmart = new SmartAppliance(type, watts, prob, location, name, low);
			wholeArea.add(newSmart);
		} else {
			Appliance newApp = new Appliance(type, watts, prob, location, name);
			wholeArea.add(newApp);
		}
		System.out.println("Successfully added your appliance");

	}

	public static void deleteApp() {
		System.out.println("Enter the index number of the appliance to delete:");
		int input = scnr.nextInt();
		if (input >= wholeArea.size() || input < 0) {
			System.out.println("Enter a valid index number:");
			input = scnr.nextInt();
		}
		wholeArea.remove(input);
		System.out.println("Successfully removed appliance");
	}

	public static void main(String[] args) {

		// User interactive part
		char option1;

		// clear file before use
		String file = directoryPath + folderName + "/" + fileName;
		try {
			File directory = new File(directoryPath, folderName);
			directory.mkdirs();
			// Create a FileWriter object to write to the file
			FileWriter writer = new FileWriter(file);
			writer.close();
		} catch (IOException e) {
			// Handle IO exception
			e.printStackTrace();
		}

		while (true) {// Application menu to be displayed to the user.
			System.out.println("Select an option:");
			System.out.println("Type \"A\" Add an appliance");
			System.out.println("Type \"D\" Delete an appliance");
			System.out.println("Type \"L\" List the appliances");
			System.out.println("Type \"F\" Read Appliances from a file");
			System.out.println("Type \"S\" To Start the simulation");
			System.out.println("Type \"Q\" Quit the program");
			option1 = scnr.nextLine().charAt(0);

			if (option1 == 'A') {
				addApplication();
			} else if (option1 == 'D') {
				deleteApp();
			} else if (option1 == 'L') {
				for (int i = 0; i < wholeArea.size(); ++i) {
					System.out.println(wholeArea.get(i).toString());
				}
			} else if (option1 == 'F') {
				System.out.println("Enter the file path: ");
				String filePath = scnr.nextLine();
				readAppFile(filePath);
			} else if (option1 == 'S') {
				if (wholeArea.size() <= 0) {
					System.out.println("Please add appliances first, either manually or from a file.");
				} else {
					simulation();
				}
			} else if (option1 == 'Q') {
				System.exit(0);
			} else {
				System.out.println("Invalid Input");
			}

		}
	}
}
