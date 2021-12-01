import { getAuth, onAuthStateChanged, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-auth.js";
import { app } from "./FirebaseConfig.js";

const auth = getAuth(app);

const emailInput = document.querySelector("#email");
const passwordInput = document.querySelector("#password");
const submitButton = document.querySelector("#submit");

submitButton.onclick = () => {
	signInWithEmailAndPassword(auth, emailInput.value, passwordInput.value)
	.then(userCredential => {
		const user = userCredential.user;
		
		const params = new URLSearchParams();
		params.append("idToken", user.accessToken);
		params.append("type", "SIGN_IN");
		return axios.post("./Auth", params);
	})
	.then(res => {
		if (res.data.status == "Success")
			location.href = "./Home";
	})
	.catch(error => {
		alert(error.message);	
	});
}