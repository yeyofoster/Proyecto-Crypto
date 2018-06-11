$(document).ready(function(e) {
	$(".modal").modal();
	$("#modalFormUpd #sexo").material_select();
	$("#nvoEst").on("click",function(){
		$("#modalFormIns #sexo").material_select();
		$("#modalFormIns").modal("open");
	});
		
    $(".eliminar").click(function(e) {
        var idDel = $(this).data("eliminar");
    
		$.confirm({
			title:"SEE",
			content:"Desea eliminar el registro del id: "+idDel,
			type:"blue",
			useBootstrap:false,
			boxWidth:"50%",
			buttons:{
				confirm:function(){
					$.ajax({
						method:"post",
						url:"delete_AX.php",
						cache:false,
						data:{id:idDel},
						success:function(respAX){
							if(respAX == 1){
								$.alert({
									title:"Feedback",
									content:"El registro se elimin&oacute; correctamente",
									type:"green",
									useBootstrap:false,
									boxWidth:"50%",
									onClose:function(){
										location.reload(true);
									}
								})
							}else{
								$.alert({
									title:"Feedback",
									content:"Se presentaron problemas en la operación: "+respAX,
									type:"red",
									useBootstrap:false,
									boxWidth:"50%"
								})
							}
						}
					})
				},
				cancel:function(){
					//
				}
			}
		})
    });
	
	$(".editar").click(function(e) {
        var idUpd = $(this).attr("data-editar");
		$.ajax({
			method:"post",
			url:"loadUpd_AX.php",
			data:{id:idUpd},
			cache:false,
			success: function(respAX){
				var infid = JSON.parse(respAX);
				$("#formUpd #id").val(infid.id);
				$("#modalFormUpd #id2").val(infid.id);
				$("#modalFormUpd #contra").val(infid.pass);
				$("#modalFormUpd #tipo").val(infid.tipo);

				Materialize.updateTextFields();
				$("#modalFormUpd").modal("open");
			}
		})
    });
		
	$(".ver").click(function(e) {
        var idVer = $(this).attr("data-ver");
		$.ajax({
			method:"post",
			url:"read_AX.php",
			cache:false,
			data:{id:idVer},
			success: function(respAX){
				var obj = JSON.parse(respAX);
				var cadenaHTML = 
				"<p class='flow-text' style='color:#555;'>"+
					"<i class='fa fa-address-card blue-text'></i>  <b>Identificador:</b> "+obj.id+
					"<br><i class='fa fa-user-circle-o blue-text'></i>  <b>Contrase&ntilde;a:</b> "+obj.pass+
					"<br><i class='fa fa-bullseye blue-text'></i>  <b>Tipo:</b> "+obj.tipo+
				"</p>";
				$("#respAX").html(cadenaHTML);
				$("#modalAX").modal("open");
			}
		});
    });
	
	$("#modalFormUpd").validetta({
		
		bubblePosition:"bottom", bubbleGapTop:10, bubbleGapLeft:-5,
		onValid:function(e){
			e.preventDefault();
			$("#modalFormUpd").modal("close");
			$.ajax({
				method:"post",
				url:"update_AX.php",
				data:$("#formUpd").serialize(),
				cache:false,
				success: function(respAX){
					if(respAX == 1){
						$.alert({
							title:"Feedback",
							content:"Se actualizó correctamente el registro seleccionado",
							type:"green",
							useBootstrap:false,
							boxWidth:"50%",
							onClose:function(){
								location.reload(true);
							}
						});
					}else{
						$.alert({
							title:"Feedback",
							content:"No se pudo actualizar el registro. Vuelva a intentarlo: "+respAX,
							type:"red",
							useBootstrap:false,
							boxWidth:"50%"
						});
					}
				}
			})
		}
	});

	$("#modalFormUpdServ").validetta({
		
		bubblePosition:"bottom", bubbleGapTop:10, bubbleGapLeft:-5,
		onValid:function(e){
			e.preventDefault();
			$("#modalFormUpdServ").modal("close");
			$.ajax({
				method:"post",
				url:"updateServ_AX.php",
				data:$("#formUpdServ").serialize(),
				cache:false,
				success: function(respAX){
					if(respAX == 1){
						$.alert({
							title:"Feedback",
							content:"Se actualizó correctamente el servicio seleccionado",
							type:"green",
							useBootstrap:false,
							boxWidth:"50%",
							onClose:function(){
								location.reload(true);
							}
						});
					}else{
						$.alert({
							title:"Feedback",
							content:"No se pudo actualizar el servicio. Vuelva a intentarlo: "+respAX,
							type:"red",
							useBootstrap:false,
							boxWidth:"50%"
						});
					}
				}
			})
		}
	});
	$("#modalFormDelServ").validetta({
		
		bubblePosition:"bottom", bubbleGapTop:10, bubbleGapLeft:-5,
		onValid:function(e){
			e.preventDefault();
			$("#modalFormDelServ").modal("close");
			$.ajax({
				method:"post",
				url:"deleteServ_AX.php",
				data:$("#formDelServ").serialize(),
				cache:false,
				success: function(respAX){
					if(respAX == 1){
						$.alert({
							title:"Feedback",
							content:"Se elimino correctamente el servicio seleccionado",
							type:"green",
							useBootstrap:false,
							boxWidth:"50%",
							onClose:function(){
								location.reload(true);
							}
						});
					}else{
						$.alert({
							title:"Feedback",
							content:"No se pudo eliminar el servicio. Vuelva a intentarlo: "+respAX,
							type:"red",
							useBootstrap:false,
							boxWidth:"50%"
						});
					}
				}
			})
		}
	});
	$("#modalFormIns").validetta({
		bubblePosition:"bottom", bubbleGapTop:10, bubbleGapLeft:-5,
		onValid:function(e){
			e.preventDefault();
			$("#modalFormIns").modal("close");
			$.ajax({
				method:"post",
				url:"create_AX.php",
				data:$("#formIns").serialize(),
				cache:false,
				success: function(respAX){
					if(respAX == 1){
						$.alert({
							title:"Feedback",
							content:"Se agregó correctamente el usuario",
							type:"green",
							useBootstrap:false,
							boxWidth:"50%",
							onClose:function(){
								location.reload(true);
							}
						});
					}else{
						if(respAX == 2){
							$.alert({
								title:"SEE",
								content:"No se pudo insertar el registro en los alumnos. Intente con otro id.",
								type:"red",
								useBootstrap:false,
								boxWidth:"50%"
							});
						}
						else{
							$.alert({
								title:"SEE",
								content:"No se pudo insertar el registro en los usuario. Intente con otro usuario. ",
								type:"red",
								useBootstrap:false,
								boxWidth:"50%"
							});
						}
					}
				}
			})
			console.log(respAX);
		}
	});

	$("#modalFormInsServ").validetta({
		bubblePosition:"bottom", bubbleGapTop:10, bubbleGapLeft:-5,
		onValid:function(e){
			e.preventDefault();
			$("#modalFormInsServ").modal("close");
			$.ajax({
				method:"post",
				url:"createServ_AX.php",
				data:$("#formInsServ").serialize(),
				cache:false,
				success: function(respAX){
					if(respAX == 1){
						$.alert({
							title:"Feedback",
							content:"Se agregó correctamente el servicio",
							type:"green",
							useBootstrap:false,
							boxWidth:"50%",
							onClose:function(){
								location.reload(true);
							}
						});
					}else{
						if(respAX == 2){
							$.alert({
								title:"SEE",
								content:"No se pudo insertar el registro en los servicios. Intente con otro id.",
								type:"red",
								useBootstrap:false,
								boxWidth:"50%"
							});
						}
						else{
							$.alert({
								title:"SEE",
								content:"No se pudo insertar el registro en los servicios. Intente con otro servicio. ",
								type:"red",
								useBootstrap:false,
								boxWidth:"50%"
							});
						}
					}
				}
			})
			console.log(respAX);
		}
	});

});