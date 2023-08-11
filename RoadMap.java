//c21012668
/* Put your student number here
 *
 * Optionally, if you have any comments regarding your submission, put them here.
 * For instance, specify here if your program does not generate the proper output or does not do it in the correct manner.
 */

import java.util.*;
import java.io.*;

class Vertex {

	// Constructor: set name, chargingStation and index according to given values,
	// initilaize incidentRoads as empty array
	public Vertex(String placeName, boolean chargingStationAvailable, int idx) {
		name = placeName; //name of place
		incidentRoads = new ArrayList<Edge>(); //Roads(edges) that connect this place with another place
		index = idx;
		chargingStation = chargingStationAvailable; //charging station?
	}

	public String getName() {
		return name;
	}

	public boolean hasChargingStation() {
		return chargingStation;
	}

	public ArrayList<Edge> getIncidentRoads() {
		return incidentRoads;
	}

	// Add a road to the array incidentRoads
	public void addIncidentRoad(Edge road) {
		incidentRoads.add(road);
	}

	public int getIndex() {
		return index;
	}

	private String name; // Name of the place
	private ArrayList<Edge> incidentRoads; // Incident edges
	private boolean chargingStation; // Availability of charging station
	private int index; // Index of this vertex in the vertex array of the map
}

class Edge {
	public Edge(int roadLength, Vertex firstPlace, Vertex secondPlace) {
		length = roadLength; //length of road
		incidentPlaces = new Vertex[] { firstPlace, secondPlace }; //array with two elements of two vertices connected by this edge
	}

	public Vertex getFirstVertex() {
		return incidentPlaces[0];
	}

	public Vertex getSecondVertex() {
		return incidentPlaces[1];
	}

	public int getLength() {
		return length;
	}

	private int length;
	private Vertex[] incidentPlaces;
}

// A class that represents a sparse matrix
public class RoadMap {

