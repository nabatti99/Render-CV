<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://rsms.me/inter/inter.css">
	<link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet">
	<script defer src="https://unpkg.com/axios/dist/axios.min.js"></script>
	<script defer type="module" src="./js/Register.js"></script>
	<title>Create an account</title>
</head>
<body class="relative bg-white">
	<div class="flex min-h-screen">
		<div class="flex flex-col items-center justify-between lg:w-1/3 md:w-1/2 w-full lg:px-24 px-8">
			<div class="flex w-full mt-8">
				<img class="h-8 w-auto sm:h-10" src="https://tailwindui.com/img/logos/workflow-mark-indigo-600.svg" alt="">
			</div>	
			<div class="flex flex-col justify-center w-full">
				<h1 class="font-bold text-2xl text-gray-700 mb-8">Welcome to <span class="text-indigo-500">Render CV</span></h1>

				<input id="email" class="font-medium text-gray-700 bg-gray-100 rounded-lg flex-grow py-2 px-4 mb-2" placeholder="Email" />
				<input id="password" class="font-medium text-gray-700 bg-gray-100 text-gray-900 rounded-lg flex-grow py-2 px-4 mb-4" type="password" placeholder="Password" />
				<button id="submit" class="whitespace-nowrap inline-flex items-center justify-center px-4 py-2 border border-transparent rounded-md shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 mb-4">Sign Up</button>
				<p class="text-center text-base font-medium text-gray-500">Already had an account - <a href="./Auth" class="text-indigo-600 hover:text-indigo-700">Sign In</a></p>
				</div>
			<p class="text-center text-base font-medium text-gray-500 mb-2">Hugo English Club © 2021</p>
		</div>
		<div class="bg-indigo-200 flex-grow md:block hidden">
			<div class="flex flex-col justify-end h-full lg:px-24 px-8">
				<img src="./assets/undraw_designer_life.svg" class="lg:mr-48 mt-12">
				<h1 class="text-base font-bold lg:text-6xl md:text-4xl text-indigo-800 mb-8 mt-16">Convert your CV</h1>
				<p class="text-base font-medium text-indigo-800 mb-24">from your long Sheet to a beautiful Document</p>
			</div>
		</div>
	</div>
</body>
</html>