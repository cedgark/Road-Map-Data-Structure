# Road-Map-Data-Structure
Implements a graph data structure to represent road network. It is based on the adjacency list structure. The class stores a list of vertices (represented using the Vertex class) and a list of edges (represented using the Edge class).

To load a map and print its information:

java RoadMap -i <MapFile>

To load a map file and find out the shortest path between two vertices:

java RoadMap -s <MapFile> <StartVertexIndex> <EndVertexIndex>

Here StartVertexIndex and EndVertexIndex are 0-based indices for the two vertices within the places array. This command calls isConnectedWithChargingStations(. . .) to check if such a
path exists. If it does, then the command continues to call shortestPathWithChargingStations(. . .) to find the shortest path with charging stations and print out the places along the path.
