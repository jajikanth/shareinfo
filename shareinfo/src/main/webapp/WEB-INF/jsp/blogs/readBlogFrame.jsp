<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<sec:authentication var="loggedInUserId" property="principal.id" />
<spring:url value="/users/${loggedInUserId}/blogs" htmlEscape="true" var="authUserBlogUrl" />
	<script type="text/javascript">

	$(document).ready(function() {
		$.fn.placeholder();
		
	jQuery( "#read-more-dialog" ).dialog({
         bgiframe:true,
         autoOpen: false,
         width: 1130,
		height: 500,
       //   modal: true, 
         resizable : true,
         
         
         width: 'auto', // overcomes width:'auto' and maxWidth bug
         height: 500,
         maxWidth: 1130,
         fluid: true, //new option
         
         
         
     });
	
	  $('textarea.max-length').maxlength({
	      threshold: 220,
	      placement: 'bottom-right'
	     
	  });
	});
	
	$(document).on("dialogopen", ".ui-dialog", function (event, ui) {
		getBlogFiles();
		getComments();
	    fluidDialog();
	});
	
	$(document).on("dialogclose", ".ui-dialog", function (event, ui) {
	    $(window).off("resize.responsive");
	});
	
	
	function fluidDialog() {
	    var $visible = $(".ui-dialog:visible");
	    // each open dialog
	    $visible.each(function () {
	        var $this = $(this);
	        var dialog = $this.find(".ui-dialog-content").data("dialog");
	        // if fluid option == true
	        if (dialog.options.maxWidth && dialog.options.width) {
	            // fix maxWidth bug
	            $this.css("max-width", dialog.options.maxWidth);
	            //reposition dialog
	            dialog.option("position", dialog.options.position);
	        }

	        if (dialog.options.fluid) {
	            // namespace window resize
	            $(window).on("resize.responsive", function () {
	                var wWidth = $(window).width();
	                // check window width against dialog width
	                if (wWidth < dialog.options.maxWidth + 50) {
	                    // keep dialog from filling entire screen
	                    $this.css("width", "90%");
	                    
	                }
	              //reposition dialog
	              dialog.option("position", dialog.options.position);
	            });
	        }

	    });
	}
	
var authUserBlogUrl = "<c:out value='${authUserBlogUrl}'/>";
var currentBlogId = -1;

function loadModal(blogId) {
	currentBlogId = blogId;
	$("#read-more-dialog").dialog("close");

	// Load Title	
	var blogTitle = $("#blog-"+blogId+"").find(".panel-heading").text();
	$( "#read-more-dialog" ).dialog( "option", "title", blogTitle);

	//Load Blog Content
	var blogContent = $("#blog-"+blogId+"").find(".panel-body").html();
	
	$("#modal-body-content").html(blogContent);
	//Load current Comment Count
	$("#comment-header span").html($("#comment-count-"+currentBlogId+"").html());
	
	$("#read-more-dialog").dialog("open");
	$("#modal-body-content").find("iframe").show();
}


function getBlogFiles(){
	//Empty the previous comments and load new comments 
	$("#files-container").html('<img src="/sdm-admin/resources/css/images/loading-icon.gif"/> <p>Loading files. . .</p> ');
	console.log(" user url: "+ authUserBlogUrl);
	$.ajax({
			url : authUserBlogUrl +"/loadBlogFiles/" + currentBlogId,
			type : "GET",
			cache : false,
			dataType : "json",
			async : false,
			success : function(blogFiles, status) {
				var fileResults = '<div> <ol id= "blog-files-'+currentBlogId+'"class= "unstyled">';
				if(blogFiles.length > 0){
					blogFiles.forEach(function(blogFile) {
						fileResults += '<li><a href="/shareinfo/download/blogFiles/'+ blogFile.id +'">'+ blogFile.name +'</a></li>';
						});
				}else{
					fileResults +=  '<li> No files </li>';
				}
				fileResults +=  '</ol> </div>';
				$("#files-container").html(fileResults);
				},
			error : function(e) {
				console.log("Error: " + e);
				bootbox.alert("Running Out of Session.");
			},
			complete : function() {
				$("#read-more-dialog").scrollTop(0);
			}
	});
}


