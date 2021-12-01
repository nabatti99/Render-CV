import { initializeApp } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.5.0/firebase-analytics.js";
// https://firebase.google.com/docs/web/setup#available-libraries

const firebaseConfig = {
    apiKey: "AIzaSyAxMqpFZb7RRwrA9iTUtknHBwABDnaBYO4",
    authDomain: "convert-cv.firebaseapp.com",
    projectId: "convert-cv",
    storageBucket: "convert-cv.appspot.com",
    messagingSenderId: "178506663115",
    appId: "1:178506663115:web:dc2163321fedbbd706c74e",
    measurementId: "G-HMM4HT14N8"
};

// Initialize Firebase

export const app = initializeApp(firebaseConfig);
export const analytics = getAnalytics(app);