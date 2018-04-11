package comp557lw.a3;

/**
 * Class implementing the Catmull-Clark subdivision scheme
 * 
 * @author TODO: Michel Kassis 260662779
 */
public class CatmullClark {

    /**
     * Subdivides the provided half edge data structure
     * @param heds
     * @return the subdivided mesh
     */
    public static HEDS subdivide( HEDS heds ) {
        HEDS heds2 = new HEDS();

        // TODO: Objectives 2,3,4: finish this method!!
        // you will certainly want to write lots of helper methods!
        for (int i= 0;i<heds.faces.size();i++) {
        	
        	Face face = heds.faces.get(i);
        	HalfEdge halfEdge = face.he;
        	
        	//compute vertex
        	face.child = new Vertex();
        	int verticesNumber = 0;
        	do{
        		sumOfVertex(halfEdge,face);        		
        		computeEvenVertex(halfEdge);
        		computeOddVertex(halfEdge);
         
        		
        		halfEdge = halfEdge.next;
        		verticesNumber++;
        	} while(halfEdge != face.he);
        	
        	//after knowing the number of vertices, we can compute the avg to know the center coordinates
        	centerOfVertex(halfEdge, face, verticesNumber);
        	face.child.n = halfEdge.head.n;
        	
        	// Center half edges
        	do{
        		HalfEdge halfEdgeCenter1 = new HalfEdge();
        		HalfEdge halfEdgeCenter2 = new HalfEdge();

        		centerOfHalfEdges(halfEdge,halfEdgeCenter1,halfEdgeCenter2,face);

        		Face new_face = new Face(halfEdgeCenter1);

        		heds2.faces.add(new_face);
   	
               	halfEdge = halfEdge.next;
               	verticesNumber++;

        	} while(halfEdge != face.he);	
        }
        
        for(int i = 0; i < heds.faces.size(); i++) {
        	Face face = heds.faces.get(i);
        	HalfEdge halfEdge = face.he;
        	
    		// HalfEdge twin connection
        	do{
        		halfEdgeTwinConnection(halfEdge);
        		
        		halfEdge = halfEdge.next;
        	} while(halfEdge != face.he);
        }
        
        return heds2;        
    }
   
    /*
     * HELPER METHODS
     */
    
    public static  void computeEvenVertex(HalfEdge halfEdge) {
    	if(halfEdge.head.child == null) {
    		halfEdge.head.child = halfEdge.head;	
    	}
    }
    
    public static void sumOfVertex(HalfEdge halfEdge, Face face) {
		face.child.p.x += halfEdge.head.p.x;
		face.child.p.y += halfEdge.head.p.y;
		face.child.p.z += halfEdge.head.p.z;	
    }
    
    public static void centerOfVertex(HalfEdge halfEdge,Face face,int verticesNumber) {
    	face.child.p.x /= verticesNumber;
		face.child.p.y /= verticesNumber;
		face.child.p.z /= verticesNumber;
    }
    
    public static void centerOfHalfEdges(HalfEdge halfEdge,HalfEdge halfEdgeCenter1,
    		HalfEdge halfEdgeCenter2, Face face) 
    {
    	halfEdge.child1.next = halfEdge.next.child2;
		halfEdge.next.child2.next =  halfEdgeCenter2;
		halfEdgeCenter2.next = halfEdgeCenter1;
		halfEdgeCenter1.next = halfEdge.child1;
		halfEdgeCenter1.head = face.child;
		halfEdgeCenter2.head = halfEdge.next.child1.head;

    }
    
    public static void computeOddVertex(HalfEdge halfEdge) {
    	
    	Vertex middle = new Vertex();
    	middle.p.x = (halfEdge.head.p.x + halfEdge.next.head.p.x) / 2;
    	middle.p.y = (halfEdge.head.p.y + halfEdge.next.head.p.y) / 2;
    	middle.p.z = (halfEdge.head.p.z + halfEdge.next.head.p.z) / 2;
    	
    	halfEdge.child1 = new HalfEdge();
    	halfEdge.child2 = new HalfEdge();
    	
    	halfEdge.child1.head = middle;
    	halfEdge.child2.head = halfEdge.head.child;
    	
    	halfEdge.child1.parent = halfEdge;
    	halfEdge.child2.parent = halfEdge;

    }
    public static void halfEdgeTwinConnection(HalfEdge halfEdge) {
    	halfEdge.child1.twin = halfEdge.twin.child2;
		halfEdge.twin.child2.twin = halfEdge.child1;
		
		halfEdge.child2.twin = halfEdge.twin.child1;
		halfEdge.twin.child1.twin = halfEdge.child2;
		
		HalfEdge halfEdgeCenter2 = halfEdge.next.child2.next;
		HalfEdge halfEdgeCenter1 = halfEdgeCenter2.next;
		
		HalfEdge halfEdgeCenter2_twin = halfEdge.next.next.child2.next.next;
		HalfEdge halfEdgeCenter1_twin = halfEdge.prev().next.child2.next;
		
		halfEdgeCenter1.twin = halfEdgeCenter1_twin;
		halfEdgeCenter1_twin.twin = halfEdgeCenter1;
		
		halfEdgeCenter2.twin = halfEdgeCenter2_twin;
		halfEdgeCenter2_twin.twin = halfEdgeCenter2;
    	
    }
    
}
