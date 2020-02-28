package formulation.ongoingwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exception.InvalidPCenterInputFile;
import exception.UnknownCommandException;
import exception.UnknownVariableName;

public class PCenterPCSCZCompletelyOrdered extends PCenterPCSC{

	public PCenterPCSCZCompletelyOrdered(String inputFile) throws IOException,
	InvalidPCenterInputFile, UnknownCommandException, InterruptedException {
		super(inputFile);
	}


	@Override
	public String getConstraints() throws UnknownVariableName {

		String output = "";

		/* At least one factory */
		for(int i = 1 ; i < M ; i++)
			output += portaName("y" + i) + " + "; 

		output += portaName("y" + M) + " >= 1\n";

		/* At most M factories */
		for(int i = 1 ; i < M ; i++)
			output += portaName("y" + i) + " + "; 

		output += portaName("y" + M) + " <= " + p + "\n";

		/* The z variables are sorted */
		for(int k = 1 ; k < K() ; k++)
			output += portaName("z" + (k+1)) + " - " + portaName("z" + k) + " <= 0\n";

		/* Link between z and y variables */	
		for(int i = 1 ; i <= N ; i++){

			System.out.println("Client n°" + i );

			Iterator<Double> it = D.iterator();

			/* The first D value is not used in the formulation (it is just a constant in the objective) */
			it.next();

			List<String> previousYVariables = null;

			for(int k = 1 ; k <= K() ; k++){

				List<String> currentYVariables = new ArrayList<>();

				/* Get the radius associated to variable zk */
				Double currentD = it.next();

				System.out.println("CurrentD: " + currentD);

				for(int j = 1 ; j <= M ; j++){

					if(d[i-1][j-1] < currentD)
						currentYVariables.add("y" + j);
				}

				if(k != 1){
					System.out.println("--\ncurrent y var: " + currentYVariables);
					System.out.println("previous y var: " + previousYVariables);
				}

				/* If the previous K contained less y variables */
				if(k != 1 && currentYVariables.size() > previousYVariables.size()){

					for(String yVar: previousYVariables)
						output += portaName(yVar) + " + ";

					System.out.println("z" + (k-1) + " >= 1 <- added\n");
					output += portaName("z" + (k-1)) + " >= 1\n";	
				}
				else if(k != 1)
					System.out.println("z" + (k-1) + " >= 1 <- not added\n");

				previousYVariables = currentYVariables;

			}	

			/* Add the last k inequality */
			for(String yVar: previousYVariables)
				output += portaName(yVar) + " + ";

			System.out.println("--\n" + previousYVariables);
			System.out.println("z" + K() + " >= 1 <- added\n");
			output += portaName("z" + K()) + " >= 1\n";	

		}


		Iterator<Double> it = D.iterator();

		double previousD = it.next();
		String inequalitySuffix = " <= " + (int)previousD + "\n";
		String inequalitySuffix2 = " <= - " + ((int)previousD - 1);
		
		for(int k = 1 ; k <= K() ; k++){
			
			double currentD = it.next();
			double coefficient = currentD - previousD;
			inequalitySuffix  = " - " + ((int)coefficient) + portaName("z" + k) + inequalitySuffix;
			inequalitySuffix2 = " + " + ((int)coefficient) + portaName("z" + k) + inequalitySuffix; 
			previousD = currentD;
		}
		
		it = D.iterator();
		for(int k = 1 ; k <= K() ; k++){
			int currentD = it.next().intValue();
			output += currentD + portaName("z" + k) + inequalitySuffix;
			output += currentD + portaName("z" + k) + inequalitySuffix2 + " + " + currentD + "\n";
			
		}

		return output;
	}

	public static void main(String[] args){

		try {
			String inputFile = "pc3_2";//"pc5_3"; //"pc20_5";
			PCenterPCSCZCompletelyOrdered formulation = new PCenterPCSCZCompletelyOrdered("./data/" + inputFile + ".dat");
			//
			System.out.println(formulation.getIPDimension());
			//			Porta.getFacets(formulation, "./.tmp/" + inputFile + "_ordered_facets.ieq");

			formulation.convertPOIFile("./.tmp/tmp.poi", "./.tmp/" + inputFile + "_nOrdered_converted_integer_points.poi");
			formulation.convertIEQFile("./.tmp/tmp.ieq", "./.tmp/" + inputFile + "_nOrdered_converted_formulation.ieq", false);
			//			formulation.convertIEQFile("./.tmp/tmp.poi.ieq", "./.tmp/" + inputFile + "_ordered_converted_facets.ieq");
			//			formulation.convertIEQFile("./.tmp/tmp.poi.ieq", "./.tmp/converted_facets.ieq");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
