
# README - Smurffing Around

This project is a mini-game build for OOP class at Ariel University.
Using a server that manage the game an contain level (0-23) the user can pick a level to play and should it be an automated game (using algorithm) or a manual.
After playing the user will also have the option to export the game to KML then show the game played on google earth, the graph will be shown on Ariel landscape and the game will run around the university and the city.


The server will then provide the game info that contain -
1. Graph - a directed graph (AKA DGraph) build from vertexes (AKA NodeV) and edges (AKA Edge) each.  
2. Robots - the robots (in this game Gargamle) will have speed path to walk and value that based on how many fruits they collected.
3. Fruits - the fruits (in this game the smurfs) will have type ( -1 -> papasmurf. 1 -> smurfette) value and location on the graph.


## Game concept
Grgamle the evil wizard have found the smurfs village and trying to catch as many smurfs as he can! 
Manual - the user will pick a level and then can pick the next vertex to move to, using Dijkstra algorithm to calculate the shortest path to the selected vertex the game will move Grgamle and once landed will let the user to pick again.
Auto - using an algorithm (details in WiKi) the game will move Grgamle to try and collected as many smurfs as it can.

## Game elements

### NodeV:
This class represents the set of operations applicable on a 
 node (vertex) in a (directional) weighted graph.
 
This class will creates the vertexes of the graph, each vertex will contain the following meta-data and a unique ID:

* my_id  -  the unique ID of the vertex.
* point  - will be an Point 3D object that will hold the coordinates.
* weight -  the weight of the vertex
* tag  - will be used to determine if  the graph is strongly connected / path from one vertex to the other.

### Edge:
This class represents the set of operations applicable on a 
directional edge(src,dest) in a (directional) weighted graph.

this class will creates the edges of the graph, each edge will contain the following meta-data: 

* node_data src  - the source vertex of the edge
* node_data dest - the destination vertex of the edge
* double w - the "cost" of taking the travel on the edge.


### Dgraph: 
This class represents a directional weighted graph.

The interface has a road-system or communication network in mind - and should support a large number of nodes (over 100,000).
The implementation should be based on an efficient compact representation.

##### Dgrap data structure:
 * HashMap vertex - will contain all the vertexes (node_data) in the graph.
 * HashMap edges - will contain all the edges (edge_data) in the graph.

### Algo graph: 

This class is static and will be used to implement complex algorithms when needed.


 * isConnected(); >>Using a BFS algorithm.
 * double shortestPathDist(int src, int dest,int pick); >>Djsktranode/Djsktraedge algorithm. 
 * List<Node> shortestPath(int src, int dest); >>Djsktra algorithm.
 *  List<Edge>shortestPathEdges(int src, int dest,DGraph g)>>Djsktraedge algorithm.
 * List<Node> shortestPath(int src, int dest,); >>Djsktra algorithm.
 * Object [] shortestPath_Dist(int src, int dest,DGraph g,Fruit fruit, int pick) >>Djsktranode/Djsktraedge algorithm. 
 *  public List<node_data> TSP(List<Integer> targets, DGraph g)>>Djsktraedge algorithm.
 * Edge EdgeForFruit(DGraph gg, Point3D fruit_pos, double type)


### Robot: 
This class represents a a robot object from the server. in the game Gargamle.

The server, depends on the level chosen, will let the game know how many robots are in the game and the game will initialize as many robots as needed.
Those robots will contain:

*   int id -  Id of the robot.
*	Point3D location - Robot x,y coordinates.
*	double 	Speed - Robot speed. 
*	int 	src - the robot current vertex .
*	int 	dest - the next vertex of the robot (-1 means robot is wait to instructions).
*	double path_len - the length of the path to the fruit. 
*	Fruit fruit - the fruit the robot goes to.
*	StringBuffer robotBuffer - String of the robot objects. 

##### Robot data structure:
*	MinHeap robot_Heap - will contain a list of the shortest paths from the robot to a fruit. (explantion on WiKi)
* Queue<Edge> path - will be the corrent path in which the robot is going.

### Fruit:
This class represents a a fruit object from the server. in the game type 1 = papasmurf  \ -1 = smurfette.

The server, depends on the level chosen, will let the game know how many fruits are in the current time and where they are.
If a robot/gargamle will pass over the fruit the server will know that it's been collected and will initilize a new one on the graph.

* edge_data fruitEdge - where the fruit at 
*	double value - value of the fruit
*	double type - fruit type (-1/1)
*	Point3D 	location - fruit 3D coordinates
*	String 	StartTime - time stamp of the fruit creations.
*	double id - fruit id (fruit id is the sum of the multiplication of 

### Fruit_Basket:
This class will represents a data structer, based on an ArrayList, of fruits in order to keep track on which fruit as been eaten.

* ArrayList <Fruit> fruits - Array list of fruits ( the container )  
*	StringBuffer fruitBuffer  - String buffer - will contain the fruits in a KML String. 

### Kmlmaker:
This class is a singleton class that will manage and build a KML file given to it strings that describe an obj as kml.

******how to use -> Wiki

example :

