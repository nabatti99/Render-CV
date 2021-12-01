const signOutButtons = document.querySelectorAll("button.sign-out-btn");

signOutButtons.forEach(signOutButton => {
	signOutButton.onclick = () => {
		const params = new URLSearchParams();
		params.append("type", "SIGN_OUT");
		axios.post("./Auth", params)
		.then(res => {
			if (res.data.status == "Success")
				location.href = "./Auth";
		})
		.catch(error => {
			alert(error.message);	
		});
	}
});