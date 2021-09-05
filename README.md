# WSASEP-Project-Prototype

Testing the possibility of having a web page with live 2.5 second updates from a local Davis Vantage Vue weather station for the future <b>W</b>eather <b>S</b>tation <b>A</b>pp <strong>S</strong>oftware <b>E</b>ngineering <b>P</b>ortfolio <b>P</b>roject (WSASEPP). Right now this is just a prototype, and it works really well. Future development will include a spectacular web page with graphs, a wind rose, and many more. There are also plans for an iOS and Andriod app as well. 
<p>Please visit the <a href="https://anthonygibbons.com/weather-live">Prototype App Website</a> for a more comprehensive exploration of the project.</p>
<h2>Software/System Components</h2>



This software consists of two components; 
<ul>
 <li>The Site Server</li>
 <li>The Web Application (WSASEPP-Host)</li>
</ul>
<br>
<br>
<p align="center"><img src="https://anthonygibbons.com/weather-live/includes/images/systemOverview.png" alt="System Overview" width="600"></p>
<h3>The Site Server</h3>
<p>This is a service (wsssd) developed in Java that runs on a Raspberry Pi at the site where the weather station base station is located. The service gets data from the base station through a json api and forwards this data to a web server.
<h3>The Web App</h3>
<p>This is a full stack web app, with it's back end developed in PHP interfacing with a MariaDB database, and a angular-js front-end that displays the weather observations data, updating every 2.5 seconds</p>



