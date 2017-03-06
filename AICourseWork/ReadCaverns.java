import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Set;

public class ReadCaverns {

	
	
	public static void main(String[] args) throws IOException {
		// Open input.cav
		BufferedReader br = new BufferedReader(new FileReader("input.cav"));
		
		//Read the line of comma separated text from the file
		String buffer = br.readLine(); 
		System.out.println("Raw data : " + buffer);
		br.close();
		
		//Convert the data to an array
		String[] data = buffer.split(",");
		Vector <Cave> caves = new Vector <Cave>();
		//Now extract data from the array - note that we need to convert from String to int as we go
		int noOfCaves = Integer.parseInt(data[0]);
		System.out.println ("There are " + noOfCaves + " caves.");
		
		//Get coordinates
		for (int count = 1; count < ((noOfCaves*2)+1); count=count+2){
			System.out.println("Cave at " + data[count] +"," +data[count+1]);
			caves.add(new Cave(Integer.parseInt(data[count]),Integer.parseInt(data[count+1])));
		}
		for(Cave c : caves)
		{
			c._h = getDistance(c,caves.get(caves.size()-1));
		}
		
		//Build connectivity matrix
		
		//Declare the array
		boolean[][] connected = new boolean[noOfCaves][];
		
		for (int row= 0; row < noOfCaves; row++){
			connected[row] = new boolean[noOfCaves];
		}
		//Now read in the data - the starting point in the array is after the coordinates 
		int col = 0;
		int row = 0;
		
		for (int point = (noOfCaves*2)+1 ; point < data.length; point++){
			//Work through the array
			
			if (data[point].equals("1"))
			   connected[row][col] = true;
			else
				connected[row][col] = false;
				
			col++;
			if (col == noOfCaves){ 
				col=0;
				row++;
			}
		}
		//Connected now has the adjacency matrix within it 
		AStar(caves,1,7,connected);

	}
	static public double getDistance (Cave a,Cave b)
	{
		double distance = Math.sqrt(Math.pow((b.getX()-a.getX()),2)+Math.pow((b.getY()-a.getY()),2));
		return distance;
		
	}
	static public void AStar(Vector<Cave> caves,int start,int end,boolean[][] connected)
	{
		HashMap<Integer,Cave> OpenList = new HashMap<Integer,Cave>();
		OpenList.put(start, caves.get(start));
		HashMap<Integer,Cave> ClosedList = new HashMap<Integer,Cave>();
		int current_cave = start;
		double current_cave_cost = 0;
		Vector<Integer> succesors;
		caves.get(start)._g  = 0;
		caves.get(start)._f  = caves.get(start)._h;
		while(!OpenList.isEmpty())
		{
          current_cave = BestOption(OpenList);
         
          if(current_cave == end)
          {
        	  System.out.println("Path found!");
        	  break;
          }
          succesors = getSuccesors(caves,current_cave,connected);
          OpenList.remove(current_cave);
          for(int i : succesors)
          {
        	  current_cave_cost = caves.get(current_cave)._g + getDistance(caves.get(current_cave),caves.get(i));
        	  if(OpenList.containsKey(i))
        	  {
        		  if(caves.get(i)._g <= caves.get(current_cave)._g)
        		  {
        			  continue;
        		  }
        	  }
        	  else if(ClosedList.containsKey(i))
        	  {
        		  if(caves.get(i)._g <= caves.get(current_cave)._g)
        		  {
        			  continue;
        		  }
        		  OpenList.put(i, ClosedList.get(i)) ;   
        		  ClosedList.remove(i);  
        	  }
        	  else
        	  {
        		  OpenList.put(i,caves.get(i));
        		  //fill in
        		  current_cave = i;
        	  }
        	  caves.get(i)._g  = current_cave_cost ;  
        	  
          }
          ClosedList.put(current_cave,caves.get(current_cave));
		}
		if(current_cave != end)
		{
			System.out.println("There is no way you reach that cave");
		}
	}
	static public int BestOption(HashMap<Integer,Cave> list)
	{
		double best = Integer.MAX_VALUE;
		int bestIndex = 0;
		Set<Integer> is = list.keySet();
		for(int i : is)
		{
			if(list.get(i)._f < best)
			{
				best = list.get(i)._f;
				bestIndex = i;
			}
		}
		return bestIndex;
	}
	static public Vector<Integer> getSuccesors(Vector<Cave> caves,int parent,boolean[][]connected)
	{
		Vector<Integer> succ = new Vector<Integer>();
		for (int i = 0;i<caves.size();i++)
		{
			if (connected[i][parent-1])
			{
			    succ.add(i+1);	
			    System.out.print((i+1)+" ");
			}	
		}
		System.out.println();
		return succ;
	}
}
