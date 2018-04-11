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
  
        // Objective 2: intersection of ray with sphere
    	
    	// ray: x = p + td
    	// circle: |x - c|^2 = r^2
    	Vector3d p_min_c = new Vector3d(ray.getEyePoint());
    	p_min_c.sub(center);
    	double d_dot_p_min_c = ray.getViewDirection().dot(p_min_c);
    	double disc = d_dot_p_min_c * d_dot_p_min_c - p_min_c.lengthSquared() + radius*radius;    	
    	// no intersection
    	if(disc < 0) return;
    	// calculate parameters, want the first intersection
    	double t = -d_dot_p_min_c - Math.sqrt(disc);
    	if(t < 0) return;
    	Point3d intersect = new Point3d();
    	ray.getPoint(t, intersect);
    	Vector3d normal = new Vector3d(intersect);
    	normal.sub(center);
    	normal.normalize();
    	result.t= t;
    	result.p.set(intersect);
    	result.n.set(normal);
    	result.material= this.material;
    	
    	
    	
	
//        // TODO: Objective 2: intersection of ray with sphere
//    	Vector3d originSubCenter = new Vector3d();
//    	originSubCenter.sub(ray.eyePoint, this.center);
//    	
//    	double underTheSquareRoot1 = Math.pow((2*(ray.viewDirection.dot(originSubCenter))), 2);
//    	double underTheSquareRoot2 = (-4*ray.viewDirection.lengthSquared())*(originSubCenter.lengthSquared()-Math.pow(this.radius,2));
//    	double underTheSquareRootTotal = underTheSquareRoot1 + underTheSquareRoot2;
//    	
//    	double numerator = (-2*(ray.viewDirection.dot(originSubCenter))) - Math.sqrt(underTheSquareRootTotal);
//    	
//    	double denominator = 2*(ray.viewDirection.lengthSquared());
//    	
//    	double d = (numerator/denominator);
//    	
//    	if(underTheSquareRootTotal >= 0 ) { 
//    	if(d < result.t) {
//    		result.t =d;
//    		
//    		/* The material of the intersection */
//    		result.material = this.material;
//    		
//    		/** Intersection position */
//    		ray.getPoint(result.t, result.p);
//    		
//    		
//    		result.n.sub(result.p, this.center);
//    		
//    		result.n.normalize();
//    	}
//    }
//    	
    }
    
}
