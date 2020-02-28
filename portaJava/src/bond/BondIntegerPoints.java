package bond;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import exception.InvalidIEQFileFormatException;
import exception.UnknownCommandException;
import exception.UnknownVariableName;
import formulation.AbstractIntegerPoints;
import formulation.IntegerPoint;
import formulation.Variable;
import graph.Graph;
public class BondIntegerPoints extends AbstractIntegerPoints{

	int n; // nombre de sommets
	int m; // nombre d'arêtes
	ArrayList<ArrayList<Integer>> adj; // liste d'adjacence
	
	public BondIntegerPoints(int n,int m, ArrayList<ArrayList<Integer>> ajd) throws UnknownCommandException, IOException, InterruptedException {
		//super();
		this.n= n;
		this.m = m;
		this.adj = adj;
		
		// TODO Auto-generated constructor stub
	}
	public BondIntegerPoints(String inputFile) throws UnknownCommandException, IOException, InterruptedException {

		//super();
		
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
	public void createIntegerPoints() throws UnknownVariableName {
		ArrayList<ArrayList<Integer>> SE = getSE(n);
		ArrayList<ArrayList<Integer>> cut ;
		String s;
		int x1;
		int x2;
		IntegerPoint point = new IntegerPoint(this);
		for(int i = 0 ; i < SE.size();i++) {
			if(verifieConSE(adj,SE.get(i),n)){
				point = new IntegerPoint(this);
				cut = getCut(SE.get(i));
				for(int j = 0; j<cut.size();j++) {
					x1= cut.get(j).get(0);
					x2 = cut.get(j).get(1);
					if(x1 < x2 ) {
						s = "x" +x1 +""+x2;	
					}else {
						s = "x" +x2 +""+x1;	
					}
						
					
					point.setVariable(s, 1);
				}
				//mettre à jour point
				addIntegerPoint(point);
			}
		}
		

		
	}
	public ArrayList<ArrayList<Integer>> getCut(ArrayList<Integer> SE){
		ArrayList<Integer> SECOMP = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		boolean t;
		for(int i = 1; i <= n;i++) {
			t = true;
			for(int j = 0 ; j< SE.size(); j ++) {
				if(i == SE.get(j)) {
					t = false;
				}
			}
			if(t) {
				SECOMP.add(i);
			}
		}
		ArrayList<Integer> r;
		for(int i = 0 ; i < SE.size();i++) {
			for(int j = 0 ; j < SECOMP.size(); j ++) {
				for(int w  = 0 ; w< adj.size();w++) {
					if(adj.get(w).get(0)== SE.get(i) && adj.get(w).get(1)== SECOMP.get(j) ) {
						r = new ArrayList<Integer>();
						r.add(SE.get(i));
						r.add(SECOMP.get(j));
						result.add(r);
					}
					if(adj.get(w).get(1)== SE.get(i) && adj.get(w).get(0)== SECOMP.get(j) ) {
						r = new ArrayList<Integer>();
						r.add(SE.get(i));
						r.add(SECOMP.get(j));
						result.add(r);
					}
				}
				
			}
		}
		return result;
	}
	
	
	public static boolean verifieConSE(ArrayList<ArrayList<Integer>> adj,ArrayList<Integer> SE, int n) {
		if(SE.size() >0) {
		boolean b1 = new Graph(adj,SE).connexity();
		
		ArrayList<Integer> SECOMP = new ArrayList<Integer>();
		boolean t;
		for(int i = 1; i <= n;i++) {
			t = true;
			for(int j = 0 ; j< SE.size(); j ++) {
				if(i == SE.get(j)) {
					t = false;
				}
			}
			if(t) {
				SECOMP.add(i);
			}
		}
		
		
		if(SECOMP.size()>0) {
		boolean b2 = new Graph(adj,SECOMP).connexity();
		return b1 && b2 ;
		}else return true;}
		else return true;
	}
	public static ArrayList<ArrayList<Integer>> getSE(int n){
		int x = puissance(2,n);
		String c;
		String letter;
		int taille;
		ArrayList<ArrayList<Integer>> l = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> t;
		for(int i = 0; i< x; i ++) {
			c = Integer.toBinaryString(i);
			taille = c.length();
			t = new ArrayList<Integer>();
			for(int j = 0; j <taille; j ++) {
				letter = Character.toString(c.charAt(j));
				if(letter.contentEquals("1")) {
					t.add(taille - j);
					
				}
			}
			l.add(t);
		}
		return l;
		
	}
	public static int puissance(int p,int n) {
		if(n==1) return p;
		else return p * puissance(p,n-1);
	}
	
	@Override
	protected void createVariables() {
		for(int i = 1; i <= adj.size(); ++i)
			
				
					this.registerVariable(new Variable("x" + adj.get(i-1).get(0)+""+adj.get(i-1).get(1), 0, 1));
				
	}
	
	public enum Use{
			
			/* Get the dimension of the integer polytope of a formulation, also returns the hyperplans which include the polytope */
			DIMENSION,
	
			/* Get feasible integer solutions of the problem */
			INTEGER_POINTS, 
	
			/* Get the facets of the problem */
			FACETS,
			
			/* Get the extrem points of the polytope of the linear relaxation */
			CONTINUOUS_EXTREME_POINTS,
			
			/* Get the extrem points of the polytope of the linear relaxation */
			INTEGER_EXTREME_POINTS
		}

	public static void main(String[] args) {
	
		
			try {
				BondIntegerPoints b = new BondIntegerPoints("./data/test.txt");
				ArrayList<ArrayList<Integer>> adj = b.adj;
				int n = b.n;
				ArrayList<ArrayList<Integer>> e = b.getSE(n);
				for(int i= 0 ; i < e.size();i++) {
					if(verifieConSE(adj,e.get(i),n)) {
						System.out.println(e.get(i));
						System.out.println("tak");
						System.out.println(b.getCut(e.get(i)));
					}
				}
				try {
					System.out.println(b.getIPFacets());
				} catch (UnknownVariableName e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidIEQFileFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (UnknownCommandException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
		
	
	
	


}
