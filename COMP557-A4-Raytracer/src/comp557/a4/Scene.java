package comp557.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
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
		Color4f c = new Color4f();

        for ( int i = 0; i < h && !render.isDone(); i++ ) {
            for ( int j = 0; j < w && !render.isDone(); j++ ) {
            	
            	Ray ray = new Ray();

            	
                // TODO: Objective 1: generate a ray (use the generateRay method)
            	generateRay(i, j, 0, 0, cam, ray);
				IntersectResult result = new IntersectResult();
				for(Intersectable surface : surfaceList){
					surface.intersect(ray, result);
					
					if ( i == w/2 && j == h/2) {
						int whatever = 3463;
					}
					
					if(result.t   < Double.POSITIVE_INFINITY) {			            	
					c.set(1, 1, 1, 1);}

            	
                // TODO: Objective 2: test for intersection with scene surfaces
            	
                // TODO: Objective 3: compute the shaded result for the intersection point (perhaps requiring shadow rays)
                
            	// Here is an example of how to calculate the pixel value.
            	//Color3f c = new Color3f(render.bgcolor);
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
    }
    
//    static double  u;
//   	static double v;
//    
    
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
		
		// TODO: Objective 1: generate rays given the provided parmeters
		double u = left + rightMinusLeft*(j + (q + 0.5)/n)/cam.imageSize.width;
		double v = bottom + topMinusBottom*(i + (p + 0.5)/n)/cam.imageSize.height;
		ray.eyePoint = cam.from;
		ray.viewDirection.set(minusDw);
		ray.viewDirection.scaleAdd(u, uBase, ray.viewDirection);
		ray.viewDirection.scaleAdd(v, vBase, ray.viewDirection);;
		ray.viewDirection.normalize();
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
