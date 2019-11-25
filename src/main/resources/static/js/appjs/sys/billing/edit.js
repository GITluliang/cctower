$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/sys/billing/update",
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
			parkingId : {
				required : true
			} ,
			monthlyPrice : {
				required : true
			} ,
			freeTime : {
				required : true
			} ,
			paidFreeTime : {
				required : true
			} ,
			wechatDiscount : {
				required : true
			} ,
			alipayDiscount : {
				required : true
			} ,
			dayCost : {
				required : true
			} ,
			couponTime : {
				required : true ,
				min : 0
			}
		},
		messages : {
			parkingId : {
				required : icon + "请选择停车场"
			} ,
			monthlyPrice : {
				required : icon + "请输入月租费用"
			} ,
			freeTime : {
				required : icon + "请输入免费停车时间"
			} ,
			paidFreeTime : {
				required : icon + "请输入出厂预留时间"
			} ,
			wechatDiscount : {
				required : icon + "请输入微信折扣百分比"
			} ,
			alipayDiscount : {
				required : icon + "请输入支付宝折扣百分比"
			} ,
			dayCost : {
				required : icon + "请输入24小时最高收费"
			} ,
			couponTime : {
				required : icon + "请输入时长劵时间" ,
				min : icon + "请输入时长劵时间"
			}
		}
	})
}