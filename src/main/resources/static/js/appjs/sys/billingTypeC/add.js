var prefix = "/sys/billing/typeC"
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
		url : prefix + "/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("服务器错误");
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
		errorPlacement: function (error, element) {
			error.appendTo(element.parent().parent());
		},
		rules : {
			timeInterval : {
				required : true,
				min : 1
			},
			unitPrice : {
				required : true,
				min : 1
			}
		},
		messages : {
			timeInterval : {
				required : icon + "请输入时段",
				min : icon + "时段大于0"
			},
			unitPrice : {
				required : icon + "请输入单价",
				min : icon + "单价大于0"
			}
		}
	})
}