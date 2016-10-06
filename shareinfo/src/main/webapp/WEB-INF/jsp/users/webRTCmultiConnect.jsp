
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>
<style>
            .video-container {
                background: white;
                border: 2px solid black;
                border-radius: 0.2em;
                display: inline-block;
                margin: 2em .2em;
                padding: .1em;
                vertical-align: top;
            }

            .video-container h4 {
                border: 0;
                border-top: 1px solid black;
                display: block;
                margin: 0;
                text-align: center;
            }

            .eject {
                background-image: url('https://www.webrtc-experiment.com/images/eject.png') !important;
                background-position: center center !important;
                background-repeat: no-repeat !important;
                border-color: white;
                border-radius: 0;
                height: 30px;
                margin: .2em;
                position: absolute;
                width: 30px;
                z-index: 200;
            }
            
        </style>



<jsp:include page="../fragments/headTag.jsp" />

<sec:authentication var="loggedInUser" property="principal.firstName" />

<body style="padding-top: 5px">
	<spring:url value="/users/principals" htmlEscape="true" var="authUsersUrl" />

	<div class="container-fluid">
		<div class="row-fluid">
			<!-- <button id="text-user" class="inline btn-mini btn-success" onclick="openChatSession()">Open New Conference</button> -->
			<button id="setup-new-conference" class="btn-success" style="margin-top: 1%;">Open New Conference</button>
			<!-- list of all available broadcasting rooms -->
			<table style="width: 100%;" id="rooms-list"></table>
		</div>
		<div class="row-fluid">
			<div class="span8" style="height: 80%; overflow-y: auto;">

				<!-- local/remote videos container -->
				<div id="audios-container"></div>
				<div id="remote-videos-container"></div>



			</div>
			<div class="span4">
				<div id="local-video-container"></div>


<div class="panel panel-success">
						<div class="panel-heading">
							<h1 class="panel-title text-center">
								<c:out value="Chat"></c:out>
							</h1>
						</div>
						<div id="users-chat-container" class="panel-body" style="height:350px; overflow-y: auto; ">
						<p> Click on open new Conference button to Initiate a conference or click Join button to join the existing conference and allow the browser to access your system's camera and microphone. </p> 
						</div>
						<!-- <input type=text id="input-text-chat" style="width: inherit;" disabled> -->

<textarea  id="input-text-chat" cols="100" rows="3" style="width: 94%; margin: 1%"  title="Enter the text message" disabled></textarea>

						</div>
						




			</div>

		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>


<!-- scripts used for broadcasting -->
<!-- <script src="//cdn.webrtc-experiment.com/firebase.js"></script> -->
<!-- <script src="//cdn.webrtc-experiment.com/RTCMultiConnection.js"></script> -->
<script src="<spring:url value="/resources/js/RTCMultiConnection.js" />"></script>
<script src="<spring:url value="/resources/js/FileBufferReader.js" />"></script>
<script src="<spring:url value="/resources/js/firebase.js" />"></script>
<script src="<spring:url value="/resources/js/hark.js" />"></script>

<script>
	var authUser = "<c:out value='${loggedInUser}'/>";
	console.log("Auth user:" + authUser);
	var connection = new RTCMultiConnection('6e9e6fb19c80c720cc742039cb6ac686a6532f41e377e28d40679a459d33767e838311e47d61a402');
	connection.userid = authUser;
	connection.session = {
		audio : true,
		video : true,
		data : true
	};
	//connection.interval = 5000;
	// connection.maxParticipantsAllowed = 1;
	//connection.direction = 'one-to-one';

	var sessions = {};
	connection.onNewSession = function(session) {
		console.log("new session id.  ........." + session.sessionid);
		if (sessions[session.sessionid])
			return;
		sessions[session.sessionid] = session;
		var tr = document.createElement('tr');
		tr.innerHTML = '<td><strong>' + session.extra['session-name']
				+ '</strong> is running a conference !  <button class="join">Join</button></td>';
			
		roomsList.insertBefore(tr, roomsList.firstChild);
		var joinRoomButton = tr.querySelector('.join');
		joinRoomButton.setAttribute('data-sessionid', session.sessionid);
		joinRoomButton.onclick = function() {
			this.disabled = true;
			var sessionid = this.getAttribute('data-sessionid');
			session = sessions[sessionid];
			if (!session)
				throw 'No such session exists.';
			connection.join(session);
		};
	};
	var audioContainer = document.getElementById('audios-container') || document.body;
	var roomsList = document.getElementById('rooms-list');

	document.getElementById('setup-new-conference').onclick = function() {
	 	this.disabled = true;
		console.log("chat sesion triggered. . . . .");
		connection.extra = {
			'session-name' : authUser
		};
		//allow each user to create a new conference,
		//connection.sessionid = authUser
		connection.sessionid = (Math.random() * new Date().getTime()).toString(
				36).toUpperCase().replace(/\./g, '-')
		connection.open();
		//window.scrollTo(0, document.body.scrollHeight);

		document.getElementById('users-chat-container').innerHTML = ('<p> Waiting for the participants to join. Make sure to allow the browser to share your camera and microphone. </p>');
	};
	
