package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple sphere class.
 */
public class Sphere extends Intersectable {
    
	/** Radius of the sphere. */
	public double radius = 1;
    
	/** Location of the sphere center. */
	public Point3d center = new Point3d( 0, 0, 0 );
    
    /**
     * Default constructor
     */
    public Sphere() {
    	super();
    }
    
    /**
     * Creates a sphere with the request radius and center. 
     * 
     * @param radius
     * @param center
     * @param material
     */
    public Sphere( double radius, Point3d center, Material material ) {
    	super();
    	this.radius = radius;
    	this.center = center;
    	this.material = material;
    }
    
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
    
    	
    	
    	Vector3d originSubCenter = new Vector3d();
    	originSubCenter.sub(ray.eyePoint, this.center);
    	
    	double underTheSquareRoot1 = Math.pow((2*(ray.viewDirection.dot(originSubCenter))), 2);
    	double underTheSquareRoot2 = (-4*ray.viewDirection.lengthSquared())*(originSubCenter.lengthSquared()-Math.pow(this.radius,2));
    	double underTheSquareRootTotal = underTheSquareRoot1 + underTheSquareRoot2;
    	
    	double numerator = (-2*(ray.viewDirection.dot(originSubCenter))) - Math.sqrt(underTheSquareRootTotal);
    	
    	double denominator = 2*(ray.viewDirection.lengthSquared());
    	
    	double d = (numerator/denominator);
    	
    	if(underTheSquareRootTotal >= 0 ) { 
    	if(d < result.t) {
    		result.t =d;
    		
    		/* The material of the intersection */
    		result.material = this.material;
    		
    		/** Intersection position */
    		ray.getPoint(result.t, result.p);
    		
    		
    		result.n.sub(result.p, this.center);
    		
    		result.n.normalize();
    	}
    }
    	
    	
    	
    	
    	
//        // TODO: Objective 2: finish this class
//    	Vector3d eMinusC = new Vector3d(ray.eyePoint);
//    	eMinusC.sub(center);
//    	double B = ray.viewDirection.dot(eMinusC);
//		double A = ray.viewDirection.dot(ray.viewDirection);
//		double C = eMinusC.dot(eMinusC) - radius*radius;
//    	double sqrroot = Math.sqrt(B*B - A*C);
//    	if(sqrroot >= 0){ //means intersection
//    		double t1 = (-B + sqrroot) / A;
//    		double t2 = (-B - sqrroot) / A;
//    		if(t1 >= 1e-9 && t2 >= 1e-9){//object entirely in front of camera, don't render object if camera is inside it
//    			if(t1 < t2 && t1 < result.t){
//    				result.t = t1;
//    				result.material = this.material;
//    				result.p.scaleAdd(result.t, ray.viewDirection, ray.eyePoint);
//    				result.n.sub(result.p, center);
//    				result.n.normalize();  
//    			}else if(t2 < result.t){
//    				result.t = t2;
//    				result.material = this.material;
//    				result.p.scaleAdd(result.t, ray.viewDirection, ray.eyePoint);
//    				result.n.sub(result.p, center);
//    				result.n.normalize();
//    			}
//    		}
//    	}
//	
    }    
}
