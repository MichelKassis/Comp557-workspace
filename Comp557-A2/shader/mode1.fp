// mode 1 - ambient lighting

void main(void) {
   vec4 acolour = (1,0,0,1); // TODO: Objective 1: set the ambient light colour

   gl_FragColor = gl_FrontMaterial.ambient * gl_FrontLightProduct[0].ambient ;   
}