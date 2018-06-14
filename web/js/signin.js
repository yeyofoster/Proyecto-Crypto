$(document).ready(function(e) {
	$("#signIn").validetta({
                bubblePosition: 'bottom', // Bubble position // right / bottom
                bubbleGapLeft: 55, // Right gap of bubble (px unit)
                bubbleGapTop: -7, // Top gap of bubble (px unit)
		validators: {
                    regExp:{
                         passwords: {
                            pattern : /^((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/,
                            errorMessage : 'Passwords must contain at least 1 upper case letter, at least 1 lower case letter, at least 1 number or special character.'
                        },
                        // you can add more
                        example : {
                        pattern : /^[\+][0-9]+?$|^[0-9]+?$/,
                        errorMessage : 'Please enter number only !'
                        }
                    }
                },
                onValid:function(e){
			e.preventDefault();
			
                        
			$.ajax({
				method:"post",
				url:"Signin",
				cache:false,
				data:$("#signIn").serialize(),
				success: function(response){
                                        if(response=="1"){
                                            alert("The enter user is already taken. Please choose another username.");
                                        }
                                        else if(response=="0" || response=="2" ||response=="4" || response=="5" || response=="6" || response=="7") {
						console.log(response);
						alert("ERROR. The password or the user is wrong. Please try again.");
					}
                                        else{
                                            alert("Welcome "+response);
                                            $(location).attr("href","client.jsp");
                                        }
				}
			});
		}
	});	
});