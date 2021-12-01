import { getAuth, onAuthStateChanged, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-auth.js";
import { app } from "./FirebaseConfig.js";

const auth = getAuth(app);

const emailInput = document.querySelector("#email");
const passwordInput = document.querySelector("#password");
const submitButton = document.querySelector("#submit");

submitButton.onclick = () => {		
	const params = new URLSearchParams();
	params.append("type", "SIGN_UP");
	params.append("email", emailInput.value);
	params.append("password", passwordInput.value);
	
	axios.post("./Auth", params)
	.then(res => {
		if (res.data.status == "Success") {
			alert(res.data.status);
			
			const params = new URLSearchParams();
			params.append("email", res.data.email);
			params.append("password", res.data.password);			
			location.href = `./Auth?${params.toString()}`;
		}
	})
	.catch(error => {
		alert(error.message);	
	});
}