import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class PowerTester {
	static ArrayList<Appliance> wholeArea = new ArrayList<>();
	static ArrayList<ArrayList<Appliance>> divArea = new ArrayList<>();
	static ArrayList<Long> locationList = new ArrayList<>();
	static Scanner scnr = new Scanner(System.in);
	
	public void readAppFile(String file) {
		try {
			Scanner scnr = new Scanner(new File(file));
			while (scnr.hasNext()) {
				String placeholder = scnr.nextLine();
				try (Scanner parse = new Scanner(placeholder)) {
					parse.useDelimiter(",");
					long location = parse.nextLong();
					String name = parse.next();
					int powerUse = parse.nextInt();
					double state = parse.nextDouble();
					boolean type = parse.nextBoolean();
					double percent = parse.nextDouble();
					locationList.add(location);
					if (type) {
						SmartAppliance smartApp = new SmartAppliance(type, powerUse, state, location, name, percent);
						wholeArea.add(smartApp);
					} else {
						Appliance App = new Appliance(type, powerUse, state, location, name);
						wholeArea.add(app);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				
			}
			else if (option1.equals("D")) {
				
			}
			else if (option1.equals("L")) {
				
			}
			else if (option1.equals("F")) {
				
			}
			else if (option1.equals("S")) {
				
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