function getComments(){
	//Empty the previous comments and load new comments 
	$("#modal-body-comment-content").html('<img src="/sdm-admin/resources/css/images/loading-icon.gif"/> <p>Loading comments. . .</p> ');
	console.log("comment user url: "+ authUserBlogUrl);
	$.ajax({
			url : authUserBlogUrl +"/loadBlogComments/" + currentBlogId,
			type : "GET",
			cache : false,
			dataType : "json",
			async : false,
			success : function(blogComments, status) {
				var commentResults = '<div> <ol id= "blog-comments-'+currentBlogId+'"class= "unstyled">';
	
							blogComments.forEach(function(blogComment) {
							commentResults += '<li style = "padding: 5px; border-bottom: 1px solid #ddd;"> <div class="media">';
							commentResults += '<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">';
							var image = '/shareinfo/resources/css/images/user-icon.gif';
							if(blogComment.user.imageSize != null){
								image = '/shareinfo/download/users/images/'+ blogComment.user.id ; 
								
							}
							commentResults += '<img style = "width: 30px;" src="'+image+'" alt="'+blogComment.user.firstName+'"> </div>';
							commentResults += '<div class="media-body">';
							commentResults += '<h5 class="media-heading"> '+blogComment.user.firstName+' ';
							commentResults += '<small class="inline-block ">'+ new Date(blogComment.creationTime).toLocaleString()+'</small></h5>';
							commentResults += '<span> '+blogComment.comment+'</span></div></div></li>';
						});
							commentResults +=  '</ol> </div>';
						
				console.log("Comments : " + blogComments);
				$("#modal-body-comment-content").html(commentResults);
				
			},
			error : function(e) {
				console.log("Error: " + e);
				bootbox.alert("Running Out of Session.");

			},
			complete : function() {
				console.log("Completed request");
				$("#read-more-dialog").scrollTop(0);

				countComments();
			}
	});
	
}
function postComment(){
	
	var userComment = $("#user-comment").val();
	console.log("user comment:" + userComment);
if(userComment.trim().length > 0){
	

	$("#modal-body-comment-content ol").append('<div id = "loading-container"> <img src="/sdm-admin/resources/css/images/loading-icon.gif"/> <p>Writing your Comment. . .</p> </div>');
	var authUserBlogUrl = "<c:out value='${authUserBlogUrl}'/>";
	$.ajax({
						url : authUserBlogUrl +"/newBlogComment/" + currentBlogId,
						type : "POST",
						cache : false,
						data : userComment ,
						contentType : "application/text",
						dataType : "json",
						async : false,
						success : function(blogComment, status) {
							console.log("Posted Blog Comment : " + blogComment);
							var commentResult = '<li style = "padding: 10px; border-bottom: 1px solid #ddd;"> <div class="media">';
							commentResult += '<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">';
							var image = '/shareinfo/resources/css/images/user-icon.gif';
							if(blogComment.user.imageSize != null){
								image = '/shareinfo/download/users/images/'+ blogComment.user.id ; 
								
							}
							commentResult += '<img style = "width: 30px;" src="'+image+'" alt="'+blogComment.user.firstName +'"> </div>';
							commentResult += '<div class="media-body">';
							commentResult += '<h5 class="media-heading"> '+blogComment.user.firstName+' ';
							commentResult += '<small class="inline-block ">'+ new Date(blogComment.creationTime).toLocaleString()+'</small></h5>';
							commentResult += '<span> '+blogComment.comment+'</span></div></div></li>';
							$("#loading-container").remove();
							$("#modal-body-comment-content ol").append(commentResult);
							countComments();
							$("#user-comment").val("");
						},
						error : function(e) {
							console.log("Error: " + e);
							alert("Invalid Data or Session Expired.");

						},
						complete : function() {
							//console.log("Completed request for write comment.");
							
						}
	});
	
}else{
	$("#messages").addClass("text-error").text(" Please enter some text to write your comments.").show().fadeOut(8000);
}
	
}
function countComments(){
	var count = $("#blog-comments-"+currentBlogId+" li").length;
	console.log("total comments: " +  count);
	$("#comment-header span").html(count);
	$("#comment-count-"+currentBlogId+"").html(count);
	
	}

</script>
	<div id="read-more-dialog">
		<div id="read-more-modal">
			<div id="modal-body-content"></div>
									
							<div class="control-group " >
							<h4 class="control-label text-info">Files : </h4>
							<div class="controls" id="files-container" >
								</div>
								</div>
			<div id="comment-header" class="page-header">
				<h2 class="text-info">
					Comments. . . <small><span class="badge badge-info">0</span></small>
				</h2>
			</div>

			<div id="modal-body-comment-content" class="span9 offset1"></div>

			<div id="user-blog-comment-container" class="span9 offset1" style="padding: 5px; border-bottom: 1px solid #ddd;">
				<div class="media">
					<div class=" pull-left media-object" style="width: 30px; height: 50px; overflow-y: hidden;">
<spring:url value="/download/users/images/${user.id}" htmlEscape="true" var="imageUrl" />
						<img width=30px src="${!empty user.imageSize ?  imageUrl : '/shareinfo/resources/css/images/user-icon.gif'}"
							alt="<sec:authentication property='principal.firstName' />">
					</div>
					<div class="media-body">
						<h5 class="media-heading">
							<sec:authentication property="principal.firstName" />
						</h5>

						<textarea class="input-xxlarge max-length" placeholder="Write your comments. . ." id="user-comment" maxlength="256" ></textarea>
						<span id="messages" class=""></span>
						<button class="btn inline" onclick="$('#read-more-dialog').dialog('close')" style="height: 50px">Close</button>
						<button class="btn inline" onclick="postComment()" style="height: 50px">Write</button>
					</div>
				</div>

			</div>


		</div>

	</div>


