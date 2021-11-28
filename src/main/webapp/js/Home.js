window.onload = () => {
	console.log("Page loadded");
	const fileInput = document.querySelector("#fileInput");
	const dropArea = document.querySelector("#dropArea");
	const button = dropArea.querySelector("#dropArea button");
	const closeButton = dropArea.querySelector("#dropArea #close");
	const message = dropArea.querySelector("#dropArea p#message");
	const fileNames = dropArea.querySelector("#dropArea p#fileNames");
	
	let files = new Array();
	
	const updateUIStep1 = () => {
		message.textContent = "Or drag and drop here";
		
		button.textContent = "Choose .xlxs file";
		button.classList.replace("bg-red-500", "bg-green-500");
		button.classList.replace("hover:bg-red-600", "hover:bg-green-600");
		button.onclick = () => fileInput.click();
		
		closeButton.classList.add("hidden");

		fileInput.value = "";
		fileInput.onchange = handleFileChoosen;
		
		fileNames.textContent = "Upload Microsoft Excel files";
		files = new Array();
	}
		
	const updateUIStep2 = () => {
		message.textContent = "Click Upload Button below to start working";
		
		closeButton.classList.remove("hidden");
		
		button.textContent = "Upload";
		button.classList.replace("bg-green-500", "bg-red-500");
		button.classList.replace("hover:bg-green-600", "hover:bg-red-600");
		button.onclick = handleUploadClicked;
		
		fileNames.textContent = files.map(file => file.name).join(", ");	
	}
		
	const handleFileChoosen = (event) => {
		console.log(event.target.files);
		files = Array.from(event.target.files);
		updateUIStep2();
	}
	
	const handleUploadClicked = () => {
		let percent = 0;
		
		const uploadPromises = new Array();
		files.forEach(file => {
			uploadPromises.push(
				uploadFile(file)
				.then(res => {
					console.log(res);
					percent += 100 / files.length;
					console.log(percent);
				})
				.catch(error => {
					console.log(error);
					alert(error.message);
				})
			);
		});
		
		Promise.all(uploadPromises)
		.then(responses => {
			alert("done");
			button.textContent = "Done";
			button.onclick = updateUIStep1;
		})
	}
	
	const uploadFile = (file) => {
		const formData = new FormData();
		formData.append("file", file);

		return axios.post("./Home", formData, {
			headers: { "Content-Type": "multipart/form-data" }
		});
	}
	
	closeButton.onclick = updateUIStep1;
	updateUIStep1();
}