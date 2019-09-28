$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/sys/passageway/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().parent());
		},
		rules : {
			name : {
				required : true
			} ,
			ip : {
				required : true
			} ,
			parkingId : {
				required : true
			} ,
			type : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入姓名"
			} ,
			ip : {
				required : icon + "请输入摄像头ip"
			} ,
			parkingId : {
				required : icon + "请选择停车场"
			} ,
			type : {
				required : icon + "请选择出入口"
			}
		}
	})
}