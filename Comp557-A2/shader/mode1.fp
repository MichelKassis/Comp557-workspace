// mode 1 - ambient lighting
//author Michel Kassis 260662779


void main(void) {
	gl_FragColor = gl_FrontMaterial.ambient * gl_FrontLightProduct[0].ambient ;   
}