$(function(){
	$('#saveForm').validate({
		rules: {
            movie :{required:true},
            stars :{required:true}
        },messages:{
            movie :{required:"必填"},
            stars :{required:"必填"}
        }
 	});
	$('.saveBtn').click(function(){
	   if($('#saveForm').valid()){
           $.ajax({
               type: "POST",
               url: "./rating/save",
               data: $("#saveForm").serialize(),
               headers: {"Content-type": "application/x-www-form-urlencoded;charset=UTF-8"},
               success: function (data) {
                   if (data == 1) {
                       alert("评分成功");
                       pageaction();
                       closeDialog();
                   } else {
                       alert(data);
                   }
               }
           });
	   }else{
		   alert('数据验证失败，请检查！');
	   }
	});

});	
