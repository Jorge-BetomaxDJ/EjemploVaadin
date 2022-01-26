window.calculaEdad = function (nacimiento){
	//Formato a recibir  2000-01-11
	console.log(nacimiento);
	const fechaActual = new Date();
	const anoActual = parseInt(fechaActual.getFullYear());
	const mesActual = parseInt(fechaActual.getMonth() + 1);
	const diaActual = parseInt(fechaActual.getDate());
	
	const anoNacimiento = parseInt(String(nacimiento).substring(0,4));
	const mesNacimiento = parseInt(String(nacimiento).substring(5,7));
	const diaNacimiento = parseInt(String(nacimiento).substring(8,10)); 
	
	let edad = anoActual - anoNacimiento;
	
	if (mesActual < mesNacimiento){
		edad--;
	}else if (mesActual=== mesNacimiento ){
		if (diaActual < diaNacimiento){
			edad--;
		}	
	}
	console.log(edad);
	return edad;
}