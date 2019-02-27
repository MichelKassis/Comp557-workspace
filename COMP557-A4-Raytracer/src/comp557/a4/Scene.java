package comp557.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
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

//  	static double left;
//  	static double bottom;
//  	static double rightMinusLeft;
//  	static double topMinusBottom; 	
//  	static int n; 

    
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

        //init ray to be used
        Ray ray = new Ray();
        Ray shadowRay = new Ray();

        render.init(w, h, showPanel);

        for ( int i = 0; i < h && !render.isDone(); i++ ) {
            for (int j = 0; j < w && !render.isDone(); j++) {

                Color3f c = new Color3f(0, 0, 0);
                double n = render.samples;

                for (double p = 0; p < n; p++) {
                    for (double q = 0; q < n; q++) {

                        double[] offset = {(p + 0.5) / n, (q + 0.5) / n};

                        generateRay(i, j, offset, cam, ray);

                        IntersectResult result = new IntersectResult();

                        for (Intersectable surface : surfaceList) { //check intersection
                            surface.intersect(ray, result);
                        }

                        if (result.t != Double.POSITIVE_INFINITY) {

                            float r = ambient.x * result.material.diffuse.x;
                            float g = ambient.y * result.material.diffuse.y;
                            float b = ambient.z * result.material.diffuse.z;

                            result.n.normalize();

                            for (Map.Entry<String, Light> entry : lights.entrySet()) {

                                IntersectResult shadowResult = new IntersectResult();
                                boolean inShadow = inShadow(result, entry.getValue(), shadowResult, shadowRay, surfaceList);

                                if (!inShadow) {
                                    Vector3d l = new Vector3d();
                                    l.sub(entry.getValue().from, result.p);
                                    l.normalize();

                                    Vector3d v = new Vector3d();
                                    v.sub(cam.from, result.p);
                                    v.normalize();

                                    Vector3d hlight = new Vector3d();
                                    hlight.add(l, v);
                                    hlight.normalize();

                                    float ndotl = (float) result.n.dot(l);
                                    float ndoth = (float) result.n.dot(hlight);

                                    float max1 = (0 > ndotl) ? 0 : ndotl;
                                    float max2 = (0 > ndoth) ? 0 : ndoth;
                                    max2 = (float) Math.pow(max2, result.material.shinyness);

                                    float Ix = (float) (entry.getValue().color.x * entry.getValue().power);
                                    float Iy = (float) (entry.getValue().color.y * entry.getValue().power);
                                    float Iz = (float) (entry.getValue().color.z * entry.getValue().power);

                                    r += result.material.diffuse.x * Ix * max1 + result.material.specular.x * Ix * max2;
                                    g += result.material.diffuse.y * Iy * max1 + result.material.specular.y * Iy * max2;
                                    b += result.material.diffuse.z * Iz * max1 + result.material.specular.z * Iz * max2;
                                }
                            }

                            c.add(new Color3f(r, g, b));
                        } else
                            c.add(render.bgcolor);
                    }
                }

                // Here is an example of how to calculate the pixel value.
                c.scale(1 / ((float) Math.pow(n, 2)));
                c.clamp(0, 1);


                int r = (int) (255 * c.x);
                int g = (int) (255 * c.y);
                int b = (int) (255 * c.z);
                int a = 255;
                int argb = (a << 24 | r << 16 | g << 8 | b);

                // update the render image
                render.setPixel(j, i, argb);
            }
        }

        // save the final render image
        render.save();
        
        // wait for render viewer to close
        render.waitDone();
        
    }
    
    
    /**
     * Generate a ray through pixel (i,j).
     * 
     * @param i The pixel row.
     * @param j The pixel column.
     * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
     * @param cam The camera.
     * @param ray Contains the generated ray.
     */
    public static void generateRay(final int i, final int j, final double[] offset, final Camera cam, Ray ray) {

        Vector3d w = new Vector3d();
        w.sub(cam.from, cam.to);
        w.normalize();

        Vector3d u = new Vector3d();
        u.cross(cam.up, w);
        u.normalize();

        Vector3d v = new Vector3d();
        v.cross(u, w);
        v.normalize();
        
        double d = 1; //default d to 1

        double topFrustum = Math.abs(Math.tan(Math.toRadians(0.5*cam.fovy)) * d);
        double bottomFrustum = -topFrustum;

        double ratio = cam.imageSize.getWidth() / cam.imageSize.getHeight();

        double rightFrustum = topFrustum * ratio;
        double leftFrustum = bottomFrustum * ratio;

        double imageWidthSize = cam.imageSize.getWidth();
        double imageHeightSize = cam.imageSize.getHeight();

        double U = leftFrustum + (rightFrustum-leftFrustum)*(j + offset[1])/imageWidthSize;
        double V = bottomFrustum + (topFrustum-bottomFrustum)*(i + offset[0])/imageHeightSize;


        //d should be 1	
        
        double dirX =  ((U*u.x) + (V*v.x) - (d*w.x));
        double dirY =  ((U*u.y) + (V*v.y) - (d*w.y));
        double dirZ =  ((U*u.z) + (V*v.z) - (d*w.z));
        
        Vector3d dir = new Vector3d(dirX,dirY,dirZ);
        
        dir.normalize();

        ray.set(cam.from, dir);
	}
	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * @param surfaceList
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	public static boolean inShadow(final IntersectResult result, final Light light, IntersectResult shadowResult, Ray shadowRay, final List<Intersectable> surfaceList) {
		
		// Objective 5: check for shdows and use it in your lighting computation
        Vector3d d = new Vector3d();
        d.sub(light.from, result.p);
        d.normalize();

        Point3d point = new Point3d(d);
        point.scaleAdd(1e-9, result.p);

        shadowRay.set(point, d);

        for (Intersectable surface : surfaceList) {
            surface.intersect(shadowRay, shadowResult);
            //check if blocked object is before light source
            if( shadowResult.t > 1e-9 && shadowResult.t != Double.POSITIVE_INFINITY) { 
                double lenValue = Math.sqrt(Math.pow(light.from.x - point.x, 2) + Math.pow(light.from.y - point.y, 2) + Math.pow(light.from.z - point.z, 2));
                double lenValue2 = Math.sqrt(Math.pow(shadowResult.p.x - point.x, 2) + Math.pow(shadowResult.p.y - point.y, 2) + Math.pow(shadowResult.p.z - point.z, 2));

                if(lenValue2 < lenValue)
                    return true;
            }
        }
		return false;
	}    
}
