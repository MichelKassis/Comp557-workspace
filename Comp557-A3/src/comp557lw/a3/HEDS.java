package comp557lw.a3;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Half edge data structure.
 * Maintains a list of faces (i.e., one half edge of each) to allow for easy display of geometry.
 * 
 * @author Michel Kassis 260662779
 */
public class HEDS {

    /**
     * List of faces 
     */
    List<Face> faces = new ArrayList<Face>();
    
    /**
     * Half Edge Hash Map
     */
    HashMap<String,HalfEdge> halfEdgeHashMap = new HashMap<String,HalfEdge>();
        
    /**
     * Constructs an empty mesh (used when building a mesh with subdivision)
     */
    public HEDS() {
        // do nothing
    }
        
    /**
     * Builds a half edge data structure from the polygon soup   
     * @param soup
     */
    public HEDS( PolygonSoup soup ) {
   // TODO: Objective 1: create the half edge data structure from a polygon soup
    	for(int i = 0; i < soup.faceList.size(); i++) {
            int[] vertexIndex = soup.faceList.get(i);
            
            HalfEdge previousHalfEdge = null;
            HalfEdge firstHalfEdge = null;
            HalfEdge halfEdge = new HalfEdge();
            
            for(int j = 0; j < vertexIndex.length; j++)
            {
            	halfEdge = new HalfEdge();
            	
            	//check if null
            	if(firstHalfEdge == null) 
            	{
            		firstHalfEdge = halfEdge;
            	}
            	
            	//to make it loop around
            	if(j == vertexIndex.length - 1)
            	{
            		halfEdge.next = firstHalfEdge;
            	}
            	
            	//assign next of previous to current
            	if(previousHalfEdge != null)
            	{
            		previousHalfEdge.next = halfEdge;
            	}
            	
            	halfEdge.head = soup.vertexList.get(vertexIndex[j]);
            	
            	halfEdgeHashMap.put(vertexIndex[j] + "," + vertexIndex[((j-1)+vertexIndex.length)%vertexIndex.length], halfEdge);
            	
            	int vertexIndex1 = vertexIndex[((j-1)+vertexIndex.length)%vertexIndex.length];
            	int vertexIndex2 = vertexIndex[j];
            	
                findTwin(halfEdge,vertexIndex1, vertexIndex2);
                
            	previousHalfEdge = halfEdge;
            }
            
            Face face = new Face(firstHalfEdge);

            faces.add(face);
        }
        
        
        
        
    } 
    
    public  void findTwin(HalfEdge halfEdge, int vertexIndex1, int vertexIndex2) {
        HalfEdge twin = halfEdgeHashMap.get(vertexIndex1 + "," + vertexIndex2);
           if(twin != null) {
               halfEdge.twin = twin;
               twin.twin = halfEdge;
           }
       }
    
    /**
     * Draws the half edge data structure by drawing each of its faces.
     * Per vertex normals are used to draw the smooth surface when available,
     * otherwise a face normal is computed. 
     * @param drawable
     */
    public void display() {
        // note that we do not assume triangular or quad faces, so this method is slow! :(     
        Point3d p;
        Vector3d n;        
        for ( Face face : faces ) {
            HalfEdge he = face.he;
            if ( he.head.n == null ) { // don't have per vertex normals? use the face
                glBegin( GL_POLYGON );
                n = he.leftFace.n;
                glNormal3d( n.x, n.y, n.z );
                HalfEdge e = he;
                do {
                    p = e.head.p;
                    glVertex3d( p.x, p.y, p.z );
                    e = e.next;
                } while ( e != he );
                glEnd();
            } else {
                glBegin( GL_POLYGON );                
                HalfEdge e = he;
                do {
                    p = e.head.p;
                    n = e.head.n;
                    glNormal3d( n.x, n.y, n.z );
                    glVertex3d( p.x, p.y, p.z );
                    e = e.next;
                } while ( e != he );
                glEnd();
            }
        }
    }
    
    /** 
     * Draws all child vertices to help with debugging and evaluation.
     * (this will draw each points multiple times)
     * @param drawable
     */
    public void drawChildVertices() {
    	glDisable( GL_LIGHTING );
        glPointSize(8);
        glBegin( GL_POINTS );
        for ( Face face : faces ) {
            if ( face.child != null ) {
                Point3d p = face.child.p;
                glColor3f(0,0,1);
                glVertex3d( p.x, p.y, p.z );
            }
            HalfEdge loop = face.he;
            do {
                if ( loop.head.child != null ) {
                    Point3d p = loop.head.child.p;
                    glColor3f(1,0,0);
                    glVertex3d( p.x, p.y, p.z );
                }
                if ( loop.child1 != null && loop.child1.head != null ) {
                    Point3d p = loop.child1.head.p;
                    glColor3f(0,1,0);
                    glVertex3d( p.x, p.y, p.z );
                }
                loop = loop.next;
            } while ( loop != face.he );
        }
        glEnd();
        glEnable( GL_LIGHTING );
    }
}
