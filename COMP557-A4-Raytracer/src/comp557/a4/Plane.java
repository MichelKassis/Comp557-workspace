package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Class for a plane at y=0.
 * 
 * This surface can have two materials.  If both are defined, a 1x1 tile checker 
 * board pattern should be generated on the plane using the two materials.
 */
public class Plane extends Intersectable {
    
	/** The second material, if non-null is used to produce a checker board pattern. */
	Material material2;
	
	/** The plane normal is the y direction */
	public static final Vector3d n = new Vector3d( 0, 1, 0 );
    
    /**
     * Default constructor
     */
    public Plane() {
    	super();
    }

        
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
    
        // Objective 4: intersection of ray with plane
    	Vector3d viewDir = ray.getViewDirection();
    	Point3d eyePoint = ray.getEyePoint();
    	
    	if(n.dot(viewDir) != 0) {
            Vector3d vector = new Vector3d(0,0,0);
            vector.sub(eyePoint);
            double t = (n.dot(vector)/n.dot(viewDir));
            
            
            //check which intersection is closer
            
            if (t > 1e-9 && t < result.t) { 
                result.t = t;

                
                double pointX = eyePoint.x + t*viewDir.x;
                double pointY = eyePoint.y + t*viewDir.y;
                double pointZ = eyePoint.z + t*viewDir.z;
                
                Point3d point = new Point3d(pointX,pointY,pointZ);
                result.p.set(point);

                result.n.set(n);

                if(material2 != null) {
                    int x = (int) Math.floor(point.x);
                    x = Math.abs(x);
                    int z = (int) Math.floor(point.z);
                    z = Math.abs(z);

                    if((x+z)%2 == 0)
                        result.material = this.material;
                    else
                        result.material = this.material2;
                }
                else
                    result.material = this.material;
            }
            
            
    	}
    
    }	

}