	// Default constructor
	public RoadMap() {
		places = new ArrayList<Vertex>(); //ArrayList of places in graph
		roads = new ArrayList<Edge>(); //ArrayList of roads (edges) in graph
		//🤡
	}

	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a map and print information: java RoadMap -i <MapFile>");
		System.err.println(
				" - Read a map and find shortest path between two vertices with charging stations: java RoadMap -s <MapFile> <StartVertexIndex> <EndVertexIndex>");
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 2 && args[0].equals("-i")) {
			RoadMap map = new RoadMap();
			try {
				map.loadMap(args[1]);
			} catch (Exception e) {
				System.err.println("Error in reading map file");
				System.exit(-1);
			}

			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();
		} else if (args.length == 4 && args[0].equals("-s")) {
			RoadMap map = new RoadMap();
			map.loadMap(args[1]);
			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();

			int startVertexIdx = -1, endVertexIdx = -1;
			try {
				startVertexIdx = Integer.parseInt(args[2]);
				endVertexIdx = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.err.println("Error: start vertex and end vertex must be specified using their indices");
				System.exit(-1);
			}

			if (startVertexIdx < 0 || startVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for start vertex");
				System.exit(-1);
			}

			if (endVertexIdx < 0 || endVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for end vertex");
				System.exit(-1);
			}

			Vertex startVertex = map.getPlace(startVertexIdx);
			Vertex endVertex = map.getPlace(endVertexIdx);
			if (!map.isConnectedWithChargingStations(startVertex, endVertex)) {
				System.out.println();
				System.out.println("There is no path connecting " + map.getPlace(startVertexIdx).getName() + " and "
						+ map.getPlace(endVertexIdx).getName() + " with charging stations");
			} else {
				ArrayList<Vertex> path = map.shortestPathWithChargingStations(startVertex, endVertex);
				System.out.println();
				System.out.println("Shortest path with charging stations between " + startVertex.getName() + " and "
						+ endVertex.getName() + ":");
				map.printPath(path);
			}

		} else {
			printCommandError();
			System.exit(-1);
		}
	}

	// Load matrix entries from a text file
	public void loadMap(String filename) {
		File file = new File(filename);
		places.clear();
		roads.clear();

		try {
			Scanner sc = new Scanner(file);

			// Read the first line: number of vertices and number of edges
			int numVertices = sc.nextInt();
			int numEdges = sc.nextInt();

			for (int i = 0; i < numVertices; ++i) {
				// Read the vertex name and its charing station flag
				String placeName = sc.next();
				int charginStationFlag = sc.nextInt();
				boolean hasChargingStataion = (charginStationFlag == 1);

				// Add your code here to create a new vertex using the information above and add
				// it to places
				Vertex vtx = new Vertex(placeName, hasChargingStataion, i);
				places.add(vtx); //🤡
			}

			for (int j = 0; j < numEdges; ++j) {
				// Read the edge length and the indices for its two vertices
				int vtxIndex1 = sc.nextInt();
				int vtxIndex2 = sc.nextInt();
				int length = sc.nextInt();
				Vertex vtx1 = places.get(vtxIndex1);
				Vertex vtx2 = places.get(vtxIndex2);

				// Add your code here to create a new edge using the information above and add
				// it to roads
				// You should also set up incidentRoads for each vertex

					Edge crr_edge = new Edge(length, vtx1, vtx2); //🤡
					roads.add(crr_edge);
					vtx1.addIncidentRoad(crr_edge);
					vtx2.addIncidentRoad(crr_edge);


			}

			sc.close();

			// Add your code here if approparite
		} catch (Exception e) {
			e.printStackTrace();
			places.clear();
			roads.clear();
		}
	}


	// Return the shortest path between two given vertex, with charging stations on
	// each itermediate vertex.
	public ArrayList<Vertex> shortestPathWithChargingStations(Vertex startVertex, Vertex endVertex) {

		// Initialize an empty path
		ArrayList<Vertex> path = new ArrayList<Vertex>();

		// Sanity check for the case where the start vertex and the end vertex are the
		// same
		if (startVertex.getIndex() == endVertex.getIndex()) {
			path.add(startVertex);
			return path;
		}

		ArrayList<Vertex> Q = new ArrayList<Vertex>();
		int[] D = new int[places.size()];
		Vertex[] prev = new Vertex[places.size()];
	 for (Vertex v: places)  {
	 	D[v.getIndex()] = 2147483647;
		prev[v.getIndex()] = null;
	    Q.add(v);
}
 D[startVertex.getIndex()] = 0;

 while (!(Q.isEmpty())) {
 Vertex u = new Vertex("zazazb",false,0); //Test vertex
   u = Q.get(indexOfSmallest(D));
	 Q.remove(u);

	 for (Edge edge : u.getIncidentRoads()) {

		 Vertex vtx_v = edge.getFirstVertex();
			Vertex vtx_w = edge.getSecondVertex();

			Vertex post_v = new Vertex("zazazb",false,0); //Test vertex
			if(vtx_v == startVertex) {
				post_v = vtx_w;
			} else {
				post_v = vtx_v;
			}

			if (Q.contains(post_v)) {

			int alt = D[u.getIndex()] + edge.getLength();
				if(alt < D[post_v.getIndex()]){
					D[post_v.getIndex()] = alt;
					prev[post_v.getIndex()] = u;
				}
			}


	 }
 }


		// Add your code here
		//Create Two sets of Vertices //🤡
	//ArrayList<Vertex> G = new ArrayList<String>(places);
//	ArrayList<Vertex> C = new ArrayList<Vertex>();
	//	Set C for vertices whoose distances have been determind
	//	Set G for all other Vertices
	//	Initially all vertices are in G
	// Store a value in array D for each vertex to track its shortest distance from source
 //	int[] D = new int[places.size()];
	// Initially D = 0 for source vertex and D= ♾ for all other vertices
//	Arrays.fill(D,2147483647);
//	D[startVertex.getIndex()] = 0;

//	System.out.println(D[3]);

	//	ArrayList<Vertex> Q = new ArrayList<Vertex>(G);
	// In each iteration, pull the vertex u with smallest D value from G into C, update the D values in each neighbour v in G via
//	c.add(smallest D value from G G.get())
	// D[v] = min(D[v],D[u] +w(u,v))
ArrayList<Vertex> P = new ArrayList<Vertex>();
P.add(endVertex);
while(!(P.get(P.size()-1).getIndex() == startVertex.getIndex())) {
P.add(prev[P.get(P.size()-1).getIndex()]);
}
  Collections.reverse(P);
	return P; //Path🤡
}

