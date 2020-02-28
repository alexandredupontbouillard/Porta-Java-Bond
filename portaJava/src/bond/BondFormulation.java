package bond;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import exception.UnknownCommandException;
import exception.UnknownVariableName;
import formulation.AbstractFormulation;
import formulation.LPReader;
import formulation.Variable;
import formulation.example.KnapsackFormulation.InvalidKnapsackInputFile;

public class BondFormulation extends AbstractFormulation{

	public int n; // nombre de sommets
	public int m; // nombre d'arêtes
	public ArrayList<ArrayList<Integer>> adj; //liste d'djacence
	
	public BondFormulation(int n,int m, ArrayList<ArrayList<Integer>> ajd) throws UnknownCommandException, IOException, InterruptedException {
		super();
		this.n= n;
		this.m = m;
		this.adj = adj;
		
		// TODO Auto-generated constructor stub
	}
	public BondFormulation(String inputFile) throws UnknownCommandException, IOException, InterruptedException {

		super();
		
		try{
			InputStream ips=new FileInputStream(inputFile);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			String[] sLine;
			this.n = -1;
			this.m = -1;
			this.adj = new ArrayList<ArrayList<Integer>>();

			line=br.readLine();
			if(line.contains("n = ")) { 
				sLine = line.split("=");

				if(sLine.length != 2)
					System.err.println("Error the input file \"" + inputFile + "\" is not properly formated. The line \"" + line + "\" should be as follows: \"n = X\" with X an integer");				
				else {
						try {
							this.n = Integer.parseInt(sLine[1].trim());
							
						}catch(NumberFormatException e) {
							System.err.println("The line \"" + line + "\" must contain an integer after the \"=\" symbol. The program failed at converting the value to an integer.");
							e.printStackTrace();
						}
				}
			}
			
			line=br.readLine();
			if(line.contains("m = ")) { 
				sLine = line.split("=");

				if(sLine.length != 2)
					System.err.println("Error the input file \"" + inputFile + "\" is not properly formated. The line \"" + line + "\" should be as follows: \"m = X\" with X an integer");				
				else {
						try {
							this.m = Integer.parseInt(sLine[1].trim());
							
						}catch(NumberFormatException e) {
							System.err.println("The line \"" + line + "\" must contain an integer after the \"=\" symbol. The program failed at converting the value to an integer.");
							e.printStackTrace();
						}
				}
			}
			int x1;
			int x2;
			ArrayList<Integer> couple;
			for(int i = 0 ; i< m ;i++) {
				line=br.readLine();
				sLine = line.split(" ");
				x1 = Integer.parseInt(sLine[0].trim());
				x2 = Integer.parseInt(sLine[1].trim());
				couple = new ArrayList<Integer>();
				couple.add(x1);
				couple.add(x2);
				this.adj.add(couple);
			}
		}

			
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getConstraints() throws UnknownVariableName {
		
		
		
		return null;
	}

	@Override
	protected void createVariables() {
		for(int i = 0; i <= n; ++i)
			for(int j = 0; j<= n;j++)
			this.registerVariable(new Variable("x" + i + "" + j, 0, 1)); // créé une variable pour chaque arête
		
	}

}
