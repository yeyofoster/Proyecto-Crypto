
$(document).ready(function(e) {
 
	$("#uploadImage").validetta({
		bubblePosition: 'bottom', bubbleGapTop: 10, bubbleGapLeft: -5,
		onValid:function(e){
			e.preventDefault();
			
                        
			$.ajax({
				method:"post",
				url:"UploadImage",
				cache:false,
				data:$("#uploadImage").serialize(),
				success: function(response){				
					if(response=="0" || response=="2" || response=="3"){
						console.log(response);
						alert("ERROR. The something goes wrong with the upload.Please try again.");
					}else{
                        alert("The image was correctly uploaded.");
					}
				}
			});
		}
	});	
});