public static int indexOfSmallest(int[] array){ //🤡

	 // add this
	 if (array.length == 0)
			 return -1;

	 int index = 0;
	 int min = array[index];

	 for (int i = 1; i < array.length; i++){
			 if (array[i] < min){
			 min = array[i];
			 index = i;
			 }
	 }
	 return index;
} //🤡


	// Check if two vertices are connected by a path with charging stations on each itermediate vertex.
	// Return true if such a path exists; return false otherwise.
	// The worst-case time complexity of your algorithm should be no worse than O(v + e),
	// where v and e are the number of vertices and the number of edges in the graph.

	public boolean isConnectedWithChargingStations(Vertex startVertex, Vertex endVertex) {
		// Sanity check
		if (startVertex.getIndex() == endVertex.getIndex()) {
			return true;
		}

		boolean[] discovered = new boolean[places.size()];


		return DFS(places, startVertex, endVertex, discovered); //🤡

	/*	int start = startVertex.getIndex();
		int end = endVertex.getIndex();

		// Add your code here
		boolean[] discovered = new boolean[places.size()];
		Arrays.fill(discovered,false); //creates array to store visits, and fills them as false

		ArrayList <ArrayList<Integer> > neighbors = new ArrayList< ArrayList<Integer> >();
		for(int i = 0; i < places.size(); ++i)
			neighbors.add(new Arraylist<Integer>());

		for(int i = 0; i < edges.lengtn; ++ i) {
		ArrayList<Integer> neighborList;
		neighbors.get(edges[i][0]).add(edges[i][1]);
		neighbors.get(edges[i][1]).add(edges[i][0]); */
}

// return validPath(neighbors, visit, start, end);



/*	public static boolean validPath(ArrayList <ArrayList<Integer> > neighbors, boolean[] visit, int start, int end) //🤡 Call helper function to check for a path between start and end vertex
	{
		visit[start] = true;

		for(int i=0; i < neighbors.get(start).size(); ++i) {
			int next = neighbors.get(start).get(i);

			if(next == end)
			return true;

			if(!visit[next] && validPath(neighbors,visit, next, end))
			return true;
		}
		return false;
	} */

	public static boolean DFS(ArrayList<Vertex> graph, Vertex startVertex, Vertex endVertex, boolean[] discovered) //🤡 Call helper function to check for a path between start and end vertex
			{
					int v = startVertex.getIndex();

					// mark the current node as discovered
					discovered[v] = true;

					// print the current node
					//System.out.print(v + " ");

					// do for every edge (v, u)
//System.out.println("Visiting...");
//System.out.println(startVertex.getName());
					for (Edge edge : startVertex.getIncidentRoads()) { // Go through start's neighbours in the adjacency list

						Vertex vtx_v = edge.getFirstVertex();
						Vertex vtx_w = edge.getSecondVertex();

						Vertex post_v = new Vertex("zazazb",false,0); //Test vertex
						if(vtx_v == startVertex) {
							post_v = vtx_w;
						} else {
							post_v = vtx_v;
						}

					//	System.out.println(post_v.getName()); //🤡
											//	System.out.println(discovered[post_v.getIndex()]); //🤡
						if (post_v ==  endVertex) { //if neighbour is end vertex, path exists and returns true
						//	post_v.getName(); //🤡
							return true;
						} else if (!discovered[post_v.getIndex()] && post_v.hasChargingStation() && DFS(graph, post_v,endVertex,discovered)) { //Vertex not marked/visited, has charging station, and recusrive is true. Recusrsively call the helper function to check if a path exists between the neighbour and the end vertex. If so, then a path is found and return true
							return true;
						}

					}
					//System.out.println("CODE LOGICCC");
					return false; //no path found, return false

	} //..🤡




	public void printMap() {
		System.out.println("The map contains " + this.numPlaces() + " places and " + this.numRoads() + " roads");
		System.out.println();

		System.out.println("Places:");

		for (Vertex v : places) {
			System.out.println("- name: " + v.getName() + ", charging station: " + v.hasChargingStation());
		}

		System.out.println();
		System.out.println("Roads:");

		for (Edge e : roads) {
			System.out.println("- (" + e.getFirstVertex().getName() + ", " + e.getSecondVertex().getName()
					+ "), length: " + e.getLength());
		}
	}

	public void printPath(ArrayList<Vertex> path) {
		System.out.print("(  ");

		for (Vertex v : path) {
			System.out.print(v.getName() + "  ");
		}

		System.out.println(")");
	}

	public int numPlaces() {
		return places.size();
	}

	public int numRoads() {
		return roads.size();
	}

	public Vertex getPlace(int idx) {
		return places.get(idx);
	}

	private ArrayList<Vertex> places;
	private ArrayList<Edge> roads;
}