/* 	function openChatSession() {
		console.log("chat sesion triggered. . . . .");
		this.disabled = true;
		connection.extra = {
			'session-name' : authUser
		};
		//allow each user to create a new conference,
		//connection.sessionid = authUser
		connection.sessionid = (Math.random() * new Date().getTime()).toString(
				36).toUpperCase().replace(/\./g, '-')
		connection.open();
		window.scrollTo(0, document.body.scrollHeight);

		document.getElementById('users-chat-container').innerHTML = ('Waiting for the participants to join. .  <br>');
	} */

	// setup signaling to search existing sessions
	connection.connect();
	connection.firebase = '69e23c0bcb69158f03e58d0ac0166a8362d6554ae05e8957730d6d194491f90fbc3d6e6b7de6caa2';
	connection.onopen = function(event) {
		console
				.log('Text chat has been opened between you and '
						+ event.userid);
		document.getElementById('users-chat-container').innerHTML = ('<h6 class="text-success">You are connected to  '+ event.userid + '</h6>========================');
		document.getElementById('input-text-chat').disabled = false;

	};

	connection.onmessage = function(event) {
		document.getElementById('users-chat-container').innerHTML += ('<p class="text-info">' + event.userid
				+ ' : ' + event.data + '</p>');
		var objDiv = document.getElementById("users-chat-container");
		objDiv.scrollTop = objDiv.scrollHeight;
		console.log('++++++++++++++++++++++++++++++++++++++++++++++++'
				+ event.userid);
	};
	document.getElementById('input-text-chat').onkeyup = function(e) {
		if (e.keyCode != 13)
			return; // if it is not Enter-key
		var value = this.value.replace(/^\s+|\s+$/g, '');
		if (!value.length)
			return; // if empty-spaces
		//  connection.channels['ravi'].send(value);
		connection.send(value);
		document.getElementById('users-chat-container').innerHTML += ('<p> You :   ' + value + '</p>');
		var objDiv = document.getElementById("users-chat-container");
		objDiv.scrollTop = objDiv.scrollHeight;
		this.value = '';
	};

	/* 		document.getElementByName('ravi').onkeyup = function(e) {
	 if(e.keyCode != 13) return; // if it is not Enter-key
	 var value = this.value.replace(/^\s+|\s+$/g, '');
	 if(!value.length) return; // if empty-spaces
	 connection.send( value );
	
	 connection.channels['ravi'].send(value);
	
	 this.value = '';
	 }; 
	 */
	/*******************************when closed , leave the session			
	 connection.onstatechange = function (state) {
	 if(state == 'connected-with-initiator') {
	 document.getElementById('leave-session').disabled = false;
	 }
	 };

	 document.getElementById('leave-session').onclick = function() {
	 connection.leave();
	 };
	 //connection.leaveOnPageUnload = true;
	 //connection.autoCloseEntireSession = true;
	 */
	//onleave method notify the initiator of that user
	connection.onspeaking = function(e) {
		// e.streamid, e.userid, e.stream, etc.
		e.mediaElement.style.border = '1px solid red';
	};
	connection.onsilence = function(e) {
		// e.streamid, e.userid, e.stream, etc.
		e.mediaElement.style.border = '';
	};
	
/* 	
	connection.onstream = function(e) {
		audioContainer.insertBefore(e.mediaElement, audioContainer.firstChild);
	};
	 */
	
	

	connection.onstream = function(stream) {
		if (stream.type === 'local') {
			var video = getVideo(stream, {
				username : authUser
			});
			document.getElementById('local-video-container').appendChild(video);
		}
		if (stream.type === 'remote') {
			var video = getVideo(stream, stream.extra);
			var remoteVideosContainer = document
					.getElementById('remote-videos-container');
			remoteVideosContainer.appendChild(video,
					remoteVideosContainer.firstChild);
			
		}
		
	};
	connection.onstreamended = function(e) {
		if (e.mediaElement && e.mediaElement.parentNode
				&& e.mediaElement.parentNode.parentNode) {
			e.mediaElement.parentNode.parentNode
					.removeChild(e.mediaElement.parentNode);
		}
	};
	
	

	function getVideo(stream, extra) {
		var div = document.createElement('div');
		div.className = 'video-container';
		div.id = stream.userid || 'self';
		if (stream.type === 'remote') {
			if (connection.isInitiator) {
				var eject = document.createElement('button');
				eject.className = 'eject';
				eject.title = 'Eject this User';
				eject.onclick = function() {
					// eject a specific user
					connection.eject(this.parentNode.id);
					this.parentNode.style.display = 'none';
				};
				div.appendChild(eject);
			}
			stream.mediaElement.width = document.getElementsByClassName("span8")[0].offsetWidth - 30;
		}else{
			stream.mediaElement.width = document.getElementsByClassName("span4")[0].offsetWidth - 10;
			//todo: Add button to insert audio on click.
		}
		div.appendChild(stream.mediaElement);
		if (extra) {
			console.log("remote user: " + extra.username);
			var h4 = document.createElement('h4');
			h4.innerHTML = stream.userid;
			div.appendChild(h4);
		}
		return div;
	}
</script>
</html>








