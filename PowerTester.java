public class PowerTester {
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
			option1=scan.nextLine();
			
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
