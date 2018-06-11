function myFunction() {
    location.reload();
    
}
function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
    }
      var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
}

$(document).ready(function(e) {
 
	$("#formAcc").validetta({
		bubblePosition: 'bottom', bubbleGapTop: 10, bubbleGapLeft: -5,
		onValid:function(e){
			e.preventDefault();
			
                        
			$.ajax({
				method:"post",
				url:"Login",
				cache:false,
				data:$("#formAcc").serialize(),
				success: function(response){				
					if(response=="0" || response=="2" || response=="3"){
						console.log(response);
						alert("ERROR. The password or the user is wrong. Please try again.");
					}else{
						alert("Welcome "+response);
						if(response=="artist")
                            $(location).attr("href","artist.jsp");
                        else
                        	$(location).attr("href","client.jsp");
					}
				}
			});
		}
	});	
});