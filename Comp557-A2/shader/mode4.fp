// mode 4 - ambient and Lambertian and Specular with shadow map
//author Michel Kassis 260662779


uniform sampler2D shadowMap; 
uniform float sigma;

varying vec3 N;  // surface normal in camera 
varying vec3 v;  // surface fragment location in camera 
varying vec4 vL; // surface fragment location in light view NDC
 
void main(void) {

	vec3 L = normalize(gl_LightSource[0].position.xyz - v);   
   	vec3 E = normalize(-v);   
   	vec3 R = normalize(-reflect(L,N)); 


   	// TODO: Objective 6: ambient, Labertian, and Specular with shadow map.
	// Note that the shadow map lookup should only modulate the Lambertian and Specular component.
	float shadowFactor = clamp(textureProj(shadowMap, vL) + sigma, 0, 1); 
 
   	vec3 ambient = 	 gl_FrontLightProduct[0].ambient ;

	vec3 diffuse =   gl_FrontLightProduct[0].diffuse * max(dot(N,L), 0.0);

   	vec3 specular = gl_FrontLightProduct[0].specular 
                * pow(max(dot(R,E),0.0),0.3*gl_FrontMaterial.shininess);

    vL = vL/vL.w;
    vL = (vL+1)/2;

    if (vL.z > texture2D(shadowMap, vL.xy).z + sigma){
   	gl_FragColor =  new vec4(ambient,1);  
   	} 

   	else {
   		gl_FragColor = new vec4(ambient + diffuse + specular,1);
   	}
}