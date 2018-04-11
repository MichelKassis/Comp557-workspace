package comp557.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Simple scene loader based on XML file format.
 */
public class Scene {
    
    /** List of surfaces in the scene */
    public List<Intersectable> surfaceList = new ArrayList<Intersectable>();
	
	/** All scene lights */
	public Map<String,Light> lights = new HashMap<String,Light>();

    /** Contains information about how to render the scene */
    public Render render;
    
    /** The ambient light colour */
    public Color3f ambient = new Color3f();
    
    
  //Basis vectors are the same for every ray so save them, don't need w, precompute -dw, use d = 1 to make things easier
  	static Vector3d uBase= new Vector3d();
  	static Vector3d vBase= new Vector3d();
  	static Vector3d minusDw= new Vector3d();

  	//Can also precompute left, bottom and right-left and top-bottom
  	static double left;
  	static double bottom;
  	static double rightMinusLeft;
  	static double topMinusBottom;

  	static int n; //n = sqrt(samples) since I sample in an nxn grid


    /** 
     * Default constructor.
     */
    public Scene() {
    	this.render = new Render();
    }
    
    /**
     * renders the scene
     */
    public void render(boolean showPanel) {
 
        Camera cam = render.camera; 
        int w = cam.imageSize.width;
        int h = cam.imageSize.height;
        
        render.init(w, h, showPanel);
    	//IntersectResult ir = new IntersectResult();
    	
		// set u,v,minudDw, left, bottom, rightminusLeft, topMinusBottom
		Vector3d W = new Vector3d();
		W.sub(cam.from , cam.to);
		W.normalize();
		uBase.cross(cam.up, W);
		uBase.normalize();
		vBase.cross(W, uBase);
		vBase.normalize();
		minusDw.scale(-1, W);

		bottom = Math.atan(Math.toRadians(cam.fovy/2));
		topMinusBottom = -2 * bottom;
		left = -(cam.imageSize.width * bottom )/ cam.imageSize.height ;
		rightMinusLeft = -2 * left;

		n = (int)Math.sqrt(render.samples);
		Point2d[] pairs = new Point2d[n*n];

		
        
        for ( int i = 0; i < h && !render.isDone(); i++ ) {
            for ( int j = 0; j < w && !render.isDone(); j++ ) {
            	
            	IntersectResult ir = new IntersectResult();
                // TODO: Objective 1: generate a ray (use the generateRay method)
            	Ray ray = new Ray();
            	double[] offset = new double[] {0};
            	
            	for(int p = 0; p < n; p++){
					for(int q = 0; q < n; q++){
						generateRay(i, j, p, q, cam, ray);
						IntersectResult result = new IntersectResult();
						for(Intersectable surface : surfaceList){
							surface.intersect(ray, result);
						}
						
					}
				}
            	
                // TODO: Objective 2: test for intersection with scene surfaces
            
//            	surfaceList.get(i).intersect(ray, ir);
//            	surfaceList.iterator();
            	
                // TODO: Objective 3: compute the shaded result for the intersection point (perhaps requiring shadow rays)
                
            	// Here is an example of how to calculate the pixel value.
            	Color3f c = new Color3f(render.bgcolor);
            	int r = (int)(255*c.x);
                int g = (int)(255*c.y);
                int b = (int)(255*c.z);
                int a = 255;
                int argb = (a<<24 | r<<16 | g<<8 | b);    
                
                // update the render image
                render.setPixel(j, i, argb);
            }
        }
        
        // save the final render image
        render.save();
        
        // wait for render viewer to close
        render.waitDone();
        
    }
   
    
    static double  u;
	static double v;

    /**
     * Generate a ray through pixel (i,j).
     * 
     * @param i The pixel row.
     * @param j The pixel column.
     * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
     * @param cam The camera.
     * @param ray Contains the generated ray.
     */
	public static void generateRay(final int i, final int j, final int p, final int q, final Camera cam, Ray ray) {
		// TODO: Objective 1: finish this method.
				u = left + rightMinusLeft*(j + (q + 0.5)/n)/cam.imageSize.width;
				v = bottom + topMinusBottom*(i + (p + 0.5)/n)/cam.imageSize.height;
				ray.eyePoint = cam.from;
				ray.viewDirection.set(minusDw);
				ray.viewDirection.scaleAdd(u, uBase, ray.viewDirection);
				ray.viewDirection.scaleAdd(v, vBase, ray.viewDirection);;
				ray.viewDirection.normalize();
		
		
		
		// TODO: Objective 1: generate rays given the provided parmeters
//		Point3d camPoint =  cam.from;
//		double offsetD = 8.0;
//		Point3d  hitPoint = new Point3d (i+camPoint.x,j+camPoint.y,-offsetD+camPoint.z);
//		Vector3d viewDirection = new Vector3d();
//		viewDirection = vectorFromPoints(hitPoint, camPoint);
//		
//		ray.set(camPoint,viewDirection);
	}
/**
 * Make a vector3d. (Point 1 - Point 2)
 * @param point1
 * @param point2
 * @return Vector3d for (Point 1 - Point 2)
 */
public static Vector3d vectorFromPoints(Point3d point1, Point3d point2) {
		Vector3d vector = new Vector3d(point1.x-point2.x,point1.y-point2.y,point1.z-point2.z );
		return vector;
	}
/**
 * Make a Point3d. (Point 1 - Point 2)
 * @param point1
 * @param point2
 * @return Point3d for (Point 1 - Point 2)
 */
public static Point3d pointFromPoints(Point3d point1, Point3d point2) {
		Point3d point = new Point3d(point1.x-point2.x,point1.y-point2.y,point1.z-point2.z );
		return point;
	}

	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param root The scene node.
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	public static boolean inShadow(final IntersectResult result, final Light light, final SceneNode root, IntersectResult shadowResult, Ray shadowRay) {
		
		// TODO: Objective 5: check for shdows and use it in your lighting computation
		
		return false;
	}    
}
