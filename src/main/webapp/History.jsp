<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="models.Document"%>
<%@page import="java.util.ArrayList"%>
<%@page import="models.User"%>
<%@page import="controllers.AuthenticationController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://rsms.me/inter/inter.css">
	<link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet">
	<script defer src="https://unpkg.com/axios/dist/axios.min.js"></script>
	<script defer src="./js/Authentication.js"></script>
	<title>History</title>
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

	<div class="max-w-7xl mx-auto px-4 sm:px-6">
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
	        <a href="./Home" class="text-base font-medium text-gray-500 hover:text-gray-900">
	          Home
	        </a>
	        <a href="./History" class="text-base font-medium text-indigo-500 hover:text-indigo-900">
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
	
		<div class="flex flex-col mt-12">
		  <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
		    <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
		      <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
		        <table class="min-w-full divide-y divide-gray-200">
		          <thead class="bg-gray-50">
		            <tr>
		              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
		                File name
		              </th>
		              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
		                Date Upload
		              </th>
		              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
		                Status
		              </th>
		              <th scope="col" class="relative px-6 py-3">
		                <span class="sr-only">Tools</span>
		              </th>
		            </tr>
		          </thead>
		          <tbody class="bg-white divide-y divide-gray-200">
		            
		            <% ArrayList<Document> documents = (ArrayList<Document>) session.getAttribute("documents"); %>
		            
		            <% if (documents.size() == 0) {%>
		            		<tr>
				              <td class="px-6 py-4 whitespace-nowrap" colspan="4">
				                <div class="flex items-center justify-center">
				                  <div>
				                    <div class="text-base font-medium text-gray-300">
				                      Oh, Nothing here!
				                    </div>
				                  </div>
				                </div>
				              </td>
				            </tr>
		            <% } %>
		            	
		            <% for (Document document : documents) { %>
		            		<tr>
				              <td class="px-6 py-4 whitespace-nowrap">
				                <div class="flex items-center">
				                  <div>
				                    <div class="text-sm font-medium text-gray-900">
				                      <%= document.name %>
				                    </div>
				                  </div>
				                </div>
				              </td>
				              <td class="px-6 py-4 whitespace-nowrap">
				              	<%
				              		String dateString = (new SimpleDateFormat("hh:mm:ss aa")).format(document.timestamp.toDate());
				              		String timeString = (new SimpleDateFormat("dd-MM-yyyy")).format(document.timestamp.toDate());
				              	%>
				                <div class="text-sm font-medium text-gray-900"><%= dateString %></div>
				                <div class="text-sm text-gray-500"><%= timeString %></div>
				              </td>
				              <td class="px-6 py-4 whitespace-nowrap">
				              	<%if (document.status.equals(Document.INITIATING_STATUS)) { %>
					              			<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-indigo-100 text-indigo-800">
							                  Initiating
							                </span>
												<%} else if (document.status.equals(Document.RENDERING_STATUS) || document.status.equals(Document.UPLOADING_STATUS)) { %>
				              				<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-indigo-100 text-indigo-800">
							                  Rendering
							                </span>
				              	<%} else if (document.status.equals(Document.DONE_STATUS)) { %>
															<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
							                  Active
							                </span>
												<%} else if (document.status.equals(Document.ERROR_STATUS)) { %>
															<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
							                  Error
							                </span>
				              	<%}%>
				              </td>
				              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-center">
				                <% if(!document.status.equals(Document.DONE_STATUS)) { %>
				                	<a class="text-indigo-100">Download</a>
				                <% } else {%>
				                	<a href="<%= document.downloadLink %>" class="text-indigo-600 hover:text-indigo-900">Download</a>
				                <% } %>
				              </td>
				            </tr>
		            <%}%>
		
		            <!-- More people... -->
		          </tbody>
		        </table>
		      </div>
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
	            </button>
	          </p>
	        </div>
	      </div>
	    </div>
	  </div>

</body>
</html>