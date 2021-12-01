<%@page import="models.User"%>
<%@page import="controllers.AuthenticationController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://rsms.me/inter/inter.css">
	<link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" type="text/css" rel="stylesheet">
	<script defer src="https://unpkg.com/axios/dist/axios.min.js"></script>
	<script defer type="text/javascript" src="./js/Home.js"></script>
	<script defer src="./js/Authentication.js"></script>
	<title>Render CV</title>
</head>
<body class="relative bg-white">

	<%
		User user = null;
		try {
			String idToken = AuthenticationController.getIdToken(request.getCookies());
			user = new User(idToken);
			
		} catch (Exception e) {
			Cookie cookie = new Cookie("idToken", "");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			response.sendRedirect("./Auth");
			e.printStackTrace();
		}
	%>

	<div class="min-w-min max-w-7xl mx-auto px-4 sm:px-6">
	    <div class="flex justify-between items-center border-b-2 border-gray-100 py-6 md:justify-start md:space-x-10">
	      <div class="flex justify-start lg:w-0 lg:flex-1">
	        <a href="#">
	          <span class="sr-only">Workflow</span>
	          <img class="h-8 w-auto sm:h-10" src="https://tailwindui.com/img/logos/workflow-mark-indigo-600.svg" alt="">
	        </a>
	      </div>
	      <div class="-mr-2 -my-2 md:hidden">
	        <button type="button" class="bg-white rounded-md p-2 inline-flex items-center justify-center text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500" aria-expanded="false">
	          <span class="sr-only">Open menu</span>
	          <!-- Heroicon name: outline/menu -->
	          <svg class="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
	            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
	          </svg>
	        </button>
	      </div>
	      <nav class="hidden md:flex space-x-10">
	        <a href="./Home" class="text-base font-medium text-indigo-500 hover:text-indigo-900">
	          Home
	        </a>
	        <a href="./History" class="text-base font-medium text-gray-500 hover:text-gray-900">
	          History
	        </a>
	      </nav>
	      <div class="hidden md:flex items-center justify-end md:flex-1 lg:w-0">
	        <span class="whitespace-nowrap text-base font-medium text-gray-500">
	          <%= (user != null) ? user.email : null %>
	        </span>
	        <button class="ml-8 whitespace-nowrap inline-flex items-center justify-center px-4 py-2 border border-transparent rounded-md shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 sign-out-btn">
	          Sign out
	        </button>
	      </div>
	    </div>
	
		<form class="hidden">
			<input id="fileInput" type="file" name="fileInput" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" multiple />
		</form>
	
		<div id="dropArea" class="relative border-2 border-gray-300 border-dashed rounded-xl mt-24">
			<svg id="close" class="fill-current absolute top-0 right-0 w-4 m-4 cursor-pointer text-gray-500 hover:text-gray-900" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><title>xmark</title><g class="nc-icon-wrapper"><path d="M312.1 375c9.369 9.369 9.369 24.57 0 33.94s-24.57 9.369-33.94 0L160 289.9l-119 119c-9.369 9.369-24.57 9.369-33.94 0s-9.369-24.57 0-33.94L126.1 256 7.027 136.1c-9.369-9.369-9.369-24.57 0-33.94s24.57-9.369 33.94 0L160 222.1l119-119c9.369-9.369 24.57-9.369 33.94 0s9.369 24.57 0 33.94L193.9 256l118.2 119z"></path></g></svg>
			<div class="flex justify-center py-24">
				<div class="flex flex-col items-center w-3/4">
					<button class="bg-green-500 hover:bg-green-600 text-white rounded-md shadow-sm transition-colors outline-none text-base font-medium px-4 py-2">Choose .xlxs file</button>
					<p id="fileNames" class="text-base text-sm text-center text-gray-400 mt-2">Upload Microsoft Excel files</p>
					<p id="message" class="text-base font-medium text-center text-gray-400 mt-4">Or drag and drop here</p>
					<div id="percent" class="flex w-full rounded-full overflow-hidden mt-4">
						<span class="text-base text-xs leading-5 text-center font-medium text-white bg-indigo-500 h-full p-0.5 px-2 min-w-min">0%</span>
					</div>
				</div>
			</div>
		</div>
		
		<div class="mt-12">
			<h1 class="font-bold text-4xl">Introduction</h1>
			<p class="font-medium text-base text-gray-400 mt-8">This app will take the information about your candidates from your <span class="bg-green-500 text-white rounded py-1 px-2">.xlxs</span> file and convert it into great CV with <span class="bg-blue-500 text-white rounded py-1 px-2">.docx</span> extension.</p>
			<p class="font-medium text-base text-gray-400 mt-2">The app only process the <span class="bg-green-500 text-white rounded py-1 px-2">.xlxs</span> contain <span class="text-gray-900">4 Sheets</span> and <span class="text-gray-900">each Sheet having the same records</span>.</p>
			
			<p class="font-medium text-base text-gray-400 mt-8">The <span class="bg-green-500 text-white rounded py-1 px-2">.xlxs</span> files you have prepared should have the following sheets below. In this example, we prepared a <span class="bg-green-500 text-white rounded py-1 px-2">.xlxs</span> file with 3 Interviewees. 
			You can <a href="./Static/Template.xlsx" class="text-indigo-500 hover:text-indigo-700">download it here</a>.</p>
			
			<div class="mb-24">
				<h1 class="font-bold text-4xl mt-16">Sheet 1: Contain Interviewee	 Basic Information</h1>
				<p class="font-medium text-base text-gray-400 mt-8">Note: The <span class="text-red-500">Red fields are required</span>. The <span class="text-green-600">Green fields are the Optional field</span>, you can add more or remove them.</p>
				<table class="border-collapse border-2 border-green-800 w-full mt-4">
					<thead class="bg-green-500 text-white">
					    <tr>
					      <th class="border-2 border-red-600 bg-red-500">Name</th>
					      <th class="border-2 border-green-600">Date Of Birth</th>
					      <th class="border-2 border-green-600">Gender</th>
					      <th class="border-2 border-green-600">School</th>
					      <th class="border-2 border-green-600">Phone</th>
					      <th class="border-2 border-green-600">Facebook</th>
					      <th class="border-2 border-green-600">Email</th>
					    </tr>
					</thead>
					<tbody class="font-medium text-gray-500">
					    <tr>
					      <td class="border-2 border-red-600">Nguyễn Lê Anh Minh</td>
					      <td class="border-2 border-green-600">02/12/2000</td>
					      <td class="border-2 border-green-600">Male</td>
					      <td class="border-2 border-green-600">DUT</td>
					      <td class="border-2 border-green-600">09466721xx</td>
					      <td class="border-2 border-green-600">https://www.facebook.com/yourfacebookid</td>
					      <td class="border-2 border-green-600">minh@gmail.com</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-red-600">Trần Quốc Tuấn</td>
					      <td class="border-2 border-green-600">19/08/2000</td>
					      <td class="border-2 border-green-600">Male</td>
					      <td class="border-2 border-green-600">DUT</td>
					      <td class="border-2 border-green-600">09862492xx</td>
					      <td class="border-2 border-green-600">https://www.facebook.com/yourfacebookid</td>
					      <td class="border-2 border-green-600">tuan@gmail.com</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-red-600">Võ Đình Dũng</td>
					      <td class="border-2 border-green-600">xx/xx/2000</td>
					      <td class="border-2 border-green-600">Male</td>
					      <td class="border-2 border-green-600">DUT</td>
					      <td class="border-2 border-green-600">09xxxxxxxx</td>
					      <td class="border-2 border-green-600">https://www.facebook.com/yourfacebookid</td>
					      <td class="border-2 border-green-600">dung@gmail.com</td>
					    </tr>
					</tbody>
				</table>
				
				<h1 class="font-bold text-4xl mt-16">Sheet 2: English Skills</h1>
				<p class="font-medium text-base text-gray-400 mt-8">Note: The <span class="text-green-600">Green fields are the Optional field</span>, you can add more or remove them.</p>
				<table class="border-collapse border-2 border-green-800 w-full mt-4">
					<thead class="bg-green-500 text-white">
					    <tr>
					      <th class="border-2 border-green-600">Self-assessment of English Proficiency</th>
					      <th class="border-2 border-green-600">English Certificates</th>
					    </tr>
					</thead>
					<tbody class="font-medium text-gray-500">
					    <tr>
					      <td class="border-2 border-green-600">About 2/5</td>
					      <td class="border-2 border-green-600">No</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-green-600">About 4/5</td>
					      <td class="border-2 border-green-600">No</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-green-600">Below 3/5</td>
					      <td class="border-2 border-green-600">No</td>
					    </tr>
					</tbody>
				</table>
				
				<h1 class="font-bold text-4xl mt-16">Sheet 3: About The Interviewee</h1>
				<p class="font-medium text-base text-gray-400 mt-8">Note: The <span class="text-green-600">Green fields are the Optional field</span>, you can add more or remove them.</p>
				<table class="border-collapse border-2 border-green-800 w-full mt-4">
					<thead class="bg-green-500 text-white">
					    <tr>
					      <th class="border-2 border-green-600">Hobbies and Interest</th>
					      <th class="border-2 border-green-600">Talents and skills</th>
					      <th class="border-2 border-green-600">Extracurricular activities</th>
					      <th class="border-2 border-green-600">Experience in other Clubs or Young Union</th>
					    </tr>
					</thead>
					<tbody class="font-medium text-gray-500">
					    <tr>
					      <td class="border-2 border-green-600">I'm love watching movies, eating my favorite snacks. Because this bring more fun into my world.</td>
					      <td class="border-2 border-green-600">Snapping photos and editing them, coding.</td>
					      <td class="border-2 border-green-600">No</td>
					      <td class="border-2 border-green-600">It's Hugo English Club. The club is about connect the people that love english together.</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-green-600">I'm sociable, openheard, active and enthusiastic so my hobbies is playing sport such as football, swimming and athletics.</td>
					      <td class="border-2 border-green-600">I do not dare to admit that it is a talent because i am not confident and  brave enough to recommend to everyone.</td>
					      <td class="border-2 border-green-600">In 2018, a traditional camp festival in my school.</td>
					      <td class="border-2 border-green-600">My school's english club ,Da Nang students guitar club.</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-green-600">I'm a big fan of sports, especially football. I'm an IT-er so I love coding as well.</td>
					      <td class="border-2 border-green-600">I'm not excelled at singing but I love singing.</td>
					      <td class="border-2 border-green-600">Actually, I haven't joined any large extracurricular activity.</td>
					      <td class="border-2 border-green-600">No</td>
					    </tr>
					</tbody>
				</table>
				
				<h1 class="font-bold text-4xl mt-16">Sheet 4: Some Answered Questions</h1>
				<p class="font-medium text-base text-gray-400 mt-8">Note: The <span class="text-green-600">Green fields are the Optional field</span>, you can add more or remove them.</p>
				<table class="border-collapse border-2 border-green-800 w-full mt-4">
					<thead class="bg-green-500 text-white">
					    <tr>
					      <th class="border-2 border-green-600">1. Why do you want to join us?</th>
					      <th class="border-2 border-green-600">2. How did you find out Hugo?</th>
					      <th class="border-2 border-green-600">3. If you have a chance to become a member of Hugo, what can you contribute to us?</th>
					    </tr>
					</thead>
					<tbody class="font-medium text-gray-500">
					    <tr>
					      <td class="border-2 border-green-600">I want to learn more about English.</td>
					      <td class="border-2 border-green-600">Facebook, Friend referal.</td>
					      <td class="border-2 border-green-600">I can contribute abilities to team activities.</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-green-600">Because i want level up my english.</td>
					      <td class="border-2 border-green-600">Facebook.</td>
					      <td class="border-2 border-green-600">As much as I can :)))</td>
					    </tr>
					    <tr>
					      <td class="border-2 border-green-600">Improve english skill and get a girlfriend.</td>
					      <td class="border-2 border-green-600">Facebook.</td>
					      <td class="border-2 border-green-600">I’ll trying to do anything that could make your club better.</td>
					    </tr>
					</tbody>
				</table>
			</div>
			
			<h1 class="font-bold text-4xl">The Result</h1>
			<p class="font-medium text-base text-gray-400 mt-8">There are the result of the example above. You can <a href="./Static/Example_Result.docx" class="text-indigo-500 hover:text-indigo-700">download the Example_Result.docx file here</a>.</p>
		
			<div class="flex flex-col gap-1 my-8">
				<div class="">
					<img src="./Static/images/Template_RenderCV_Page1.jpg" />
				</div>
				<div class="">
					<img src="./Static/images/Template_RenderCV_Page2.jpg" />
				</div>
				<div class="">
					<img src="./Static/images/Template_RenderCV_Page3.jpg" />
				</div>
			</div>
		</div>
	</div>
	
	<!-- Mobile menu, show/hide based on mobile menu state. -->
	<div class="absolute top-0 inset-x-0 p-2 transition transform origin-top-right md:hidden">
	    <div class="rounded-lg shadow-lg ring-1 ring-black ring-opacity-5 bg-white divide-y-2 divide-gray-50">
	      <div class="pt-5 pb-6 px-5">
	        <div class="flex items-center justify-between">
	          <div>
	            <img class="h-8 w-auto" src="https://tailwindui.com/img/logos/workflow-mark-indigo-600.svg" alt="Workflow">
	          </div>
	          <div class="-mr-2">
	            <button type="button" class="bg-white rounded-md p-2 inline-flex items-center justify-center text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500">
	              <span class="sr-only">Close menu</span>
	              <!-- Heroicon name: outline/x -->
	              <svg class="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
	                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
	              </svg>
	            </button>
	          </div>
	        </div>
	        <div class="mt-6">
	          <nav class="grid gap-y-8">
	            <a href="./Home" class="-m-3 p-3 flex items-center rounded-md hover:bg-gray-50">
	              <!-- Heroicon name: outline/chart-bar -->
	              <svg class="flex-shrink-0 h-6 w-6 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
	                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
	              </svg>
	              <span class="ml-3 text-base font-medium text-gray-900">
	                Home
	              </span>
	            </a>
	
	            <a href="./History" class="-m-3 p-3 flex items-center rounded-md hover:bg-gray-50">
	              <!-- Heroicon name: outline/cursor-click -->
	              <svg class="flex-shrink-0 h-6 w-6 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
	                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 15l-2 5L9 9l11 4-5 2zm0 0l5 5M7.188 2.239l.777 2.897M5.136 7.965l-2.898-.777M13.95 4.05l-2.122 2.122m-5.657 5.656l-2.12 2.122" />
	              </svg>
	              <span class="ml-3 text-base font-medium text-gray-900">
	                History
	              </span>
	            </a>
	          </nav>
	        </div>
	      </div>
	      <div class="py-6 px-5 space-y-6">
	        <div>
	          <span class="w-full flex items-center whitespace-nowrap px-4 py-2 text-base font-medium text-white bg-indigo-600">
		          <%= (user != null) ? user.email : null %>
		        </span>
	          <p class="mt-6 text-center text-base font-medium text-gray-500">
	            <button class="text-indigo-600 hover:text-indigo-500 sign-out-btn">
	              Sign out
	            </>
	          </p>
	        </div>
	      </div>
	    </div>
	  </div>

</body>
</html>