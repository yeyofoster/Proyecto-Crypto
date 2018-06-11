$(document).ready(function(e) {
	
	$("#enviar").click(function(e) {
		var ffoto1 = $("#foto1").val();
		$.ajax({
			method:"post",
			url:"loadUpd_AX.php",
			data:{foto1:ffoto1},
			cache:false,
			success: function(respAX){
				alert(respAX);
			}
		})
    });
		
	$("#borrar").click(function(e) {
		
	    var ffoto1 = $("#foto1").val();
	    var ffoto2 = $("#foto2").val();
	    var ffoto3 = $("#foto3").val();
	    var ffoto4 = $("#foto4").val();
	    alert(ffoto1);
	});
});